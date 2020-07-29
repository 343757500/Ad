package com.lvchuan.ad.netty;

import android.util.Log;

import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
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

/**
 * Created by ffy on 19-1-21.
 */

public class Client extends Thread {

     static String host = "ws://192.168.11.130:8095/recycle-box/webSocket";
//    public static String host = "192.168.31.202";
//121 130 202
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public static Channel channel;
    public static int port = 8820;

    public void run() {

        super.run();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel SocketChannel) throws Exception {
//                                SocketChannel.pipeline().addLast("framer",new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            SocketChannel.pipeline().addLast("http-codec",new HttpClientCodec());
                            SocketChannel.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
                            SocketChannel.pipeline().addLast("hookedHandler", new WebSocketClientHandler());
                            SocketChannel.pipeline().addLast("decoder", new StringDecoder());
//                            SocketChannel.pipeline().addLast("encoder", new StringEncoder());
                            SocketChannel.pipeline().addLast("handler", new ClientHandler());
                            SocketChannel.pipeline().addLast(new IdleStateHandler(60,60,120, TimeUnit.SECONDS));
                            SocketChannel.pipeline().addLast(new ReconnHandler());
                        }
                    });
            // 这里的路径即为网关中配置的路由路径
            URI webSocketURI = new URI(host);
            HttpHeaders httpHeaders = new DefaultHttpHeaders();

            // 握手
            WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(webSocketURI, WebSocketVersion.V13, (String) null, true, httpHeaders);

            // 连接通道
            channel = bootstrap.connect(webSocketURI.getHost(),webSocketURI.getPort()).sync().channel();
            WebSocketClientHandler handler = (WebSocketClientHandler) channel.pipeline().get("hookedHandler");

            handler.setHandshaker(handshaker);
            handshaker.handshake(channel);

            // 阻塞等待是否握手成功
            handler.handshakeFuture().sync();

            // 给服务端发送的内容，如果客户端与服务端连接成功后，可以多次掉用这个方法发送消息
                    /*Thread.currentThread();
                    Thread.sleep(1000);*/

        } catch (Exception e) {
           // e.printStackTrace();
            Log.e("error",e.getMessage());
        }
    }

}
