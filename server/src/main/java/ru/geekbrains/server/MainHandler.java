package ru.geekbrains.server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.geekbrains.common.CommandMessage;
import ru.geekbrains.common.FileListMessage;
import ru.geekbrains.common.FileMessage;
import ru.geekbrains.common.FileRequest;

import static ru.geekbrains.common.CommandMessage.FILE_LIST_REQUEST;
import static ru.geekbrains.common.CommandMessage.FILE_LIST_SEND;

public class MainHandler extends ChannelInboundHandlerAdapter {
    @Override//сервер считывает информацию из канала
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null) {
                return;
            }
            if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
                if (Files.exists(Paths.get("server_storage/" + fr.getFilename()))) {
                    FileMessage fm = new FileMessage(Paths.get("server_storage/" + fr.getFilename()));
                    ctx.writeAndFlush(fm);
                }
            }
            //обрабатываю запрос списка файлов на сервере
            if (((CommandMessage) msg).getCommandMessage().equals(FILE_LIST_REQUEST)) {
                //создаю список файлов на сервере
                FileListMessage flm=new FileListMessage(Paths.get("server_storage"));

            //список файлов с server_storage отправляю в ответном сообщении.
                ctx.writeAndFlush(flm);
                System.out.println("Отправлен список файлов");
        }

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
