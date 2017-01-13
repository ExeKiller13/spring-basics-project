package com.alokhin.spring.core.loggers;

import com.alokhin.spring.core.beans.Event;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Component
public class FileEventLogger extends AbstractLogger {

    @Value("${event.file:target/events_log.txt}")
    private String filename;

    private File file;

    public FileEventLogger(String filename) {
        this.filename = filename;
    }

    public FileEventLogger() {
    }

    @PostConstruct
    public void init() throws IOException {
        this.file = new File(filename);
        if (file.exists() && !file.canWrite()) {
            throw new IllegalArgumentException("Can't write to file " + filename);
        } else if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                throw new IllegalArgumentException("Can't create file", e);
            }

        }
    }

    @Override
    public void logEvent(Event event) {
        try {
            FileUtils.writeStringToFile(file, event.toString(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Value("File logger")
    @Override
    protected void setName(String name) {
        this.name = name;
    }
}
