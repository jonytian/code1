package com.legaoyi.protocol.down.messagebody;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.legaoyi.protocol.message.MessageBody;

/**
 * 提问下发
 * 
 * @author <a href="mailto:shengbo.gao@gmail.com;78772895@qq.com">gaoshengbo</a>
 * @version 1.0.0
 * @since 2015-01-30
 */
@Scope("prototype")
@Component(MessageBody.MESSAGE_BODY_BEAN_PREFIX + "8302_2011" + MessageBody.MESSAGE_BODY_BEAN_SUFFIX)
public class JTT808_8302_MessageBody extends MessageBody {

    private static final long serialVersionUID = -1826119762827732057L;

    public static final String MESSAGE_ID = "8302";

    /** 标志 **/
    @JsonProperty("flag")
    private String flag;

    /** 问题 **/
    @JsonProperty("question")
    private String question;

    /** 候选答案列表,key/val键值对，包括答案id：answerId，答案内容：answer **/
    private List<Map<String, Object>> answerList;

    public final String getFlag() {
        return flag;
    }

    public final void setFlag(String flag) {
        this.flag = flag;
    }

    public final String getQuestion() {
        return question;
    }

    public final void setQuestion(String question) {
        this.question = question;
    }

    public final List<Map<String, Object>> getAnswerList() {
        return answerList;
    }

    public final void setAnswerList(List<Map<String, Object>> answerList) {
        this.answerList = answerList;
    }

}
