package ru.geekbrains.client;

import java.io.IOException;
import java.net.Socket;
import io.netty.handler.codec.serialization.*;
import ru.geekbrains.common.AbstractMessage;


public class Network {
    private final static int MAX_OBJ_SIZE = 300 * 1024 * 1024;

    private static Network outInstance = new Network();

    private static Socket socket;

    private static ObjectEncoderOutputStream out;
    private static ObjectDecoderInputStream in;

    public static void start() {
        try {
            socket = new Socket("localhost", 8189);
            out = new ObjectEncoderOutputStream(socket.getOutputStream());
            in = new ObjectDecoderInputStream(socket.getInputStream(), MAX_OBJ_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        out.writeObject(msg);
        Object obj = in.readObject();
        stop();
        return (AbstractMessage) obj;
    }

    public static AbstractMessage readObject() throws ClassNotFoundException, IOException {
        Object obj = in.readObject();
        return (AbstractMessage) obj;
    }
}
