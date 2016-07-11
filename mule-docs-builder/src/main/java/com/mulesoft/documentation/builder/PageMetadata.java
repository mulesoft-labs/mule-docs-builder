package com.mulesoft.documentation.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sean.osterberg on 7/6/15.
 */
public class PageMetadata {

    private static Logger logger = LoggerFactory.getLogger(PageMetadata.class);

    public static String fromAsciiDocPage(AsciiDocPage page) {
        return getMetadataEntries(page);
    }

    public static String getMetadataEntries(AsciiDocPage page) {
        logger.debug("Creating metadata for page \"" + page.getTitle() + "\"...");
        String result = "";
        result += getBodyMetadata(page);
        return result;
    }

    public static String getBodyMetadata(AsciiDocPage page) {
        String bodyText = SwiftTypeMetadata.getDescription(page);
        if (bodyText.length() > 0) {
            String result = "<meta name=\"description\" content=";
            result += "\"" + bodyText + "\" />";
            return result;
        }
        return "";
    }

}
