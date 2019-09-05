package ru.geekbrains.common;

public class CommandMessage extends AbstractMessage {
    public static final String FILE_LIST_REQUEST="request file list";
    public static final String FILE_DELETE="request file delete";
    public static final String AUTH_SUCCESS_RESPONSE = "auth successful";
    public static final String AUTH_FAIL_RESPONSE = "auth fail";

    private String commandMessage;
    private String user;
    private Object[] attachment;

    public String getCommandMessage() {
        return commandMessage;
    }

    public Object[] getAttachment() {
        return attachment;
    }

    public CommandMessage(String type,String user,Object... attachment) {
        this.commandMessage =type;
        this.attachment=attachment;
        this.user=user;
    }



    public CommandMessage(String type) {
        this.commandMessage =type;
    }
    public CommandMessage(String type,String user) {
        this.commandMessage =type;
        this.user=user;
    }

    public String getUser() {
        return this.user;
    }
}
