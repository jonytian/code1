package com.example.logsys.netty;

import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;

public class MyChannelHandler {
    public MyChannelHandler(){}

    public static Map<String, ChannelId> channelIdMap = new HashMap<>();
    //用于存放用户Channel信息，也可以建立map结构模拟不同的消息群
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ChannelGroup getChannelGroup() {
        return channelGroup;
    }
}
