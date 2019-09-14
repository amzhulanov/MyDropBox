package ru.geekbrains.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.IOException;
import java.util.logging.Logger;

public class Server {
    private final static int MAX_OBJ_SIZE = 300 * 1024 * 1024;
    private ConnectionH2 connectionH2=new ConnectionH2();
    public static final Logger logger = Logger.getLogger(Server.class.getName());

    private void run() throws Exception {
        final EventLoopGroup mainGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        connectionH2.connect();
        try {
            final ServerBootstrap b = new ServerBootstrap();
                b.group(mainGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(
                                        new ObjectDecoder(MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
                                        new ObjectEncoder(),
                                        new MainHandler()
                                );
                            }
                        })
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

            final int inetPort = 8189;
            final ChannelFuture future = b.bind(inetPort).sync();
            future.channel().closeFuture().sync();
        }
        finally {
            mainGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {

        new Server().run();

    }



}
