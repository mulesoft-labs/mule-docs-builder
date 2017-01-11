package com.mulesoft.documentation.builder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class SwiftypeMetadata {
    private static Logger logger = LoggerFactory.getLogger(SwiftypeMetadata.class);

    public static String fromAsciiDocPage(Section section, AsciiDocPage page) {
        return getMetadataEntries(section, page);
    }

    public static String getMetadataEntries(Section section, AsciiDocPage page) {
        logger.debug("Creating Swiftype metadata for page \"" + page.getTitle() + "\"...");
        String result = getTitleMetadata(page);
        String versionMetadata = getVersionMetadata(section);
        String pageKeywords = getPageKeywords(page);
        if (!versionMetadata.isEmpty()) {
            result += "\n  " + versionMetadata;
        }
        if (!pageKeywords.isEmpty()) {
            result += "\n  " + pageKeywords;
        }
        return result;
    }

    public static String getTitleMetadata(AsciiDocPage page) {
        return "<meta class=\"swiftype\" name=\"title\" data-type=\"string\"" +
            " content=\"" + page.getTitle().trim() + "\" />";
    }

    public static String getVersionMetadata(Section section) {
        if (section.isSiteRoot() || section.isVersionless()) {
            return "";
        }
        
        return "<meta class=\"swiftype\" name=\"version\" data-type=\"enum\"" +
            " content=\"" + section.getVersionPrettyName() + "\" />";
    }
    
    public static String getPageKeywords(AsciiDocPage page) {
        Pattern p = Pattern.compile(":keywords:\\s+(.*)");
 		Matcher m = p.matcher(page.getAsciiDoc());
        boolean b = m.find();
        if (!b) {
            return "";
        }
        
        return "<meta class=\"swiftype\" name=\"keywords\" data-type=\"string\"" +
            " content=\"" + m.group(1) + "\" />";
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
