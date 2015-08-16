package com.mulesoft.documentation.builder.previewer;

import com.mulesoft.documentation.builder.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/9/15.
 */
public class SinglePage {
    private static Logger logger = Logger.getLogger(SinglePage.class);

    public static void fromAsciiDocFile(String asciiDocFilePath, String outputDirPath) {
        System.out.println("Converting file to html: " + asciiDocFilePath);
        AsciiDocPage page = AsciiDocPage.fromFile(new File(asciiDocFilePath));
        String result = buildPage(page);
        File outputDir = new File(outputDirPath);
        if(!outputDir.exists()) {
            boolean success = outputDir.mkdir();
            if(!success) {
                System.out.println("\nError creating directory for output file: " + outputDirPath);
                System.exit(-1);
            }
        }
        String outputFilePath = Utilities.getConcatPath(new String[]{outputDirPath, page.getBaseName() + ".html"});
        Utilities.writeFileToDirectory(outputFilePath, result);
        System.out.println("Saved output file to: " + Utilities.getConcatPath(new String[] { outputDirPath, page.getBaseName() + ".html"}));
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
        Template template = null;

        try (InputStream contentStream = ClassLoader.class.getResourceAsStream("/preview.template")) {
            String content = IOUtils.toString(contentStream);
            template = Template.fromString(TemplateType.PREVIEW, content);
        } catch (IOException e) {
            throw new RuntimeException("Error reading contents of template.");
        }

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
