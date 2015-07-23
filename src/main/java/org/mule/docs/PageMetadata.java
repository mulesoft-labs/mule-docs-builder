package org.mule.docs;

import org.apache.log4j.Logger;

/**
 * Created by sean.osterberg on 7/6/15.
 */
public class PageMetadata {

    private static Logger logger = Logger.getLogger(PageMetadata.class);

    public static String fromAsciiDocPage(AsciiDocPage page) {
        return getMetadataEntries(page);
    }

    public static String getMetadataEntries(AsciiDocPage page) {
        logger.info("Creating metadata for page \"" + page.getTitle() + "\"...");
        String result = "";
        result += getBodyMetadata(page);
        logger.info("Finished creating metadata for page.");
        return result;
    }

    public static String getBodyMetadata(AsciiDocPage page) {
        String bodyText = SwiftTypeMetadata.getDescription(page);
        if(bodyText.length() > 0) {
            String result = "<meta name=\"description\" content=";
            result += "\"" + bodyText + "\" />";
            return result;
        }
        return "";
    }

}
