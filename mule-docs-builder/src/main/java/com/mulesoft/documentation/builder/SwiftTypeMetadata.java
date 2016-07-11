package com.mulesoft.documentation.builder;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sean.osterberg on 7/5/15.
 */
public class SwiftTypeMetadata {
    private static Logger logger = LoggerFactory.getLogger(SwiftTypeMetadata.class);

    public static String fromAsciiDocPage(AsciiDocPage page) {
        return getMetadataEntries(page);
    }

    public static String getMetadataEntries(AsciiDocPage page) {
        logger.debug("Creating SwiftType metadata for page \"" + page.getTitle() + "\"...");
        String result = "";
        result += getTitleMetadata(page);
        // result += getBodyMetadata(page);
        return result;
    }

    public static String getTitleMetadata(AsciiDocPage page) {
        String result = "<meta class=\"swiftype\" name=\"title\" data-type=\"string\" content=";
        result += "\"" + page.getTitle().trim() + "\" />\n";
        return result;
    }

    public static String getBodyMetadata(AsciiDocPage page) {
        String bodyText = getDescription(page);
        if (bodyText.length() > 0) {
            String result = "    <meta class=\"swiftype\" name=\"body\" data-type=\"text\" content=";
            result += "\"" + bodyText + "\" />\n";
            return result;
        }
        return "";
    }

    public static String getDescription(AsciiDocPage page) {
        String result = "";
        Document doc = Jsoup.parse(page.getHtml(), "UTF-8");
        List<Element> paragraphs = doc.getElementsByClass("paragraph");
        if (paragraphs != null && paragraphs.size() > 0) {
            result = getShortenedDescription(paragraphs);
        }
        return result;
    }

    private static String getShortenedDescription(List<Element> paragraphs) {
        String result = "";
        for (Element para : paragraphs) {
            result += Jsoup.clean(para.html(), Whitelist.none());
            result += " "; // Space so that paragraphs don't but against each other.
            if (result.length() >= 147) {
                break;
            }
        }
        int max = result.length();
        if (max < 147) { // Max length of 150 for meta description tag, so 157 with ellipses.
            result = result.substring(0, max);
        } else {
            result = result.substring(0, 147);
        }
        result = formatEndOfDescriptionTag(result);
        result = StringEscapeUtils.unescapeHtml4(result);
        return result;
    }

    private static String formatEndOfDescriptionTag(String text) {
        if (text.endsWith(".")) {
            text += "..";
        } else if (text.endsWith(",") || text.endsWith(";") || text.endsWith(":") || text.endsWith("'") || text.endsWith("\"") || text.endsWith("-") || text.endsWith("--")) {
            int length = text.length();
            text = text.substring(0, length - 2) + "...";
        } else {
            text += "...";
        }

        return text;
    }

}
