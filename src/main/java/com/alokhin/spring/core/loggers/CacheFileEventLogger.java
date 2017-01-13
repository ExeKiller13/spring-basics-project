package com.alokhin.spring.core.loggers;

import com.alokhin.spring.core.beans.Event;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Component
public class CacheFileEventLogger extends FileEventLogger {

    @Value("${cache.size:5}")
    private int cacheSize;

    private List<Event> cache;

    public CacheFileEventLogger(String filename, int cacheSize) {
        super(filename);

        this.cacheSize = cacheSize;
    }

    public CacheFileEventLogger() {
    }

    @PostConstruct
    public void initCache() {
        this.cache = new ArrayList<Event>(cacheSize);
    }

    @Override
    public void logEvent(Event event) {
        cache.add(event);

        if (cache.size() == cacheSize) {
            writeEventsFromCache();
            cache.clear();
        }
    }

    private void writeEventsFromCache() {
        cache.stream().forEach(super::logEvent);
    }

    @PreDestroy
    public void destroy() {
        if (!cache.isEmpty()) {
            writeEventsFromCache();
            cache.clear();
        }
    }
}
