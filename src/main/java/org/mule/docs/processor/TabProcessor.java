package org.mule.docs.processor;

import org.mule.docs.utils.Utilities;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sean.osterberg on 5/8/15.
 */
public class TabProcessor implements IAsciiDocProcessor{

    public String process(String asciiDoc) {
        return replaceTabsBlock(asciiDoc);
    }

    public static String replaceTabsBlock(String text) {
        //Pattern p = Pattern.compile("(\\[tabs\\]\\n------\\n)((.|\\n)*?)(\\n------\\n)");
        Pattern p = Pattern.compile("(\\[tabs\\]\\n------\\n)((.+|\\n+)*?)(\\n------\\n)");
        Matcher m = p.matcher(text);
        StringBuffer buffer = new StringBuffer();
        while (m.find()) {
            // replace old url with filename
            String replacement =
                "<div class=\"panel panel-default no-padding\">\n" +
                        "    <div class=\"panel-heading no-padding\">\n" +
                        "        <!-- Nav tabs -->\n" +
                        "        <ul class=\"nav nav-tabs\" role=\"tablist\">\n";

            Map<String, String> tabNames = getTabOrderAndTitles(m.group(2));
            List<Map.Entry<String,String>> randAccess = new ArrayList<Map.Entry<String,String>>(tabNames.entrySet());
            for(int i = 0; i < tabNames.size(); i++) {
                if(i == 0) {
                    replacement +=
                        "<li class=\"active\"><a href=\"" + randAccess.get(0).getValue() +
                        "\" role=\"tab\" data-toggle=\"tab\">" +
                        randAccess.get(0).getKey() + "</a></li>\n";
                } else {
                    replacement +=
                    "<li><a href=\"" + randAccess.get(i).getValue() +
                    "\" role=\"tab\" data-toggle=\"tab\">" + randAccess.get(i).getKey() + "</a></li>\n";
                }
            }
            replacement +=
                        "        </ul>\n" +
                        "    </div>" +
                        "<!-- Tab panes + Panel body -->\n" +
                        "    <div class=\"panel-body tab-content no-padding\">" +
                replaceSingleTabBlock(m.group(2), randAccess) +
                        "</div>\n" +
                        "</div>";
            m.appendReplacement(buffer, replacement);
        }
        m.appendTail(buffer);
        return buffer.toString();
    }

    public static String replaceSingleTabBlock(String text, List<Map.Entry<String,String>> randAccess) {
        Pattern p = Pattern.compile("(^\\[tab,\\s*title=\")(.*)(\"\\]\\n\\.\\.\\.\\.\\n)((.|\\n)*?)(\\n\\.\\.\\.\\.\\n)", Pattern.MULTILINE);
        Matcher m = p.matcher(text);
        StringBuffer buffer = new StringBuffer();
        int count = 0;
        while (m.find()) {
            String replacement = "<div class=\"tab-pane in active fade no-padding\" id=\"";
            replacement += randAccess.get(count).getValue() + "\">" + "</div>";
            m.appendReplacement(buffer, replacement);
        }
        m.appendTail(buffer);
        return buffer.toString();
    }

    public static Map<String, String> getTabOrderAndTitles(String text) {
        Pattern p = Pattern.compile("(^\\[tab,\\s*title=\")(.*)(\"\\]\\n\\.\\.\\.\\.\\n)((.|\\n)*?)(\\n\\.\\.\\.\\.\\n)", Pattern.MULTILINE);
        Matcher m = p.matcher(text);
        Map<String, String> tabNameAndId = new LinkedHashMap<String, String>();
        while (m.find()) {
            tabNameAndId.put(m.group(2), Utilities.getRandomAlphaNumericString(5));
        }
        return tabNameAndId;
    }
}
