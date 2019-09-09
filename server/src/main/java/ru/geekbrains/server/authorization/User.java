package ru.geekbrains.server.authorization;

import java.io.Serializable;

public class User implements Serializable {

    private int id;

    private String login;

    private String password;

    public User() {

    }
    public User(int id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public User(UserRepr user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}