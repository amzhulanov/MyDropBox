package ru.geekbrains.server.authorization;

public class UserRepr {

    private int id;

    private String login;
    private String name;

    private String password;

    public UserRepr() {
    }

    public UserRepr(Integer id,String login ,String name,  String password) {
        this.id=id;
        this.login = login;
        this.name = name;
        this.password = password;
    }
    public UserRepr(String login,String name, String password) {
        this.login = login;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
