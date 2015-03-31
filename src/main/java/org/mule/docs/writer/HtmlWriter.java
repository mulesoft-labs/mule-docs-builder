package org.mule.docs.writer;

import freemarker.cache.*;
import freemarker.template.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.mule.docs.model.*;
import org.mule.docs.utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mulesoft.
 */
public class HtmlWriter {

    Configuration configuration;
    private static HtmlWriter intance;

    public static HtmlWriter getIntance() {
        if (intance == null) {
            intance = new HtmlWriter();
        }
        return intance;
    }

    private HtmlWriter() {
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        configuration = new Configuration();
        //FileTemplateLoader fileTemplate = new FileTemplateLoader(new File("/tmp/templates"));

        stringLoader.putTemplate("breadcrumbLink", "<li><a href=\"${url}\">${title}</a></li>");
        stringLoader.putTemplate("activeBreadcrumbLink", "<li class=\"active\">${title}</li>");
        stringLoader.putTemplate("childTocLink", "<a href=\"${url}\"><li class=\"child\">${title}</li></a>");
        stringLoader.putTemplate("activeChildTocLink", "<a href=\"${url}\"><li class=\"child active\">${title}</li></a>");
        stringLoader.putTemplate("parentTocLink1",
                "<li class=\"toc-section\"><div class=\"toc-section-header child\"><a href=\"${url}\">${title}</a><a href=\"#${sectionId}\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div><ul class=\"collapsed child-section collapse in\" id=\"${sectionId}\">");
        stringLoader.putTemplate("parentTocLink2",
                "<li class=\"toc-section\"><div class=\"toc-section-header active\"><a href=\"${url}\" style=\"color: white\">${title}</a><a href=\"#${sectionId}\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div><ul class=\"collapsed child-section collapse in\" id=\"${sectionId}\">");
        stringLoader.putTemplate("parentTocLink3",
                "<li class=\"toc-section\"><div class=\"toc-section-header child\"><a href=\"${url}\">${title}</a><a href=\"#${sectionId}\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div><ul class=\"collapsed child-section collapse in\" id=\"${sectionId}\" style=\"height: 0px;\">");
        stringLoader.putTemplate("parentTocLink4",
                "<li class=\"toc-section\"><div class=\"toc-section-header child\"><a href=\"${url}\">${title}</a><a href=\"#${sectionId}\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div><ul class=\"collapsed child-section collapse in\" id=\"${sectionId}\">");
        stringLoader.putTemplate("parentTocLink5",
                "<li class=\"toc-section\"><div class=\"toc-section-header child\"><a href=\"${url}\">${title}</a><a href=\"#${sectionId}\" data-toggle=\"collapse\" class=\"collapsed\"><div class=\"toc-section-header-arrow\"></div></a></div><ul class=\"collapsed child-section collapse\" id=\"${sectionId}\" style=\"height: 0px;\">");
        TemplateLoader[] loaders = new TemplateLoader[] { stringLoader,
                //fileTemplate,
                new ClassTemplateLoader(getClass(), "/") };
        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateLoader(mtl);
    }

    public String getBreadCrumb(List<String> links) {
        Map<String, Object> fruitsMap = new HashMap<String, Object>();
        fruitsMap.put("links", links);

        return processTemplate(fruitsMap, "template/breadcrumb.ftl");
    }

    public String getChildTocLink(String activeUrl, String baseUrl, TocNode child) {
        Map<String, Object> childValues = new HashMap<String, Object>();
        childValues.put("title", child.getTitle());
        childValues.put("url", getCompleteUrl(baseUrl, child.getUrl()));
        String templateName = "childTocLink";
        if (activeUrl != null && activeUrl.equalsIgnoreCase(child.getUrl())) {
            templateName = "activeChildTocLink";
        }

        return processTemplate(childValues, templateName);
    }

    public String getParentTocLink(String activeUrl, String baseUrl, TocNode parent, boolean isFirstItem) {
        String sectionId = Utilities.getRandomAlphaNumericString(10);
        Map<String, Object> childValues = new HashMap<String, Object>();
        childValues.put("title", parent.getTitle());
        childValues.put("sectionId", sectionId);
        childValues.put("url", getCompleteUrl(baseUrl, parent.getUrl()));
        String templateName = "";
        if (activeUrl == null) {
            templateName = "parentTocLink1";
        } else if (parent.getUrl().equalsIgnoreCase(activeUrl)) {
            templateName = "parentTocLink2";
        } else if (isFirstItem && !activeUrl.isEmpty()) {
            templateName = "parentTocLink3";
        } else if (Utilities.isActiveUrlInSection(parent, activeUrl, false)) {
            templateName = "parentTocLink4";
        } else {
            templateName = "parentTocLink5";
        }
        return processTemplate(childValues, templateName);
    }

    private String processTemplate(Map<String, Object> childValues, String templateName) {
        try {
            freemarker.template.Template breadcrumb = configuration.getTemplate(templateName);
            StringWriter writer = new StringWriter();
            breadcrumb.process(childValues, writer);
            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException("Unexpected error", e);
        } catch (TemplateException e) {
            throw new RuntimeException("Unexpected error", e);
        }
    }

    public String getBreadCrumbLink(TocNode node, String activeUrl, String baseUrl) {
        Map<String, Object> fruitsMap = new HashMap<String, Object>();
        fruitsMap.put("title", node.getTitle());
        String templateName = "activeBreadcrumbLink";
        if (!node.getUrl().contentEquals(activeUrl)) {
            String url = Utilities.getConcatPath( baseUrl, node.getUrl() );
            fruitsMap.put("url", url);
            templateName = "breadcrumbLink";
        }
        return processTemplate(fruitsMap, templateName);
    }

    public String getPageContent(Section section, List<Section> sections, SiteTableOfContents toc, AsciiDocPage page) {
        Map<String, Object> pageSections = new HashMap<String, Object>();
        pageSections.put("title", page.getTitle());
        pageSections.put("toc", getPageToc(toc, sections, section, page));
        pageSections.put("breadcrumb", getBreadcrumbHtml(section, page));
        pageSections.put("content", getContentHtml(page));
        pageSections.put("version", getVersionHtml(section, page));
        return processTemplate(pageSections, "template/default.ftl");
    }

    private static String getPageToc(SiteTableOfContents toc, List<Section> sections, Section section, AsciiDocPage page) {
        SiteTocHtml tocHtml = SiteTocHtml.fromSiteTocAndSections(toc, sections);
        return tocHtml.getTocHtmlForSectionAndPage(section, page);
    }

    private static String getBreadcrumbHtml(Section section, AsciiDocPage page) {
        Breadcrumb breadcrumb = Breadcrumb.fromRootNode(section.getRootNode());
        return breadcrumb.getHtmlForActiveUrl(page.getBaseName(), "/docs/" + section.getUrl());
    }

    private static String getContentHtml(AsciiDocPage page) {
        Document doc = Jsoup.parse(page.getHtml(), "UTF-8");
        return Utilities.getOnlyContentDivFromHtml(doc.html());
    }

    private static String getVersionHtml(Section section, AsciiDocPage page) {
        VersionSelector version = VersionSelector.fromSection(section);
        return version.htmlForPage(page);
    }

    private static String getCompleteUrl(String baseUrl, String url) {
        return Utilities.getConcatPath( baseUrl, url );
    }
}
