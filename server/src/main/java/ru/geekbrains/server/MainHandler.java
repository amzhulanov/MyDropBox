package ru.geekbrains.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.geekbrains.common.CommandMessage;
import ru.geekbrains.common.FileListMessage;
import ru.geekbrains.common.FileMessage;
import ru.geekbrains.common.FileRequest;

import static ru.geekbrains.common.CommandMessage.FILE_DELETE;
import static ru.geekbrains.common.CommandMessage.FILE_LIST_REQUEST;

public class MainHandler extends ChannelInboundHandlerAdapter {

    private final String SERVER_STORAGE = "server_storage/";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null) {
                return;
            }
            if (msg instanceof FileRequest) {
                FileRequest fr = (FileRequest) msg;
                if (Files.exists(Paths.get(SERVER_STORAGE + fr.getFilename()))) {
                    FileMessage fm = new FileMessage(Paths.get(SERVER_STORAGE + fr.getFilename()));
                    ctx.writeAndFlush(fm);
                }
            } else if (msg instanceof FileMessage) {
                FileMessage fm = (FileMessage) msg;
                Files.write(Paths.get(SERVER_STORAGE + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
            } else if (msg instanceof CommandMessage && ((CommandMessage) msg).getCommandMessage().equals(FILE_LIST_REQUEST)) {
                FileListMessage flm = new FileListMessage(Paths.get(SERVER_STORAGE));
                ctx.writeAndFlush(flm);
            } else if (msg instanceof CommandMessage && ((CommandMessage) msg).getCommandMessage().equals(FILE_DELETE)) {
                Object[] object = ((CommandMessage) msg).getAttachment();
                delete(((FileRequest) object[0]).getFilename());
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void delete(String filename) throws IOException {
        if (Files.exists(Paths.get(SERVER_STORAGE + filename))) {
            Files.delete(Paths.get(SERVER_STORAGE + filename));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
