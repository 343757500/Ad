package com.lvchuan.ad.netty;
import com.google.gson.Gson;
import com.lvchuan.ad.bean.NettyCmdBean;

import org.simple.eventbus.EventBus;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.util.CharsetUtil;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {/**
 * 握手的状态信息
 */
private WebSocketClientHandshaker handshaker;

    /**
     * netty自带的异步处理
     */
    private ChannelPromise handshakeFuture;
    boolean flag;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("当前握手的状态"+this.handshaker.isHandshakeComplete());
        if (this.handshaker.isHandshakeComplete()){

        }

        Channel ch = ctx.channel();
        FullHttpResponse response;
        // 进行握手操
        if (!this.handshaker.isHandshakeComplete()) {
            try {
                response = (FullHttpResponse)msg;
                // 握手协议返回，设置结束握手
                this.handshaker.finishHandshake(ch, response);
                // 设置成功
                this.handshakeFuture.setSuccess();
                System.out.println("服务端的消息"+response.headers());
            } catch (WebSocketHandshakeException var7) {
                FullHttpResponse res = (FullHttpResponse)msg;
                String errorMsg = String.format("握手失败,status:%s,reason:%s", res.status(), res.content().toString(CharsetUtil.UTF_8));
                this.handshakeFuture.setFailure(new Exception(errorMsg));
            }
        } else if (msg instanceof FullHttpResponse) {
            response = (FullHttpResponse)msg;
            throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        } else {
            // 接收服务端的消息
            WebSocketFrame frame = (WebSocketFrame)msg;
            // 文本信息
            if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame textFrame = (TextWebSocketFrame)frame;
                ByteBuf content = textFrame.content();
                String s =content.toString(CharsetUtil.UTF_8);
                System.out.println(new Date() + "Client reciver heart beat message : ----> " + s);


                if (s.equals("pong")) {

                }else if (s.startsWith("{")){
                    NettyCmdBean nettyCmdBean = new Gson().fromJson(s, NettyCmdBean.class);
                    EventBus.getDefault().post(nettyCmdBean, "nettyCmdBean");
                    System.out.println("客户端接收的消息是:"+new Gson().toJson(nettyCmdBean));
                }

            }

        }
    }

    /**
     * Handler活跃状态，表示连接成功
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.executor().scheduleWithFixedDelay(

                new HeartBeatTask(ctx), 5, 50, TimeUnit.SECONDS);
        System.out.println("活跃状态"+ctx);

        //netty的发送
        EventBus.getDefault().post("devId","devId");
        //发送关闭服务
        EventBus.getDefault().post("0","startService");
    }

    /**
     * 非活跃状态，没有连接远程主机的时候。
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("主机关闭"+ctx);

        //StopThread.getInstance().start();
        Clients.getInstance().doConnect();;

        //发送开启服务
        EventBus.getDefault().post("1","startService");

    }


    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("连接异常："+cause.getMessage());
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        this.handshakeFuture = ctx.newPromise();
    }

    public WebSocketClientHandshaker getHandshaker() {
        return handshaker;
    }

    public void setHandshaker(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelPromise getHandshakeFuture() {
        return handshakeFuture;
    }

    public void setHandshakeFuture(ChannelPromise handshakeFuture) {
        this.handshakeFuture = handshakeFuture;
    }

    ChannelFuture handshakeFuture() {
        return this.handshakeFuture;
    }

    private class HeartBeatTask implements Runnable {

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


}

