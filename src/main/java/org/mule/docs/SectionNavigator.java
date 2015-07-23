package org.mule.docs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sean.osterberg on 5/28/15.
 */
public class SectionNavigator {

    public static String getHtmlForPage(AsciiDocPage page) {
        String html = page.getHtml();
        Map<String, String> sectionTitlesAndUrls = new LinkedHashMap<String, String>();
        Document doc = Jsoup.parse(html, "UTF-8");
        List<Element> elements = doc.select("h2");
        for(Element element : elements) {
            String title = element.text();
            String url = element.select("a").get(0).attr("href");
            if(url != null) {
                sectionTitlesAndUrls.put(url, title);
            }
        }
        return getHtml(sectionTitlesAndUrls);
    }

    public static String getHtml(Map<String, String> sectionTitlesAndUrls) {
        String output = "";
        if(sectionTitlesAndUrls.entrySet().size() > 0) {
            output = "<!-- scroll-menu -->" +
                    "<div class=\"col-md-2 scroll-menu-container\" data-swiftype-index='false'>" +
                    "<div class=\"scroll-menu\">\n" +
                    "                <h4>In this article:</h4>\n" +
                    "                <ul>";
            for (Map.Entry<String, String> entry : sectionTitlesAndUrls.entrySet()) {
                output += "<li><a class=\"scroll-menu-link\" href=\"" + entry.getKey() + "\">" + entry.getValue() + "</a></li>";
            }
            output += "</ul></div></div><!-- /scroll-menu -->";
        }
        return output;
    }


}
