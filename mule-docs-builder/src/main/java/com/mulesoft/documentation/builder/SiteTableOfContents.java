package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.model.TocNode;
import com.mulesoft.documentation.builder.util.Utilities;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean.osterberg on 3/11/15.
 */
public class SiteTableOfContents {
    private List<TocNode> nodes;

    public SiteTableOfContents(List<TocNode> nodes) {
        this.nodes = nodes;
    }

    public static SiteTableOfContents fromAsciiDocFile(File asciiDocFile) {
        Utilities.validateAsciiDocFile(asciiDocFile);
        AsciiDocPage asciiDocPage = AsciiDocPage.fromFile(asciiDocFile);
        List<TocNode> nodes = getNodesFromToc(asciiDocPage.getHtml());
        return new SiteTableOfContents(nodes);
    }

    private static List<TocNode> getNodesFromToc(String html) {
        Document tocHtml = Jsoup.parse(html, "UTF-8");
        List<TocNode> nodes = new ArrayList<TocNode>();
        Elements tocSections = tocHtml.select("li");
        for (int i = 0; i < tocSections.size(); i++) {
            getNodeFromRawTocHtml(tocHtml, nodes);
        }
        return nodes;
    }

    private static void getNodeFromRawTocHtml(Document doc, List<TocNode> nodes) {
        Element item = doc.select("li").first();
        String link = item.select("a").first().attr("href");
        String title = item.select("a").first().text();
        TocNode firstNode = new TocNode(link, title, null);
        item.remove();
        nodes.add(firstNode);
    }

    public List<TocNode> getNodes() {
        return nodes;
    }
}
