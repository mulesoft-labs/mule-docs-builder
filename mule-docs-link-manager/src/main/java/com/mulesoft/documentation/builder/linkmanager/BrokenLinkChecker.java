package com.mulesoft.documentation.builder.linkmanager;

import com.mulesoft.documentation.builder.AsciiDocPage;
import com.mulesoft.documentation.builder.Section;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 7/29/15.
 */
public class BrokenLinkChecker {



    public List<Link> getLinksFromPage(Section section, AsciiDocPage page) {
        Pattern p = Pattern.compile("(\\s*link:)(.*?[^\\[])(\\[.*?\\])");
        Matcher m = p.matcher(page.getAsciiDoc());
        List<Link> links = new ArrayList<Link>();
        int[] indexes = getLineStartIndexes(page.getAsciiDoc());
        while (m.find()) {
            int lineNumber = getLineNumberFromIndex(indexes, m.start());
            String linkDeclarationAndSpaces = m.group(1);   // "link:"
            String linkUrl = m.group(2);                    // "cloudhub/cloudhub-overview"
            String linkText = m.group(3).substring(1, m.group(3).length() - 1);        // "CloudHub Overview", without brackets
            Link link = new Link(
                    section.getPrettyName(),
                    section.getBaseName(),
                    page.getBaseName(),
                    page.getTitle(),
                    linkUrl,
                    linkText
                    );
            m.start();
            links.add(link);
        }
        return links;
    }

    private int getLineNumberFromIndex(int[] indexes, int current) {
        int previous = indexes[0];
        for(int i = 0; i < indexes.length; i++) {
            if(current <= indexes[i]) {
                return previous;
            } else {
                previous = indexes[i];
            }
        }
        return 0; // Failed
    }

    private int[] getLineStartIndexes(String asciidocContent) {
        int[] indexOfNewline = new int[]{};
        int count = 0;
        for (int pos = asciidocContent.indexOf("\n"); pos != -1; pos = asciidocContent.indexOf("\n", pos + 1)) {
            indexOfNewline[count] = pos;
            count++;
        }
        return indexOfNewline;
    }
/*
    public List<String> getLinkTextFromPage(String asciidocContent) {

    }

    */
}
