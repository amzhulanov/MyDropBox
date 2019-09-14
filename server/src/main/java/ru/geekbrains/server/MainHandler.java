package ru.geekbrains.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import ru.geekbrains.common.*;
import ru.geekbrains.common.Exception.RegLoginException;
import ru.geekbrains.common.Exception.RegPasswordException;
import ru.geekbrains.common.Exception.ResourceException;
import ru.geekbrains.server.EventLog.EventLogJdbcImpl;
import ru.geekbrains.server.authorization.UserRepository;
import ru.geekbrains.server.authorization.UserRepr;
import ru.geekbrains.common.NewDirectory;

import static ru.geekbrains.common.CommandMessage.*;
import static ru.geekbrains.server.ConnectionH2.con;

public class MainHandler extends ChannelInboundHandlerAdapter {

    private final String SERVER_STORAGE = "server/server_storage/";
    private final NewDirectory newDirectory=new NewDirectory();
    private static EventLogJdbcImpl eventLogJdbc;
    private static final Logger logger = Logger.getLogger(MainHandler.class.getName());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        eventLogJdbc = new EventLogJdbcImpl(con);
        logger.setLevel(Level.FINE);
        logger.getParent().setLevel(Level.FINE);
        logger.getParent().getHandlers()[0].setLevel(Level.FINE);

        logger.addHandler(new Handler() {
            @Override
            public void publish(LogRecord record) {
                eventLogJdbc.insertLog(record);
            }

            @Override
            public void flush() {
                flush();
            }

            @Override
            public void close() throws SecurityException {
            }
        });

        try {
            if (msg != null) {
                if (msg instanceof AuthMessage) {
                    logger.info("(AuthMessage) msg="+((AuthMessage) msg).getLogin());
                    checkUserLogin(ctx, (AuthMessage) msg);
                } else if (msg instanceof RegMessage) {
                    logger.info("(AuthMessage) msg="+((RegMessage) msg).getLogin());
                    regUser(ctx, (RegMessage) msg);
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
            ReferenceCountUtil.release(msg);
        }
    }

    private void fileListRequest(ChannelHandlerContext ctx, FileRequest fileRequest, String user) throws IOException {
        if (Files.exists(Paths.get(SERVER_STORAGE + user + "/" + fileRequest.getFilename()))) {
            ctx.writeAndFlush(new FileMessage(Paths.get(SERVER_STORAGE + user + "/" + fileRequest.getFilename()), user));
        }
    }

    private void fileWriteToServer(FileMessage fm, String user) throws IOException {
        Files.write(Paths.get(SERVER_STORAGE + user + "/" + fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
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

    private void checkUserLogin(ChannelHandlerContext ctx, AuthMessage authMessage) throws SQLException {
        UserRepr user = new UserRepr();
        UserRepository userRepository = new UserRepository();
        try {
            user = userRepository.findByLogin(authMessage.getLogin());
            if (user != null && user.getPassword().equals(authMessage.getPassword())) {
                logger.info("AUTH_SUCCESS_RESPONSE login="+user.getLogin());
                ctx.writeAndFlush(new CommandMessage(AUTH_SUCCESS_RESPONSE, user.getLogin()));
            }else{
                logger.warning("AUTH_FAIL_RESPONSE login="+authMessage.getLogin());
                ctx.writeAndFlush(new CommandMessage(AUTH_FAIL_RESPONSE));
            }
        } catch (Exception e) {
            logger.warning("SQLException: "+e);
        }
    }

    private void regUser(ChannelHandlerContext ctx, RegMessage msg) throws SQLException {
        UserRepository userRepository = new UserRepository();
        if (msg.getPassword() == null || msg.getPasswordRepeat() == null || !msg.getPassword().equals(msg.getPasswordRepeat())) {
            logger.warning("RegPasswordException");
            ctx.writeAndFlush(new CommandMessage(REG_FAIL_RESPONSE, new RegPasswordException()));
        }
        if (userRepository.findByLogin(msg.getLogin()) != null) {
            logger.warning("REG_FAIL_RESPONSE - user already exist");
            ctx.writeAndFlush(new CommandMessage(REG_FAIL_RESPONSE, new RegLoginException()));
        }
        if (userRepository.insert(new UserRepr(msg.getLogin(), msg.getName(), msg.getPassword()))) {
            if (newDirectory.createDir(SERVER_STORAGE,msg.getLogin())){
                logger.info("Created dir = "+msg.getLogin());
            }else{
                logger.warning("Don`t create dir = "+msg.getName());
            };
            logger.info("REG_SUCCESS_RESPONSE");
            ctx.writeAndFlush(new CommandMessage(REG_SUCCESS_RESPONSE));
        } else {
            logger.warning("REG_FAIL_RESPONSE - unknown error");
            ctx.writeAndFlush(new CommandMessage(REG_FAIL_RESPONSE, new ResourceException()));
        }
    }

}
