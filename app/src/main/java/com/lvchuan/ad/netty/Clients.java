package com.lvchuan.ad.netty;

import android.util.Log;

import java.net.URI;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class Clients {

    private NioEventLoopGroup worker = new NioEventLoopGroup();

    public static Channel channel;

    private Bootstrap bootstrap;

    private static Clients mSingleton = null;
    private Clients() {}

    public static Clients getInstance() {
        if (mSingleton == null) {
            synchronized (Clients.class) {
                if (mSingleton == null) {
                    mSingleton = new Clients();
                }
            }
        }
        return mSingleton;
    }


    public void start() {
        bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        // TODO Auto-generated method stub
                        ChannelPipeline pipeline = ch.pipeline();
            /*    pipeline.addLast(new IdleStateHandler(0,0,5));
                pipeline.addLast(new ClientHandler());*/
                        pipeline.addLast("http-codec",new HttpClientCodec());
                        pipeline.addLast("aggregator",new HttpObjectAggregator(65536));
                        pipeline.addLast("hookedHandler", new WebSocketClientHandler());
                        pipeline.addLast("decoder", new StringDecoder());
//              pipelinenel.pipeline().addLast("encoder", new StringEncoder());
                        pipeline.addLast("handler", new ClientHandler());
                        pipeline.addLast(new IdleStateHandler(60,60,120, TimeUnit.SECONDS));
                        pipeline.addLast(new ReconnHandler());
                    }
                });
        doConnect();
    }

    /**
     * 连接服务端 and 重连
     */
    public void doConnect() {
        try {

            if (channel != null && channel.isActive()){
                return;
            }
            URI webSocketURI = new URI("ws://192.168.11.130:8095/netty/ws");
            HttpHeaders httpHeaders = new DefaultHttpHeaders();

            // 握手
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(webSocketURI, WebSocketVersion.V13, (String) null, true, httpHeaders);
            // 连接通道
            ChannelFuture connect = bootstrap.connect(webSocketURI.getHost(), webSocketURI.getPort());
            channel = connect.sync().channel();

            WebSocketClientHandler handler = (WebSocketClientHandler) channel.pipeline().get("hookedHandler");
            handler.setHandshaker(handshaker);
            handshaker.handshake(channel);
            //实现监听通道连接的方法
            connect.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {

                    if(channelFuture.isSuccess()){
                        channel = channelFuture.channel();
                        System.out.println("连接服务端成功");
                    }else{
                        System.out.println("每隔2s重连....");
                        channelFuture.channel().eventLoop().schedule(new Runnable() {

                            @Override
                            public void run() {
                                doConnect();
                            }
                        },2, TimeUnit.SECONDS);
                    }
                }
            });

        }catch (Exception e){
            Log.e("11",e.getMessage());
        }
    }
    /**
     * 向服务端发送消息
     */
    public void sendData() {
        Scanner sc= new Scanner(System.in);
        for (int i = 0; i < 1000; i++) {

            if(channel != null && channel.isActive()){
                //获取一个键盘扫描器
                String nextLine = sc.nextLine();
                channel.writeAndFlush(nextLine);
            }
        }
    }
}