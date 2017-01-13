package com.alokhin.spring.core;

import com.alokhin.spring.core.aspects.StatisticsAspect;
import com.alokhin.spring.core.beans.Client;
import com.alokhin.spring.core.beans.Event;
import com.alokhin.spring.core.beans.EventType;
import com.alokhin.spring.core.loggers.EventLogger;
import com.alokhin.spring.core.spring.AppConfig;
import com.alokhin.spring.core.spring.LoggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class App {

    @Autowired
    private Client client;

    @Value("#{T(com.alokhin.spring.core.beans.Event).isDay(8,17) ? cacheFileEventLogger : consoleEventLogger}")
    private EventLogger defaultLogger;

    @Resource(name = "loggerMap")
    private Map<EventType, EventLogger> loggers;

    @Autowired
    private StatisticsAspect statisticsAspect;

    public App() {}

    public App(Client client, EventLogger defaultLogger, Map<EventType, EventLogger> loggers) {
        super();
        this.client = client;
        this.defaultLogger = defaultLogger;
        this.loggers = loggers;
    }

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class, LoggerConfig.class);
        ctx.scan("com.alokhin.spring.core");
        ctx.refresh();

        App app = (App) ctx.getBean("app");

        Client client = (Client) ctx.getBean(Client.class);
        System.out.println("Client greeting: " + client.getGreeting());

        Event event = ctx.getBean(Event.class);
        app.logEvent(EventType.INFO, event, "Some event for 1");

        event = ctx.getBean(Event.class);
        app.logEvent(EventType.INFO, event, "One more event for 1");

        event = ctx.getBean(Event.class);
        app.logEvent(EventType.INFO, event, "And one more event for 1");

        event = ctx.getBean(Event.class);
        app.logEvent(EventType.ERROR, event, "Some event for 2");

        event = ctx.getBean(Event.class);
        app.logEvent(null, event, "Some event for 3");

        app.outputLoggingCounter();

        ctx.close();
    }

    private void outputLoggingCounter() {
        if (statisticsAspect != null) {
            System.out.println("Loggers statistics. Number of calls: ");
            for (Map.Entry<Class<?>, Integer> entry: statisticsAspect.getCounter().entrySet()) {
                System.out.println("    " + entry.getKey().getSimpleName() + ": " + entry.getValue());
            }
        }
    }

    private void logEvent(EventType eventType, Event event, String msg) {
        String message = msg.replaceAll(client.getId(), client.getFullName());
        event.setMsg(message);

        EventLogger logger = loggers.get(eventType);
        if(logger == null) {
            logger = defaultLogger;
        }

        logger.logEvent(event);
    }
}
