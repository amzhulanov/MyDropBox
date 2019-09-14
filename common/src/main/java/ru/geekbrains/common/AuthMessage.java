package ru.geekbrains.common;

public class AuthMessage extends AbstractMessage {
    private String login;
    private String name;

    private String password;
    private String passwordRepeat;

    public AuthMessage(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public AuthMessage(String login, String name, String password, String passwordRepeat) {
        this.login = login;
        this.name=name;
        this.password = password;
        this.passwordRepeat=passwordRepeat;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
