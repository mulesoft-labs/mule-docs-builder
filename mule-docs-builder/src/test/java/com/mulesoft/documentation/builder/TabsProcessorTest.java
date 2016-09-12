package com.mulesoft.documentation.builder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class TabsProcessorTest {

    @Test
    public void testTabsProcessor() throws URISyntaxException {
        AsciiDocProcessor processor = AsciiDocProcessor.getProcessorInstance();
        String result = processor.convertAsciiDocString(readFixture("html-processors/tabs.adoc"));
        assertThat(result, containsString("<li class=\"active\"><a href=\"#tab-1_1\" role=\"tab\" data-toggle=\"tab\">Studio <em>Visual</em> Editor</a></li>"));
        assertThat(result, containsString("<li><a href=\"#tab-1_2\" role=\"tab\" data-toggle=\"tab\">XML Editor</a></li>"));
        assertThat(result, containsString("<li class=\"active\"><a href=\"#tab-2_1\" role=\"tab\" data-toggle=\"tab\">First Tab</a></li>"));
    }

    public String readFixture(String filename) {
        try {
            URL resource = getClass().getClassLoader().getResource(filename);
            if (resource != null) {
                return new String(Files.readAllBytes(Paths.get(resource.toURI())));
            }
            else {
                throw new RuntimeException("Could not find fixture path: " + filename);
            }
        }
        catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Could not read fixtured path: " + filename, e);
        }
    }
}
