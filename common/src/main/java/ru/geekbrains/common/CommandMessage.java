package ru.geekbrains.common;

public class CommandMessage extends AbstractMessage {
    public static final String FILE_LIST_REQUEST="request file list";
    public static final String FILE_DELETE="request file delete";
    public static final String AUTH_SUCCESS_RESPONSE = "auth successful";
    public static final String AUTH_FAIL_RESPONSE = "auth fail";
    public static final String REG_SUCCESS_RESPONSE = "reg successful";
    public static final String REG_FAIL_RESPONSE = "reg fail";


    private String commandMessage;
    private String user;
    private Object[] attachment;
    private Exception ex;

    public String getCommandMessage() {
        return commandMessage;
    }

    public Object[] getAttachment() {
        return attachment;
    }

    public CommandMessage(String type) {
        this.commandMessage =type;
    }
    public CommandMessage(String type,Exception ex) {
        this.commandMessage =type;
        this.ex=ex;
    }
    public CommandMessage(String type,String user) {
        this.commandMessage =type;
        this.user=user;
    }

    public CommandMessage(String type,String user,Object... attachment) {
        this.commandMessage =type;
        this.attachment=attachment;
        this.user=user;
    }

    public Exception getEx() {
        return ex;
    }

    public String getUser() {
        return this.user;
    }
}
