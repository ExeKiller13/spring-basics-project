package com.alokhin.spring.core.loggers;

import com.alokhin.spring.core.beans.Event;
import org.springframework.stereotype.Component;

@Component
public class ConsoleEventLogger implements EventLogger {

    @Override
    public void logEvent(Event event) {
        System.out.println(event);
    }
}
