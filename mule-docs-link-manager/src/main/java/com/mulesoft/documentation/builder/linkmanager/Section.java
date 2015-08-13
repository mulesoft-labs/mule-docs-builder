package com.mulesoft.documentation.builder.linkmanager;

import com.mulesoft.documentation.builder.AsciiDocPage;
import com.mulesoft.documentation.builder.TocNode;
import com.mulesoft.documentation.builder.util.Utilities;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sean.osterberg on 2/22/15.
 */

public class Section {
    private static Logger logger = Logger.getLogger(Section.class);
    private List<AsciiDocPage> pages;
    private TocNode rootNode;
    private String filepath;
    private String url;
    private String prettyName;
    private String versionPrettyName;
    private String baseName;
    private List<Section> versions;

    public Section(List<AsciiDocPage> pages,
                   TocNode rootNode,
                   List<Section> versions,
                   String filepath,
                   String url,
                   String prettyName,
                   String versionPrettyName) {
        validateInputParams(new Object[] {pages, rootNode, versions, filepath});
        this.pages = pages;
        this.rootNode = rootNode;
        this.versions = versions;
        this.filepath = filepath;
        this.url = url;
        this.prettyName = prettyName;
        this.baseName = getBaseName(filepath);
        this.versionPrettyName = versionPrettyName;
        logger.debug("Created Section for directory \"" + filepath + "\".");
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

    public List<Section> getVersions() {
        return versions;
    }

    public static Section fromDirectory(File directory) {
        validateDirectory(directory);
        return getSectionFromDirectory(directory, "");
    }

    public static List<Section> fromMasterDirectory(File masterDirectory) {
        Utilities.validateMasterDirectory(masterDirectory);
        List<Section> sections = new ArrayList<Section>();
        if (masterDirectory.isDirectory()) {
            for (File directory : masterDirectory.listFiles()) {
                if (directory.isDirectory() && !directory.getName().startsWith("_") && !directory.getName().startsWith(".")) {
                    sections.add(fromDirectory(directory));
                }
            }
        }
        return sections;
    }

    private static Section getSectionFromDirectory(File directory, String url) {
        List<File> validFiles = getValidAsciiDocFilesInSection(new ArrayList<File>(Arrays.asList(directory.listFiles())));
        List<AsciiDocPage> pages = AsciiDocPage.fromFiles(validFiles);
        //File tocFile = new File(Utilities.getConcatPath(new String[]{directory.getPath(), "_toc.adoc"}));
        //TocNode rootNode = SectionTableOfContents.fromAsciiDocFile(tocFile).getRootTocNode();
        List<Section> versions = new ArrayList<Section>();
        if (!url.contains(File.separator + "v" + File.separator)) {
            if (url.isEmpty() && directory.getPath().contains(File.separator + "v" + File.separator)) {
                url = getVersionUrl(directory.getPath(), getBaseName(directory.getPath()));
            } else {
                url = Utilities.getConcatPath(new String[]{url, directory.getPath().substring(directory.getPath().lastIndexOf(File.separator) + 1)});
            }
        }
        if (Utilities.directoryContainsVersions(directory)) {
            getVersions(directory, versions, url);
        }
        return new Section(pages, new TocNode("foobar", "foobar", null), versions, directory.getPath(), url, getPrettyName(directory), getVersionPrettyName(directory));
    }

    private static List<File> getValidAsciiDocFilesInSection(List<File> files) {
        List<File> validFiles = new ArrayList<File>();
        for (File file : files) {
            if (!file.isDirectory() &&
                    Utilities.fileEndsWithValidAsciidocExtension(file.getName()) &&
                    !file.getName().equals("_toc.adoc") &&
                    !file.isHidden()) {
                validFiles.add(file);
            }
        }
        return validFiles;
    }

    private static void getVersions(File directory, List<Section> versions, String url) {
        File versionDirectory = new File(Utilities.getConcatPath(new String[] {directory.getPath(), "v"}));
        for (File file : versionDirectory.listFiles()) {
            if (file.isDirectory()) {
                versions.add(getSectionFromDirectory(file, getVersionUrl(file.getPath(), url)));
            }
        }
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

    private static String getPrettyName(File directory) {
        for (File file : directory.listFiles()) {
            if (FilenameUtils.getExtension(file.getName()).equals("section")) {
                String baseName = FilenameUtils.getBaseName(file.getName());
                int length = baseName.length();
                return baseName.substring(1, length); // Need to remove the _ underscore in front of the name
            }
        }
        return null;
    }

    private static String getVersionPrettyName(File directory) {
        for (File file : directory.listFiles()) {
            if (FilenameUtils.getExtension(file.getName()).equals("version")) {
                String baseName = FilenameUtils.getBaseName(file.getName());
                int length = baseName.length();
                return baseName.substring(1, length); // Need to remove the _ underscore in front of the version
            }
        }
        return null;
    }

    private static String getBaseName(String directoryPath) {
        if (!directoryPath.contains(File.separator + "v" + File.separator)) {
            return directoryPath.substring(directoryPath.lastIndexOf(File.separator) + 1);
        } else {
            List<String> paths = getDirectoriesOrFilesBetweenSeparators(directoryPath);
            String[] result = paths.toArray(new String[paths.size()]);
            String baseName = "";
            for (int i = 0; i < result.length; i++) {
                if (result[i].equals("v")) {
                    baseName = new StringBuilder(result[i + 1]).reverse().toString();
                }
            }
            return baseName;
        }
    }

    private static void validateDirectory(File directory) {
        //Utilities.validateIsDirectory(directory);
        //Utilities.validateDirectoryContainsAsciiDocFile(directory);
        //Utilities.validateDirectoryContainsTocFile(directory);
        //Utilities.validatePrettyNameExists(directory);
    }

    private static void validateInputParams(Object[] params) {
        Utilities.validateCtorObjectsAreNotNull(params, Section.class.getSimpleName());
    }
}
