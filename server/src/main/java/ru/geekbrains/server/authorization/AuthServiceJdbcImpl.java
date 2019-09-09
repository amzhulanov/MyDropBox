package ru.geekbrains.server.authorization;

import ru.geekbrains.common.Exception.LoginNonExistent;
import ru.geekbrains.common.Exception.RegLoginException;
import ru.geekbrains.common.UserRepr;

import java.sql.SQLException;

public class AuthServiceJdbcImpl implements AuthService {

    private UserRepository userRepository;

    public AuthServiceJdbcImpl(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public boolean authUser(UserRepr user) throws LoginNonExistent, SQLException {//авторизовываю пользователя через userRepository
        String login = user.getLogin();
        UserRepr searchUser = null;
        searchUser = userRepository.findByLogin(login);//проверяю наличие имени в базе
        if (searchUser==null){
            throw new LoginNonExistent();
        }
        String pwd = user.getPassword();
        return pwd != null && pwd.equals(searchUser.getPassword());
    }


    @Override
    public void registrationUser(UserRepr user) throws RegLoginException {
        if (!userRepository.insert(user)) {
            throw new RegLoginException();
        }

    }


}
