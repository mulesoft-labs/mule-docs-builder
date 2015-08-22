package com.mulesoft.documentation.builder.linkmanager;

import com.mulesoft.documentation.builder.*;
import com.mulesoft.documentation.builder.model.SectionConfiguration;
import com.mulesoft.documentation.builder.model.SectionVersion;
import com.mulesoft.documentation.builder.util.Utilities;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 7/28/15.
 */
public class LinkFile {
    private File masterDirectory;
    public static Process _process;

    public LinkFile(String masterDirectory) {
        Utilities.validateIsDirectory(new File(masterDirectory));
        this.masterDirectory = new File(masterDirectory);
    }

    // Creates a manifest of every Asciidoc file in the master path like shown:
    //      "mule-fundamentals/v/3.6/foobar-file"
    //public List<String> createManifestOfAsciidocFilesInMasterDirectory() {

    //}

    public void appendUniqueAsciidocPathExcludingMasterDirectoryPath(Map<String, String> asciidocPathAndFilename,
                                                                     File parentDir,
                                                                     String masterPathToExclude) {
        File[] files = parentDir.listFiles();
        if(files != null) {
            for(File f : files) {
                if(f.isDirectory()) {
                    appendUniqueAsciidocPathExcludingMasterDirectoryPath(asciidocPathAndFilename, f, masterPathToExclude);
                } else {
                    if(Utilities.fileEndsWithValidAsciidocExtension(f.getName())) {
                        // /User/user.name/mule-fundamentals/v/3.6/anypoint-exchange.adoc --> mule-fundamentals/v/3.6/anypoint-exchange.adoc
                        String pathToAdd = f.getAbsolutePath().replace(masterPathToExclude, "");
                        String extension = FilenameUtils.getExtension(pathToAdd);

                        // mule-fundamentals/v/3.6/anypoint-exchange.adoc --> mule-fundamentals/v/3.6/anypoint-exchange
                        pathToAdd = pathToAdd.substring(0, extension.length() - 1);

                        // anypoint-exchange
                        String fileNameToAdd = FilenameUtils.getBaseName(pathToAdd);

                        asciidocPathAndFilename.put(pathToAdd, fileNameToAdd);
                    }
                }
            }
        }
    }

    public void createFile(String path) {
        String output = getCsvPagesInSections();
        Utilities.writeFileToDirectory(path, output);
        File outputFile = new File(path);
        if(outputFile.exists()) {
            System.out.println("Successfully created link file.");
        }
    }

    public String getCsvPagesInSections() {
        List<PageAttribute> pages = new ArrayList<PageAttribute>();
        File[] sectionDirs = this.masterDirectory.listFiles();
        if(sectionDirs != null) {
            for(File sectionDir : sectionDirs) {
                File vDir = new File(Utilities.getConcatPath(new String[] { sectionDir.getAbsolutePath(), "v" }));
                if(vDir.exists()) {
                    File[] versionSections = vDir.listFiles();
                    if (versionSections != null) {
                        for(File versionSectionDir : versionSections) {
                            if(isValidSectionDirectory(versionSectionDir)) {
                                SectionVersion sectionVersion = getVersionFromSectionAndConfiguration(versionSectionDir);
                                File[] files = versionSectionDir.listFiles();
                                if (files != null) {
                                    for(File f : files) {
                                        if(Utilities.fileEndsWithValidAsciidocExtension(f.getName()) && !f.getName().equals("_toc.adoc")) {
                                            PageAttribute page = new PageAttribute(
                                                    getTopicTitleFromFile(f),
                                                    sectionVersion.getSectionPrettyName(),
                                                    sectionVersion.getVersionName(),
                                                    sectionVersion.getSectionBaseName(),
                                                    FilenameUtils.getBaseName(f.getName()),
                                                    f.getAbsolutePath()
                                            );
                                            pages.add(page);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (PageAttribute page : pages) {
            sb.append(createCsvLine(page));
        }
        return sb.toString();
    }

    private String getTopicTitleFromFile(File asciiDocFile) {
        String contents = Utilities.getFileContentsFromFile(asciiDocFile);
        Pattern p = Pattern.compile("=\\s*(.*)");
        Matcher m = p.matcher(contents);
        String title = "";
        while (m.find()) {
            title = m.group(1);
            break;
        }
        return title;
    }

    /**
     * Gets the version and section information from the version directory and its configuration file.
     * @param versionDirectory The valid version directory to process.
     * @return A Version object with information about the version.
     */
    private SectionVersion getVersionFromSectionAndConfiguration(File versionDirectory) {
        SectionConfiguration config = getConfigurationForSection(versionDirectory);
        File vDir = versionDirectory.getParentFile();
        String sectionDirectoryBaseName = vDir.getParentFile().getName();
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
                    if (file.getName().equals("_config.yml")) {
                        InputStream input = null;
                        try {
                            input = new FileInputStream(file);
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
            Object version = configMap.get("versionName");
            String versionName = version.toString();
            Object latest = configMap.get("isLatest");
            boolean isLatest = (boolean) latest;
            String sectionName = configMap.get("sectionName");
            return new SectionConfiguration(sectionName, versionName, isLatest);
        } else {
            throw new DocBuildException("Couldn't parse _config.yml beacuse Yaml Map was null.");
        }
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

    private String createCsvLine(PageAttribute page) {
        String temp = page.getTopicTitle().replace(",", "") + ","; // replace spaces if there are any so it doesn't screw up processing
        temp += page.getPrettySectionName() + ",";
        temp += page.getVersionName() + ",";
        temp += page.getTopicBaseName() + ".adoc,";

        String finalTopicUrl = page.getTopicBaseName();

        // This case is because we don't want to expose a version for those sections that don't actually have a real version
        if(!StringUtils.containsIgnoreCase(page.getVersionName(), "latest")) {
            temp += "/" + page.getSectionBaseName() + "/v/" + page.getVersionName() + "/" + finalTopicUrl + "\n";
        } else {
            temp += "/" + page.getSectionBaseName() + "/" + finalTopicUrl + "\n";
        }
        return temp;
    }

}
