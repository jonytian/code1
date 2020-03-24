package com.legaoyi.platform.dwr;

import org.apache.commons.lang3.StringUtils;
import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.legaoyi.platform.util.JsonUtil;
import com.legaoyi.rabbitmq.NotifyMessageRabbitmqListener;

@RemoteProxy
public class DwrMessagePusher {

    private static final Logger logger = LoggerFactory.getLogger(NotifyMessageRabbitmqListener.class);

    private static final String KEY_ENTERPRISEID = "enterpriseId";

    private static final String KEY_MESSAGE = "message";

    @RemoteMethod
    public void init() {
        // ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
        // scriptSession.setAttribute("userId", userId);
        DwrScriptSessionManagerUtil dwrScriptSessionManagerUtil = new DwrScriptSessionManagerUtil();
        try {
            dwrScriptSessionManagerUtil.init();
        } catch (ServletException e) {
            logger.error("init  dwr script session error", e);
        }
    }

    @RemoteMethod
    public void pushMessage(final String message) {
        try {
            final Map<?, ?> map = JsonUtil.json2Map(message);
            Browser.withAllSessionsFiltered(new ScriptSessionFilter() {

                public boolean match(ScriptSession session) {
                    Map<?, ?> message = (Map<?, ?>) map.get(KEY_MESSAGE);
                    logger.info("message"+message);
                    // 全局通知消息
                    String enterpriseId = (String) message.get(KEY_ENTERPRISEID);
                    Object type = message.get("type");
                    if (StringUtils.isBlank(enterpriseId)) {
                        return true;
                    }

                    // 自定义过滤规则
                    String userEnterpriseId = (String) session.getAttribute(KEY_ENTERPRISEID);
                    if (StringUtils.isBlank(userEnterpriseId)) {
                        return false;
                    }
                    else {
                        String key = "messageType_" + type;
                        Long lastTime = (Long) session.getAttribute(key);
                        long now = System.currentTimeMillis();
                        // 重复消息类型暂不推送，避免阻塞卡死DWR
                        boolean bool = enterpriseId.startsWith(userEnterpriseId) && (lastTime == null || ((now - lastTime) >= 1000));
                        if (bool) {
                            session.setAttribute(key, now);
                        }
                        return bool;
                    }
                }
            }, new Runnable() {

                private ScriptBuffer script = new ScriptBuffer();

                public void run() {
                    // 设置要调用的 js及参数
                    script.appendCall("handleDwrPushMsg", message);
                    // 得到所有ScriptSession
                    Collection<ScriptSession> colls = Browser.getTargetSessions();
                    // 遍历每一个ScriptSession
                    for (ScriptSession scriptSession : colls) {
                        scriptSession.addScript(script);
                    }
                }
            });
        } catch (Exception e) {
            logger.error("pushMessage error", e);
        }
    }

}
