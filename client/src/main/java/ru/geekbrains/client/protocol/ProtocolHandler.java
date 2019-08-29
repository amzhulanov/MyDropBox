package ru.geekbrains.client.protocol;

import java.util.HashMap;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ProtocolHandler extends ChannelInboundHandlerAdapter {
    private HashMap<String, Object> map = new HashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
       try {
           while (buf.readableBytes() > 0) {
               System.out.print((char) buf.readByte());
               System.out.flush();
           }
       }finally {
           ReferenceCountUtil.release(msg);
       }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
