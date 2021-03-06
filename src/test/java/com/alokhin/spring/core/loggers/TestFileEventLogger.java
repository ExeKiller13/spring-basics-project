package com.alokhin.spring.core.loggers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alokhin.spring.core.beans.Event;

public class TestFileEventLogger {

    private File file;

    @Before
    public void createFile() throws IOException {
        this.file = File.createTempFile("test", "FileEventLogger");
    }

    @After
    public void removeFile() {
        file.delete();
    }

    @Test
    public void testInit() {
        FileEventLogger logger = new FileEventLogger(file.getAbsolutePath());
        try {
            logger.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitFail() {
        file.setReadOnly();
        FileEventLogger logger = new FileEventLogger(file.getAbsolutePath());
        try {
            logger.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogEvent() throws IOException {
        Event event = new Event(new Date(), DateFormat.getDateInstance());
        FileEventLogger logger = new FileEventLogger(file.getAbsolutePath());
        logger.init();

        String contents = FileUtils.readFileToString(this.file);
        assertTrue(contents.isEmpty());

        logger.logEvent(event);

        contents = FileUtils.readFileToString(this.file);
        assertFalse(contents.isEmpty());
    }

}
