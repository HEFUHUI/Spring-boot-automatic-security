package com.mrhui.automatic.service;

import com.mrhui.automatic.entity.Logging;

/**
*
*/
public interface LoggingService extends CrudService<Logging> {
    void log(int logType, boolean logOutcome, String userId, String comment);
    void error(boolean logOutcome, String userId, String comment);
    void login(boolean logOutcome, String userId, String comment);
    void logout(boolean logOutcome, String userId, String comment);
    void face(boolean logOutcome, String userId, String comment);
    void warning(boolean logOutcome, String userId, String comment);
}
