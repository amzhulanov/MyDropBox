package ru.geekbrains.server.authorization;


import ru.geekbrains.common.User;
import ru.geekbrains.common.UserRepr;

import java.sql.*;

public class UserRepository {
    private final Connection conn;
    private Statement statmt = null;

    public UserRepository(Connection conn) throws SQLException {

        this.conn = conn;

        try {
            statmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public boolean insert(UserRepr user)  { //добавляю нового пользователя
        try {
            PreparedStatement preparedStatement=conn.prepareStatement("insert into users (login,password) values(?,?)");
            preparedStatement.setString(1,user.getLogin());
            preparedStatement.setString(2,user.getPassword());

            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (SQLException e) {
            return false;
        }




    }

    public UserRepr findByLogin(String login)  {//ищу пользователя в БД по логину
        boolean exist=false;
        ResultSet resultSet = null;
        try {
            resultSet = statmt.executeQuery("select id,login,password from users where login='" + login + "'");

            exist = resultSet.next();
            if (exist) {
                return new UserRepr(resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3));
            } else {
                return null;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
