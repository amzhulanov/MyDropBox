package ru.geekbrains.common;

public class CommandMessage extends AbstractMessage {
    public static final String FILE_LIST_REQUEST="request file list";
    public static final String FILE_LIST_SEND="send file list";
    public static final String FILE_DELETE="request file delete";

    private String commandMessage;
    private Object[] attachment;

    public String getCommandMessage() {
        return commandMessage;
    }

    public Object[] getAttachment() {
        return attachment;
    }

    public CommandMessage(String type,Object... attachment) {
        this.commandMessage =type;
        this.attachment=attachment;
    }



    public CommandMessage(String type) {
        this.commandMessage =type;
    }
}
