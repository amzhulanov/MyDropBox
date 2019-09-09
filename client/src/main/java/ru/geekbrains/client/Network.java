package ru.geekbrains.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.*;
import ru.geekbrains.common.AbstractMessage;

import ru.geekbrains.common.AuthMessage;

public class Network {
    private final static int MAX_OBJ_SIZE = 300 * 1024 * 1024;

    private static Network outInstance = new Network();

    public static Network getInstance() {
        return outInstance;
    }

    private static Socket socket;//=new Socket();

    private static ObjectEncoderOutputStream out;
    private static ObjectDecoderInputStream in;

    private Channel currentChannel;

    public Channel getCurrentChannel() {
        return currentChannel;
    }

    public static void start() {
        try {
            socket = new Socket("localhost", 8189);
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
            in = new ObjectDecoderInputStream(socket.getInputStream(), MAX_OBJ_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public void start(){
//        //final EventLoopGroup mainGroup = new NioEventLoopGroup();
//        final EventLoopGroup group = new NioEventLoopGroup();
//        try {
//            Bootstrap bootstrap = new Bootstrap();
//            bootstrap.group(group);
//            bootstrap.channel(NioSocketChannel.class);
//            bootstrap.remoteAddress(new InetSocketAddress("localhost", 8188));
//            bootstrap.handler((new ChannelInitializer<>() {})(socketChannel) {
//                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            socketChannel.pipeline().addLast(
//                                    new ObjectDecoder(MAX_OBJ_SIZE, ClassResolvers.cacheDisabled(null)),
//                                    new ObjectEncoder(),
//                                    new MainHandler()
//                            );
//                        }
//            }
//
//                    )
//        }
//                    .childOption(ChannelOption.SO_KEEPALIVE, true);
//
//            final int inetPort = 8189;
//            final ChannelFuture future = b.bind(inetPort).sync();
//            future.channel().closeFuture().sync();
//        } finally {
//            mainGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
//    }

    public static void stop() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMsg(AbstractMessage msg) {
        try {
            out.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AbstractMessage sendLogin(AbstractMessage msg) throws ClassNotFoundException, IOException {
        start();
        System.out.println("Пройден Network.start()  msg=" + msg.toString());
        out.writeObject(msg);
        Object obj = in.readObject();//клиент постоянно читает канал
        stop();
        return (AbstractMessage) obj;
    }

    public static AbstractMessage readObject() throws ClassNotFoundException, IOException {
        Object obj = in.readObject();//клиент постоянно читает канал
        return (AbstractMessage) obj; //приводит полученный объект к типу Абстрактного сообщения и возвращает
    }
}
