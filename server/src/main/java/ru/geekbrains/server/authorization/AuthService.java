package ru.geekbrains.server.authorization;

import ru.geekbrains.common.Exception.LoginNonExistent;
import ru.geekbrains.common.Exception.RegLoginException;
import ru.geekbrains.common.User;

import java.sql.SQLException;

public interface AuthService {
    boolean authUser(User user) throws LoginNonExistent, SQLException;

    void registrationUser(User user) throws RegLoginException;
}
