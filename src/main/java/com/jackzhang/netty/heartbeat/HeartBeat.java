package com.jackzhang.netty.heartbeat;

/**
 * 心跳内容
 * Created by Jack on 2017/4/22.
 */
public class HeartBeat {

    /**
     *客户端发送心跳包内容
     */
    public static final String HEARTBEAT_REQUEST_CLIENT = "HEARTBEAT_REQUEST_CLIENT";
    /**
     * 服务端返回心跳包内容
     */
    public static final String HEARTBEAT_RESPONSE_SERVER = "HEARTBEAT_RESPONSE_SERVER";
}
