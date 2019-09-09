package ru.geekbrains.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import ru.geekbrains.common.*;
import ru.geekbrains.common.Exception.RegLoginException;
import ru.geekbrains.common.Exception.RegPasswordException;
import ru.geekbrains.common.Exception.ResourceException;
import ru.geekbrains.server.authorization.*;

import static ru.geekbrains.common.CommandMessage.*;

public class MainHandler extends ChannelInboundHandlerAdapter {

    private final String SERVER_STORAGE = "server_storage/";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg != null) {
                if (msg instanceof AuthMessage) {
                    checkUserLogin(ctx, (AuthMessage) msg);
                } else if (msg instanceof RegMessage) {
                    regUser(ctx,(RegMessage) msg);
                } else if (msg instanceof FileRequest) {
                    fileListRequest(ctx, (FileRequest) msg, ((FileRequest) msg).getUser());
                } else if (msg instanceof FileMessage) {
                    fileWriteToServer((FileMessage) msg, ((FileMessage) msg).getUser());
                } else if (msg instanceof CommandMessage && ((CommandMessage) msg).getCommandMessage().equals(FILE_LIST_REQUEST)) {
                    sendFileList(ctx, ((CommandMessage) msg).getUser());
                } else if (msg instanceof CommandMessage && ((CommandMessage) msg).getCommandMessage().equals(FILE_DELETE)) {
                    deleteFileFromServer(((CommandMessage) msg).getAttachment(), ((CommandMessage) msg).getUser());
                }
            }
        } finally {
            System.out.println();
            ReferenceCountUtil.release(msg);
        }
    }



    private void fileListRequest(ChannelHandlerContext ctx, FileRequest fileRequest, String user) throws IOException {
        if (Files.exists(Paths.get(SERVER_STORAGE + user + "/" + fileRequest.getFilename()))) {
            ctx.writeAndFlush(new FileMessage(Paths.get(SERVER_STORAGE + user + "/" + fileRequest.getFilename()), user));
        }
    }

    private void fileWriteToServer(FileMessage fm, String user) throws IOException {
        Files.write(Paths.get(SERVER_STORAGE + user + "/" + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
    }

    private void sendFileList(ChannelHandlerContext ctx, String user) throws IOException {
        ctx.writeAndFlush(new FileListMessage(Paths.get(SERVER_STORAGE + user + "/")));
    }

    private void deleteFileFromServer(Object[] object, String user) throws IOException {
        if (Files.exists(Paths.get(SERVER_STORAGE + user + "/" + ((FileRequest) object[0]).getFilename()))) {
            Files.delete(Paths.get(SERVER_STORAGE + user + "/" + ((FileRequest) object[0]).getFilename()));
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void checkUserLogin(ChannelHandlerContext ctx, AuthMessage authMessage) {
        ctx.writeAndFlush(new CommandMessage(AUTH_SUCCESS_RESPONSE, authMessage.getUser()));
//        UserRepr user = new UserRepr();
//        UserRepository userRepository;
//
//        try {
//            userRepository = new UserRepository(DriverManager.getConnection("jdbc:mysql://localhost:3306/javaee_test_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Novosibirsk", "root", "rootroot1"));
//        } catch (SQLException e) {
//            return;
//        }
//        try {
//            user = userRepository.findByLogin(authMessage.getUser());
//            if (user != null && user.getPassword().equals(authMessage.getPassword())) {
//                ctx.writeAndFlush(new CommandMessage(AUTH_SUCCESS_RESPONSE, user.getLogin()));
//            }
//        } catch (Exception e) {
//            ctx.writeAndFlush(new CommandMessage(AUTH_FAIL_RESPONSE));
//
//        }
    }

    private void regUser(ChannelHandlerContext ctx, RegMessage msg) {
        ctx.writeAndFlush(new CommandMessage(REG_SUCCESS_RESPONSE));
//        UserRepository userRepository;
//        try {
//            userRepository = new UserRepository(DriverManager.getConnection("jdbc:mysql://localhost:3306/javaee_test_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Novosibirsk", "root", "rootroot1"));
//        } catch (SQLException e) {
//            return;
//        }
//        if (msg.getPassword()==null||msg.getPasswordRepeat()==null|| !msg.getPassword().equals(msg.getPasswordRepeat())){
//            ctx.writeAndFlush(new CommandMessage(REG_FAIL_RESPONSE,new RegPasswordException()));
//        } if (userRepository.findByLogin(msg.getUser())!=null){
//            ctx.writeAndFlush(new CommandMessage(REG_FAIL_RESPONSE,new RegLoginException()));
//        }
//        if (userRepository.insert(new UserRepr(msg.getName(),msg.getPassword()))){
//            createDir(msg.getName());
//            ctx.writeAndFlush(new CommandMessage(REG_SUCCESS_RESPONSE));
//        }else{
//            ctx.writeAndFlush(new CommandMessage(REG_FAIL_RESPONSE,new ResourceException()));
//        }
    }

    private void createDir(String name) {

        //TODO Create Directory new User

    }


}
