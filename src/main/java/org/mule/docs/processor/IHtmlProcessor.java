package org.mule.docs.processor;

/**
 * Created by Mulesoft.
 */
public interface IHtmlProcessor {
    /**
     * Process HTML and returns the modified HTML
     *
     * @param html Input Html
     * @return Modified Html
     */
    String process(String html, AsciiDocProcessor processor);
}
