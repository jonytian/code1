package com.legaoyi.file.server.util;

import java.util.List;
import java.util.Map;

import com.legaoyi.file.messagebody.Attachment;

import io.netty.util.AttributeKey;

public class Constants {

    public static final String PROTOCOL_VERSION = "tjsatl";

    public static final AttributeKey<Map<String, Object>> ATTRIBUTE_SESSION_ATTACHMENT_INFO = AttributeKey.valueOf("attachment_info");

    public static final AttributeKey<Map<String, Object>> ATTRIBUTE_SESSION_ATTACHMENT_LIST = AttributeKey.valueOf("attachment_list");

    public static final AttributeKey<List<Attachment>> ATTRIBUTE_SESSION_ATTACHMENT = AttributeKey.valueOf("attachment");

    public static final AttributeKey<String> ATTRIBUTE_SESSION_STATE = AttributeKey.valueOf("session_state");

}
