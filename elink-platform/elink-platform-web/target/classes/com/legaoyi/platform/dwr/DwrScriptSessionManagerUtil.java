package com.legaoyi.platform.dwr;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.directwebremoting.Container;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.event.ScriptSessionEvent;
import org.directwebremoting.event.ScriptSessionListener;
import org.directwebremoting.extend.ScriptSessionManager;
import org.directwebremoting.servlet.DwrServlet;

public class DwrScriptSessionManagerUtil extends DwrServlet {

    private static final long serialVersionUID = -2212500383885820122L;

    private static final String KEY_ENTERPRISEID = "enterpriseId";

    public void init() throws ServletException {
        Container container = ServerContextFactory.get().getContainer();
        ScriptSessionManager manager = container.getBean(ScriptSessionManager.class);
        ScriptSessionListener listener = new ScriptSessionListener() {

            public void sessionCreated(ScriptSessionEvent ev) {
                HttpSession session = WebContextFactory.get().getSession();
                ScriptSession scriptSession = ev.getSession();
                Map<?, ?> user = (Map<?, ?>) session.getAttribute("user");
                scriptSession.setAttribute(KEY_ENTERPRISEID, user.get(KEY_ENTERPRISEID));
            }

            public void sessionDestroyed(ScriptSessionEvent ev) {}
        };
        manager.addScriptSessionListener(listener);
    }
}
