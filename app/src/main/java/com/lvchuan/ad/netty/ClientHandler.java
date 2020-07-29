package com.lvchuan.ad.netty;

import org.simple.eventbus.EventBus;

import java.util.Date;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * Created by ffy on 19-1-21.
 */

public class ClientHandler extends SimpleChannelInboundHandler<String> {

//    public static Map<String , Channel> map = new ConcurrentHashMap<>();

    private volatile ScheduledFuture<?> heartBeat;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {
        //50秒钟发一个心跳

        System.out.println(new Date() + "Client reciver heart beat message : ----> " + message);


        if (message.startsWith("{"))
            EventBus.getDefault().post(message, "handlecmd");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.executor().scheduleWithFixedDelay(
//                new ClientHandler.HeartBeatTask(ctx), 5, 50, TimeUnit.SECONDS);
    }



}