package ru.geekbrains.server.authorization;

import ru.geekbrains.common.Exception.LoginNonExistent;
import ru.geekbrains.common.Exception.RegLoginException;
import ru.geekbrains.common.UserRepr;

import java.sql.SQLException;

public interface AuthService {
    boolean authUser(UserRepr user) throws LoginNonExistent, SQLException;

    void registrationUser(UserRepr user) throws RegLoginException;
}
