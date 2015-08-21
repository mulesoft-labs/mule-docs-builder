package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.model.SectionConfiguration;
import com.mulesoft.documentation.builder.model.SectionVersion;
import com.mulesoft.documentation.builder.util.Utilities;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

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
    private List<SectionVersion> allSectionVersions;

    public SiteBuilder(File sourceDirectory, File outputDirectory, String gitHubRepoUrl, String gitHubBranchName) {
        this.sourceDirectory = sourceDirectory;
        this.outputDirectory = outputDirectory;
        this.gitHubRepoUrl = gitHubRepoUrl;
        this.gitHubBranchName = gitHubBranchName;
        this.sections = new ArrayList<Section>();
    }

    public void buildSite() {
        getSectionsAndVersions();
        this.toc = getSiteToc(sourceDirectory);
        this.templates = getTemplates(sourceDirectory);
        writeSections();
    }

    /**
     * Gets sections from the source directory.
     * @return A list of sections.
     */
    public void getSectionsAndVersions() {
        List<SectionVersion> versions = new ArrayList<SectionVersion>();
        if (this.sourceDirectory.isDirectory()) {
            File[] files = this.sourceDirectory.listFiles();
            if(files != null) {
                for (File parentSectionDir : files) {
                    if (parentSectionDir.isDirectory()) {
                        getVersionSections(parentSectionDir, versions);
                    }
                }
            } else {
                throw new DocBuildException("Source directory for sections was null.");
            }
        }
        this.allSectionVersions = versions;
    }

    // Give it a list of sections and the section's version directory, e.g. mule-fundamentals/v/

    /**
     * With the provided section version path (../mule-fundamentals/v/..), add sections and versions to instance collection.
     * @param sectionVersionPath The version path for the section where the individual version folders are located.
     * @param versions A list of SectionVersions that will be appended to.
     */
    private void getVersionSections(File sectionVersionPath, List<SectionVersion> versions) {
        if(sectionVersionPath.isDirectory() && sectionVersionPath.getName().equals("v")) {
            File[] versionSectionDirectories = sectionVersionPath.listFiles();
            if(versionSectionDirectories != null) {
                for (File versionDirectory : versionSectionDirectories) {
                    if(isValidSectionDirectory(versionDirectory)) {
                        SectionVersion sectionVersion = getVersionFromSectionAndConfiguration(versionDirectory);
                        versions.add(sectionVersion);
                        Section section = Section.fromDirectoryAndSectionVersion(versionDirectory, sectionVersion);
                        this.sections.add(section);
                    }
                }
            } else {
                throw new DocBuildException("FAILED processing section version path because path was null: " + sectionVersionPath.getAbsolutePath());
            }
        }
    }


    /**
     * Gets the version and section information from the version directory and its configuration file.
     * @param versionDirectory The valid version directory to process.
     * @return A Version object with information about the version.
     */
    private SectionVersion getVersionFromSectionAndConfiguration(File versionDirectory) {
        SectionConfiguration config = getConfigurationForSection(versionDirectory);
        String sectionDirectoryBaseName = versionDirectory.getParentFile().getName();
        String versionUrl = "/" + sectionDirectoryBaseName + "/v/";

        return new SectionVersion(config.getSectionName(), sectionDirectoryBaseName, versionUrl, config.getVersionName(), config.isLatest());
    }

    /**
     * Gets a SectionConfiguration object that describes the specified directory's name, version, and whether or not it's the latest version.
     * @param sectionDirectory The section directory to process.
     * @return The section configuration information.
     */
    private SectionConfiguration getConfigurationForSection(File sectionDirectory) {
        if (sectionDirectory.isDirectory()) {
            File[] filesInSection = sectionDirectory.listFiles();
            if (filesInSection != null) {
                for (File file : filesInSection) {
                    // Get the configuration .yml file and process it
                    if (file.getName().equals("_config") && FilenameUtils.getExtension(file.getName()).equals("yml")) {
                        InputStream input = null;
                        try {
                            input = new FileInputStream("_config.yml");
                        } catch (FileNotFoundException e) {
                            throw new DocBuildException("Couldn't process _config.yml file for section: " + sectionDirectory.getAbsolutePath());
                        }
                        return getConfigurationFromInputYaml(input);
                    }
                }
            }
        }
        throw new DocBuildException("Section didn't have a _config.yml file: " + sectionDirectory);
    }

    /**
     * Creates a SectionConfiguration object from the InputStream that represents a .yml file.
     * @param input InputStream that represents a .yml file
     * @return The section configuration information.
     */
    @SuppressWarnings("unchecked")
    private SectionConfiguration getConfigurationFromInputYaml(InputStream input) {
        Yaml yaml = new Yaml();
        LinkedHashMap<String, Map<String, String>> map = (LinkedHashMap) yaml.load(input);
        if (map != null) {
            Map<String, String> configMap = map.get("configuration");
            String versionName = configMap.get("versionName");
            String latest = configMap.get("isLatest");
            boolean isLatest = false;
            if(latest.equals("true")) {
                isLatest = true;
            }
            String sectionName = configMap.get("sectionName");
            return new SectionConfiguration(sectionName, versionName, isLatest);
        } else {
            throw new DocBuildException("Couldn't parse _config.yml beacuse Yaml Map was null.");
        }
    }

    /**
     * Writes all of the section's pages to the output directory specified in the instance's outputDirectory parameter.
     */
    private void writeSections() {
        for (Section section : this.sections) {
            String sectionPath = Utilities.getConcatPath(new String[]{this.outputDirectory.getPath(), Utilities.removeLeadingSlashes(section.getUrl())});
            logger.info("Started creating directory for \"" + section.getPrettyName() + "\" section: " + sectionPath + "...");
            Utilities.makeTargetDirectory(sectionPath);
            logger.info("Finished creating section directory.");
            logger.info("Started writing pages for section \"" + section + "\".");
            writePagesForSection(section);
            logger.info("Finished writing pages for section \"" + section.getPrettyName() + "\".");
        }
    }

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

    public List<SectionVersion> getSectionVersionCollectionForSectionBaseName(String sectionBaseName) {
        List<SectionVersion> versionsForSection = new ArrayList<SectionVersion>();
        for(SectionVersion version : this.allSectionVersions) {
            if(version.getSectionBaseName().equals(sectionBaseName)) {
                versionsForSection.add(version);
            }
        }
        return versionsForSection;
    }

    private void writePagesForSection(Section section) {
        List<SectionVersion> sectionVersions = getSectionVersionCollectionForSectionBaseName(section.getBaseName());
        List<Page> pages = Page.forSection(section, this.sections, this.templates, this.toc, this.gitHubRepoUrl, this.gitHubBranchName, sectionVersions);
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
                directory.getName().equals("_images")) {
            return false;
        }
        return true;
    }
}
