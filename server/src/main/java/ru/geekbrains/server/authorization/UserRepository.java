package ru.geekbrains.server.authorization;


import ru.geekbrains.server.ConnectionH2;

import java.sql.*;

public class UserRepository {
private ConnectionH2 connectionH2=new ConnectionH2();

    public UserRepository() throws SQLException {
            connectionH2.connect();
    }

    public boolean insert(UserRepr user) throws SQLException { //добавляю нового пользователя
            return connectionH2.insert(user);
    }

    public UserRepr findByLogin(String login)  {//ищу пользователя в БД по логину
       return connectionH2.findByLogin(login);
    }


}
