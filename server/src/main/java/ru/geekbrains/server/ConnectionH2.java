package ru.geekbrains.server;


import ru.geekbrains.server.authorization.UserRepr;

import java.sql.*;

public class ConnectionH2 {
    private static final String url = "jdbc:h2:C:/GeekBrains/Faculty Java/MyDropBox/server/db/users_mydropbox";
    private static final String user = "root";
    private static final String password = "rootroot1";

    public static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public void connect() {
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void closeConnect() {
        try {
            con.close();
        } catch (SQLException ex) {
        }
        try {
            stmt.close();
        } catch (SQLException ex) {
        }
        try {
            rs.close();
        } catch (SQLException ex) {
        }
    }

    public boolean insert(UserRepr user) throws SQLException {
        try {
            PreparedStatement preparedStatement = con.prepareStatement("insert into users (login,name,password) values(?,?,?)");
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.execute();
            preparedStatement.close();
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }


    public UserRepr findByLogin(String login) {//ищу пользователя в БД по логину
        boolean exist = false;
        try {
            rs = stmt.executeQuery("select id,login,name,password from users where login='" + login + "'");

            exist = rs.next();
            if (exist) {
                return new UserRepr(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
