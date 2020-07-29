package com.lvchuan.ad.netty;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class ReconnHandler extends ChannelInboundHandlerAdapter {

    private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1);


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        super.userEventTriggered(ctx, evt);
       if(evt instanceof IdleStateEvent){
           executor.execute(new Runnable() {
               @Override
               public void run() {
                   System.out.println("Client 尝试重新连接-->>>>>>");
                   //等待InterVAl时间，重连
                   try {
                       TimeUnit.SECONDS.sleep(5);
                       //发起重连
                       Client client = new Client();
                       client.run();
                   } catch (Exception e) {
                       e.printStackTrace();
                   }

               }
           });
       }
    }

}
