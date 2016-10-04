package com.mulesoft.documentation.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mulesoft.documentation.builder.model.SectionVersion;
import com.mulesoft.documentation.builder.model.TocNode;
import com.mulesoft.documentation.builder.util.Utilities;

/**
 * Created by sean.osterberg on 2/22/15.
 */

public class Section {
    private static Logger logger = LoggerFactory.getLogger(Section.class);
    private List<AsciiDocPage> pages;
    private TocNode rootNode;
    private String filepath;
    private String url;
    private String prettyName;
    private String versionPrettyName;
    private String baseName;
    private SectionVersion sectionVersion;

    public Section(List<AsciiDocPage> pages,
                   TocNode rootNode,
                   String filepath,
                   String url,
                   String prettyName,
                   String versionPrettyName,
                   String baseName,
                   SectionVersion sectionVersion
                    ) {
        //validateInputParams(new Object[] {pages, rootNode, filepath});
        this.pages = pages;
        this.rootNode = rootNode;
        this.filepath = filepath;
        this.url = url;
        this.prettyName = prettyName;
        this.baseName = baseName;
        this.versionPrettyName = versionPrettyName;
        this.sectionVersion = sectionVersion;
    }

    public List<AsciiDocPage> getPages() {
        return pages;
    }

    public TocNode getRootNode() {
        return rootNode;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getUrl() {
        return url;
    }

    public String getPrettyName() {
        return prettyName;
    }

    public String getVersionPrettyName() {
        return versionPrettyName;
    }

    public String getBaseName() {
        return baseName;
    }

    public boolean containsPageMatching(AsciiDocPage page) {
        // in this case, the section has the same version as the page
        if (getPages().contains(page)) {
            return true;
        }
        else {
            String baseName = page.getBaseName();
            for (AsciiDocPage candidate : getPages()) {
                if (candidate.getBaseName().equals(baseName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isRoot() {
        return baseName.isEmpty();
    }

    public SectionVersion getSectionVersion() {
        return sectionVersion;
    }

    public static Section fromDirectoryAndSectionVersion(File directory, SectionVersion sectionVersion) {
        validateDirectory(directory);
        return getSectionFromDirectory(directory, sectionVersion, "");
    }

    private static Section getSectionFromDirectory(File directory, SectionVersion sectionVersion, String url) {
        File[] filesInDirectory = directory.listFiles();
        if(filesInDirectory != null) {
            logger.info(String.format("Processing section located at [%s].",directory.getAbsolutePath()));
            List<File> validFiles = getValidAsciiDocFilesInSection(new ArrayList<File>(Arrays.asList(filesInDirectory)));
            List<AsciiDocPage> pages = AsciiDocPage.fromFiles(validFiles);
            File tocFile = new File(Utilities.getConcatPath(new String[] { directory.getPath(), "_toc.adoc" }));
            TocNode rootNode = SectionTableOfContents.fromAsciiDocFile(tocFile).getRootTocNode();
            url = Utilities.getConcatPath(new String[] { url, Utilities.removeLeadingSlashes(sectionVersion.getVersionUrl()) });

            return new Section(pages,
                    rootNode,
                    directory.getPath(),
                    url,
                    sectionVersion.getSectionPrettyName(),
                    sectionVersion.getVersionName(),
                    sectionVersion.getSectionBaseName(),
                    sectionVersion
                    );
        } else {
            throw new DocBuildException("Section directory was empty: " + directory);
        }
    }

    private static List<File> getValidAsciiDocFilesInSection(List<File> files) {
        List<File> validFiles = new ArrayList<File>();
        for (File file : files) {
            if (!file.isDirectory() &&
                    Utilities.fileEndsWithValidAsciidocExtension(file.getName()) &&
                    !file.getName().equals("_toc.adoc")) {
                validFiles.add(file);
            }
        }
        return validFiles;
    }

    public static String getVersionUrl(String directoryPath, String sectionName) {
        List<String> pathEntries = getDirectoriesOrFilesBetweenSeparators(directoryPath);
        String buffer = "";
        for (String entry : pathEntries) {
            String temp = new StringBuilder(entry).reverse().toString();
            if (!temp.equals(sectionName)) {
                buffer += entry + File.separator;
            } else {
                return new StringBuilder(buffer + entry).reverse().toString();
            }
        }
        return sectionName;
    }

    // Results are reversed so that you can easily reverse the entire generated path later
    private static List<String> getDirectoriesOrFilesBetweenSeparators(String directoryPath) {
        List<String> fileOrDirectories = new ArrayList<String>();
        String buffer = "";
        for (int i = directoryPath.length() - 1; i > 0; i--) {
            char current = directoryPath.charAt(i);
            if (current != File.separatorChar) {
                buffer += current;
            } else {
                if (!buffer.isEmpty()) {
                    fileOrDirectories.add(buffer);
                    buffer = "";
                }
            }
        }
        return fileOrDirectories;
    }

    private static void validateDirectory(File directory) {
        Utilities.validateIsDirectory(directory);
        Utilities.validateDirectoryContainsAsciiDocFile(directory);
        Utilities.validateDirectoryContainsTocFile(directory);
    }
}
