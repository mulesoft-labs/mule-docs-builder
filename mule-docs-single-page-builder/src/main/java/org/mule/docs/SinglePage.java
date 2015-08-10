package org.mule.docs;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mule.docs.util.Utilities;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/9/15.
 */
public class SinglePage {
    private static Logger logger = Logger.getLogger(SinglePage.class);

    public static void fromAsciiDocFile(String asciiDocFilePath, String outputPath) {
        AsciiDocPage page = AsciiDocPage.fromFile(new File(asciiDocFilePath));
        String result = buildPage(page);
        logger.info("Built page from template for \"" + getPageTitle(page) + "\".");
        logger.info("Writing page to file...");
        File outputDir = new File(outputPath);
        if(!outputDir.exists()) {
            outputDir.mkdir();
        }
        String outputFilePath = Utilities.getConcatPath(new String[] { outputPath, page.getBaseName() + ".html" });
        Utilities.writeFileToDirectory(outputFilePath, result);
        logger.info("Saved file to path: " + outputPath);
    }

    private static String buildPage(AsciiDocPage page) {
        Template template = getTemplate();
        StringBuilder html = new StringBuilder(getTemplateContents(page, template));

        html = Utilities.replaceText(html, "{{ page.title }}", getPageTitle(page));
        html = Utilities.replaceText(html, "{{ page.content }}", getContentHtml(page));
        html = Utilities.replaceText(html, "{{ page.sections }}", getSectionNavigator(page));
        return html.toString();
    }

    private static String getPageTitle(AsciiDocPage page) {
        return page.getTitle();
    }

    private static String getTemplateContents(AsciiDocPage page, Template template) {
         return template.getContents();
    }

    private static String getSectionNavigator(AsciiDocPage page) {
        return SectionNavigator.getHtmlForPage(page);
    }

    private static String getContentHtml(AsciiDocPage page) {
        Document doc = Jsoup.parse(page.getHtml(), "UTF-8");
        return Utilities.getOnlyContentDivFromHtml(doc.html());
    }

    private static Template getTemplate() {
        File templateFile = new File(Utilities.getConcatPath(new String[] { getResourcesPath(), "preview.template" }));
        Template template = Template.fromFile(templateFile);
        return template;
    }

    private static String getResourcesPath() {
        URL pathToResource = SinglePage.class.getClassLoader().getResource("");
        String resourcesPath = "";
        if(pathToResource != null) {
            resourcesPath = pathToResource.getFile();
            if (!StringUtils.isEmpty(resourcesPath)) {
                return resourcesPath;
            }
        }
        throw new RuntimeException("Resources path was null.");
    }
}
