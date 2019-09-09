package ru.geekbrains.common;

public class RegMessage  extends AbstractMessage {
    private String user;
    private String name;

    private String password;
    private String passwordRepeat;




    public RegMessage(String user, String name, String password, String passwordRepeat) {
        this.user = user;
        this.name=name;
        this.password = password;
        this.passwordRepeat=passwordRepeat;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }
}
