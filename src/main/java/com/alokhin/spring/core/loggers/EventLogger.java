package com.alokhin.spring.core.loggers;

import com.alokhin.spring.core.beans.Event;

public interface EventLogger {

    void logEvent(Event event);
}
