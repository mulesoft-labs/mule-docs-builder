package org.mule.docs.writer;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.*;

import org.mule.docs.model.*;
import org.mule.docs.utils.Utilities;
import org.w3c.tidy.*;
import org.w3c.dom.*;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class SiteBuilder {

    private List<Section> sections;
    private SiteTableOfContents toc;
    private File sourceDirectory;
    private File outputDirectory;
    private List<Template> templates;

    public SiteBuilder(File sourceDirectory, File outputDirectory) {
        this.sourceDirectory = sourceDirectory;
        this.outputDirectory = outputDirectory;
        this.sections = getSections(sourceDirectory);
        this.toc = getSiteToc(sourceDirectory);
        this.templates = getTemplates(sourceDirectory);
        writeSections();
    }

    public static void buildSite(File sourceDirectory, File outputDirectory) {
        SiteBuilder builder = new SiteBuilder(sourceDirectory, outputDirectory);
    }

    public List<Section> getSections(File sourceDirectory) {
        List<Section> sections = new ArrayList<Section>();
        if (sourceDirectory.isDirectory()) {
            for (File file : sourceDirectory.listFiles()) {
                if (isValidSectionDirectory(file)) {
                    Section section = Section.fromDirectory(file);
                    sections.add(section);
                }
            }
        }
        return sections;
    }

    private void writeSections() {
        for (Section section : sections) {
            String sectionPath = Utilities.getConcatPath(new String[] { outputDirectory.getPath(), Utilities.removeLeadingSlashes(section.getUrl()) });
            Utilities.makeTargetDirectory(sectionPath);
            writePagesForSection(section);
            for (Section version : section.getVersions()) {
                String versionPath = Utilities.getConcatPath(new String[] { outputDirectory.getPath(), Utilities.removeLeadingSlashes(version.getUrl()) });
                Utilities.makeTargetDirectory(versionPath);
                writePagesForSection(version);
            }
        }
    }

    private List<Template> getTemplates(File sourceDirectory) {
        List<Template> templates = new ArrayList<Template>();
        File templateDirectory = new File(Utilities.getConcatPath(new String[] { sourceDirectory.getPath(), "_templates" }));
        if (templateDirectory.isDirectory()) {
            for (File templateFile : templateDirectory.listFiles()) {
                if (FilenameUtils.getExtension(templateFile.getName()).equalsIgnoreCase("template")) {
                    templates.add(Template.fromFile(templateFile));
                }
            }
        }
        return templates;
    }

    private void writePagesForSection(Section section) {
        List<Page> pages = Page.forSection(section, this.sections, this.templates, this.toc);
        for (Page page : pages) {
            String pagePath =
                    Utilities.getConcatPath(new String[] { this.outputDirectory.getPath(), Utilities.removeLeadingSlashes(section.getUrl()), page.getBaseName() }) + ".html";
            Utilities.writeFileToDirectory(pagePath, page.getContent());
        }
    }

    private String tidyHtml(String html) {
        Tidy tidy = new Tidy();
        boolean xhtml = false;
        tidy.setXHTML(xhtml);
        InputStream htmlStream = new ByteArrayInputStream(html.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document doc = tidy.parseDOM(htmlStream, outputStream);
        tidy.pprint(doc, outputStream);
        return outputStream.toString();
    }

    private SiteTableOfContents getSiteToc(File masterDirectory) {
        File masterTocFile = new File(Utilities.getConcatPath(new String[] { masterDirectory.getPath(), "toc.ad" }));
        return SiteTableOfContents.fromAsciiDocFile(masterTocFile);
    }

    private boolean isValidSectionDirectory(File directory) {
        if ((!directory.isDirectory()) || directory.isHidden() ||
                directory.getName().startsWith("_")) {
            return false;
        }
        return true;
    }
}
