package com.lvchuan.ad.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class HeartBeatTask implements Runnable {

    private ChannelHandlerContext ctx;

    public HeartBeatTask(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void run() {
//            Message message = buildHeartMessage();
//            System.out.println(new Date()+"Client send heart beat message : ----> " + message);
        ctx.writeAndFlush(new TextWebSocketFrame("{\"flag\":\"HeartbeatAndDevice\",\"Heartbeat\":\"ping\"}"));
    }

    private Message buildHeartMessage() {
        Message message = new Message();
        return message;
    }
}


