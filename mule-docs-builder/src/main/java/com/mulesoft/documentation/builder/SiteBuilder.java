package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.util.Utilities;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class SiteBuilder {
    private static Logger logger = Logger.getLogger(SiteBuilder.class);
    private List<Section> sections;
    private SiteTableOfContents toc;
    private File sourceDirectory;
    private File outputDirectory;
    private String gitHubRepoUrl;
    private String gitHubBranchName;
    private List<Template> templates;

    public SiteBuilder(File sourceDirectory, File outputDirectory, String gitHubRepoUrl, String gitHubBranchName) {
        this.sourceDirectory = sourceDirectory;
        this.outputDirectory = outputDirectory;
        this.gitHubRepoUrl = gitHubRepoUrl;
        this.gitHubBranchName = gitHubBranchName;
    }

    public void buildSite() {
        this.sections = getSections(this.sourceDirectory);
        this.toc = getSiteToc(sourceDirectory);
        this.templates = getTemplates(sourceDirectory);
        writeSections();
    }

    /**
     * Gets sections from the source directory.
     * @param sourceDirectory The directory from where to get the sections.
     * @return A list of sections.
     */
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

    /**
     *
     */
    private void writeSections() {
        for (Section section : this.sections) {
            String sectionPath = Utilities.getConcatPath(new String[]{this.outputDirectory.getPath(), Utilities.removeLeadingSlashes(section.getUrl())});
            logger.info("Started creating directory for \"" + section.getPrettyName() + "\" section: " + sectionPath + "...");
            Utilities.makeTargetDirectory(sectionPath);
            logger.info("Finished creating section directory.");
            logger.info("Started writing pages for section \"" + section + "\".");
            writePagesForSection(section);
            /*
            for (Section version : section.getVersions()) {
                String versionPath = Utilities.getConcatPath(new String[] {this.outputDirectory.getPath(), Utilities.removeLeadingSlashes(version.getUrl())});
                Utilities.makeTargetDirectory(versionPath);
                writePagesForSection(version);
            }*/
            logger.info("Finished writing pages for section \"" + section.getPrettyName() + "\".");
        }
    }

    private void 

    /**
     * There are multiple kinds of assets, including things like images, snippets, diagrams, etc.
     */
    private void writeAssets() {

    }

    private List<Template> getTemplates(File sourceDirectory) {
        List<Template> templates = new ArrayList<Template>();
        File templateDirectory = new File(Utilities.getConcatPath(new String[]{sourceDirectory.getPath(), "_templates"}));
        if (templateDirectory.isDirectory()) {
            File[] templateFiles = templateDirectory.listFiles();
            if (templateFiles != null) {
                for (File templateFile : templateFiles) {
                    if (FilenameUtils.getExtension(templateFile.getName()).equalsIgnoreCase("template")) {
                        templates.add(Template.fromFile(templateFile));
                    }
                }
            }
        }
        return templates;
    }

    private void writePagesForSection(Section section) {
        List<Page> pages = Page.forSection(section, this.sections, this.templates, this.toc, this.gitHubRepoUrl, this.gitHubBranchName);
        for (Page page : pages) {
            String pagePath = Utilities.getConcatPath(new String[] {this.outputDirectory.getPath(), Utilities.removeLeadingSlashes(section.getUrl()), page.getBaseName()}) + ".html";
            logger.info("Started writing page \"" + pagePath + "\"...");
            Utilities.writeFileToDirectory(pagePath, page.getContent());
            logger.info("Finished writing page.");
        }
    }


    private SiteTableOfContents getSiteToc(File masterDirectory) {
        File masterTocFile = new File(Utilities.getConcatPath(new String[] {masterDirectory.getPath(), "_toc.adoc"}));
        return SiteTableOfContents.fromAsciiDocFile(masterTocFile);
    }

    private boolean isValidSectionDirectory(File directory) {
        if (!directory.isDirectory() ||
                directory.isHidden() ||
                directory.getName().startsWith("_") ||
                directory.getName().equals(".DS_Store") ||
                directory.getName().equals("images")) {
            return false;
        }
        return true;
    }
}
