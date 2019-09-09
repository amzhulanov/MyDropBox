package ru.geekbrains.common;

public class UserRepr {

    private int id;

    private String login;

    private String password;

    public UserRepr() {
    }

    public UserRepr(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
    }

    public UserRepr(Integer id,String login, String password) {
        this.id=id;
        this.login = login;
        this.password = password;
    }
    public UserRepr(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

}
