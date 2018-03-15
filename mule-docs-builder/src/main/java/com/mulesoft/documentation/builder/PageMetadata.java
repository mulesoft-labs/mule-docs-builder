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
        return getBodyMetadata(page);
    }

    public static String getBodyMetadata(AsciiDocPage page) {
        String bodyText = SwiftypeMetadata.getDescription(page);
        return bodyText.isEmpty() ? "" : "<meta name=\"description\" content=\"" + bodyText + "\" />";

    }

    public static String setNoIndex(AsciiDocPage page) {
        if (page.containsAttribute("noindex")) {
            return "<meta name=\"robots\" content=\"noindex\" />";
        }
            return "";
    }
}
