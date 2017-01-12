package com.alokhin.spring.core.loggers;

import com.alokhin.spring.core.beans.Event;

import java.util.Collection;

public class CombineEventLogger implements EventLogger {

    private Collection<EventLogger> loggers;

    public CombineEventLogger(Collection<EventLogger> loggers) {
        super();
        this.loggers = loggers;
    }

    @Override
    public void logEvent(Event event) {
        loggers.stream().forEach(eventLogger -> eventLogger.logEvent(event));
    }
}
