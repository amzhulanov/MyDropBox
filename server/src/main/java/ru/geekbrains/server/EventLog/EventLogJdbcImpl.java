package ru.geekbrains.server.EventLog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.LogRecord;

import static ru.geekbrains.server.Server.logger;

public class EventLogJdbcImpl implements EventLog{
    private final Connection conn;
    private Statement statmt = null;


    public EventLogJdbcImpl(Connection conn) throws SQLException {//создаю таблицу пользователей, если её нет

        this.conn = conn;

        try {
            statmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        statmt.execute("CREATE TABLE if not exists log (id int primary key AUTO_INCREMENT, type_msg varchar(25), datetime_msg datetime, text_msg text)");
       // logger.fine("CREATE TABLE log");
    }

    @Override
    public void insertLog(LogRecord record) {
        try {
            System.out.println(" insertLog Записываю лог в БД");
            PreparedStatement preparedStatement=conn.prepareStatement("insert into log (type_msg,datetime_msg,text_msg) values(?,?,?)");
            preparedStatement.setString(1,record.getLevel().getName());
            preparedStatement.setString(2,String.format("%tF %tT",record.getMillis(),record.getMillis()));
            preparedStatement.setString(3,record.getMessage());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            logger.fine(String.format("insert into log: type=%s text=%s datetime=%s"
                    ,record.getLevel().getName()
                    ,record.getMessage()
                    ,String.format("%tF %tT",record.getMillis())));
            e.printStackTrace();
        }
    }
}
