package ru.geekbrains.server.EventLog;

import java.util.logging.LogRecord;

public interface EventLog {
    void insertLog(LogRecord record);
}
