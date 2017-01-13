package com.alokhin.spring.core.loggers;

import com.alokhin.spring.core.beans.Event;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;

@Component
public class CombinedEventLogger implements EventLogger {

    @Resource(name = "combinedLoggers")
    private Collection<EventLogger> loggers;

    @Override
    public void logEvent(Event event) {
        loggers.stream().forEach(eventLogger -> eventLogger.logEvent(event));
    }
}
