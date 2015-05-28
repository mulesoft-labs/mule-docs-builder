package org.mule.docs.processor;

/**
 * Created by MuleSoft.
 */
public interface IAsciiDocProcessor {
    /**
     * Process HTML and returns the modified AsciiDoc
     *
     * @param asciiDoc Input AsciiDoc
     * @return Modified AsciiDoc
     */
    String process(String asciiDoc);
}
