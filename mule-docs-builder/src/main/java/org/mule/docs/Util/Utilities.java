package org.mule.docs.util;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.mule.docs.DocBuildException;
import org.mule.docs.TocNode;
import org.w3c.tidy.Tidy;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

/**
 * Created by sean.osterberg on 2/20/15.
 */
public class Utilities {
    static Logger logger = Logger.getLogger(Utilities.class);

    public static String getFileContentsFromFile(File file) {
        String contents = "";
        try {
            FileReader reader = new FileReader(file);
            contents = IOUtils.toString(reader);
        } catch (FileNotFoundException ffe) {
            String error = "The file \"" + file.getName() + "\" was not found.";
            logger.fatal(error, ffe);
            throw new DocBuildException(error);
        } catch (IOException ioe) {
            String error = "Cannot get file contents for \"" + file.getName() + "\".";
            logger.fatal(error, ioe);
            throw new DocBuildException(error);
        }
        return contents;
    }

    public static List<String> getFileContentsFromFiles(List<File> files) {
        List<String> fileContents = new ArrayList<String>();
        for (File file : files) {
            try {
                FileReader reader = new FileReader(file);
                fileContents.add(IOUtils.toString(reader));
            } catch (FileNotFoundException ffe) {
                String error = "The file \"" + file.getName() + "\" was not found.";
                logger.fatal(error, ffe);
                throw new DocBuildException(error);
            } catch (IOException ioe) {
                String error = "Cannot get file contents for \"" + file.getName() + "\".";
                logger.fatal(error, ioe);
                throw new DocBuildException(error);
            }
        }
        return fileContents;
    }

    public static boolean fileEndsWithValidAsciidocExtension(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        if (extension.equalsIgnoreCase("ad") || extension.equalsIgnoreCase("asciidoc") || extension.equalsIgnoreCase("adoc"))
            return true;
        return false;
    }

    public static void validateAsciiDocFile(File asciiDocFile) {
        if (!asciiDocFile.exists()) {
            String error = "AsciiDoc file does not exist: \"" + asciiDocFile.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
        if (!fileEndsWithValidAsciidocExtension(asciiDocFile.getName())) {
            String error = "Presumed AsciiDoc file does not have valid extension: \"" + asciiDocFile.getName() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static String getConcatPath(String[] filesOrDirectoriesToAppend) {
        String temp = filesOrDirectoriesToAppend[0];
        for (int i = 1; i < filesOrDirectoriesToAppend.length; i++) {
            if (!temp.isEmpty() && !temp.endsWith("/")) {
                temp = temp.concat("/").concat(filesOrDirectoriesToAppend[i]);
            } else {
                temp = temp.concat(filesOrDirectoriesToAppend[i]);
            }
        }
        return temp;
    }

    public static String removeLeadingSlashes(String s) {
        if (s.startsWith("/") && s.length() > 1) {
            s = s.substring(1);
        }
        return s;
    }

    public static StringBuilder replaceText(StringBuilder original, String toReplace, String replacement) {
        String modified = original.toString().replace(toReplace, replacement);
        return new StringBuilder(modified);
    }

    public static String getOnlyContentDivFromHtml(String html) {
        Document doc = Jsoup.parse(html, "UTF-8");
        return doc.getElementById("content").html();
    }

    public static void validateCtorStringInputParam(String[] params, String className) {
        for (String param : params) {
            if (StringUtils.isBlank(param)) {
                String error = "Constructor input parameter for " + className + " cannot be null, empty, or whitespace.";
                logger.fatal(error);
                throw new DocBuildException(error);
            }
        }
    }

    public static String cleanPageFileNames(String originalFilename) {
        if(originalFilename != null) {
            String result = originalFilename;
            try {
                result = java.net.URLDecoder.decode(originalFilename, "UTF-8");
            } catch(UnsupportedEncodingException e) {
                System.out.println("Couldn't decode filename: " + originalFilename + "\n" + e);
            }
            result = result.toLowerCase();
            result = StringUtils.replacePattern(result, "(_[0-9].)", "-");
            result = StringUtils.replacePattern(result, "(\\([0-9]\\))", "-");
            result = StringUtils.replacePattern(result, "([^\\w\\/\\.\\-])", "-");
            result = StringUtils.replacePattern(result, "(-)(-*)", "-");
            if(result.endsWith("-")) {
                result = result.substring(0, result.length() - 1);
            }
            return result;
        } else
            throw new NullPointerException("String references are null.");
    }

    public static void validateCtorObjectsAreNotNull(Object[] params, String className) {
        for (Object obj : params) {
            if (obj == null) {
                String error = "Constructor input parameter for " + className + " cannot be null.";
                logger.fatal(error);
                throw new DocBuildException(error);
            }
            if (obj.getClass().getSimpleName().equals("String")) {
                validateCtorStringInputParam(new String[] { (String) obj}, className);
            }
        }
    }

    public static String getRandomAlphaNumericString(int length) {
        StringBuffer buffer = new StringBuffer();
        while (buffer.length() < length) {
            buffer.append(uuidString());
        }
        //this part controls the length of the returned string
        return buffer.substring(0, length);
    }

    private static String uuidString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static boolean isActiveUrlInSection(TocNode parentNode, String activeUrl, boolean isInSection) {
        if (parentNode.getParent() == null && parentNode.getUrl().equals(activeUrl)) {
            return true;
        }
        if (isInSection) {
            return true;
        }
        if (activeUrl.isEmpty()) {
            return false;
        }
        for (TocNode node : parentNode.getChildren()) {
            if (node.getUrl().equalsIgnoreCase(activeUrl)) {
                isInSection = true;
            }
            else if (node.getChildren().size() > 0) {
                isInSection = isActiveUrlInSection(node, activeUrl, isInSection);
            }
        }
        return isInSection;
    }

    public static void validateIfActiveUrlIsInSection(TocNode root, String activeUrl) {
        if (root.getUrl().equals(activeUrl)) {
            return;
        } else if (!isActiveUrlInSection(root, activeUrl, false)) {
            String error = "Active URL does not exist in nodes for TOC: \"" + activeUrl + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static void validateFileExists(File file) {
        if (!file.exists()) {
            String error = "File or directory does not exist: \"" + file.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static void validateIsDirectory(File directory) {
        validateFileExists(directory);
        if (!directory.isDirectory()) {
            String error = "File is not a directory as expected: \"" + directory.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static boolean directoryContainsAsciiDocFile(File directory) {
        boolean isValid = false;
        for (File file : directory.listFiles()) {
            if (Utilities.fileEndsWithValidAsciidocExtension(file.getName())) {
                isValid = true;
            }
        }
        return isValid;
    }

    public static void validateDirectoryContainsAsciiDocFile(File directory) {
        if (!directoryContainsAsciiDocFile(directory)) {
            String error = "Directory does not contain valid AsciiDoc file(s) as expected: \"" + directory.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static boolean directoryContainsTocFile(File directory) {
        boolean isValid = false;
        for (File file : directory.listFiles()) {
            if (file.getName().equals("_toc.adoc")) {
                isValid = true;
            }
        }
        return isValid;
    }

    public static void validateDirectoryContainsTocFile(File directory) {
        if (!directoryContainsTocFile(directory)) {
            String error = "Directory does not contain a TOC file: \"" + directory.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static void validatePrettyNameExists(File directory) {
        boolean exists = false;
        for (File file : directory.listFiles()) {
            if (FilenameUtils.getExtension(file.getName()).equals("version")) {
                exists = true;
            }
        }
        if (!exists) {
            String error = "Directory does not contain version file: \"" + directory.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static boolean directoryContainsVersions(File directory) {
        File versionDir = new File(Utilities.getConcatPath(new String[] {directory.getPath(), "v"}));
        if (!versionDir.exists()) {
            return false;
        }
        boolean containsDir = false;
        List<File> versionDirectories = new ArrayList<File>();
        for (File file : versionDir.listFiles()) {
            if (file.isDirectory()) {
                versionDirectories.add(file);
                containsDir = true;
            }
        }
        if (containsDir) {
            for (File file : versionDirectories) {
                if (!directoryContainsAsciiDocFile(file) || !directoryContainsTocFile(file)) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    public static void validateMasterDirectory(File masterDirectory) {
        Utilities.validateIsDirectory(masterDirectory);
        if (masterDirectory.isDirectory() && masterDirectory.exists()) {
            if (masterDirectory.listFiles().length > 0) {
                boolean containsDirectory = false;
                for (File file : masterDirectory.listFiles()) {
                    if (file.isDirectory()) {
                        containsDirectory = true;
                    }
                }
                if (!containsDirectory) {
                    String error = "Master directory does not contain valid section directories: \"" + masterDirectory.getPath() + "\".";
                    logger.fatal(error);
                    throw new DocBuildException(error);
                }
            } else {
                String error = "Master directory does not contain files or directories: \"" + masterDirectory.getPath() + "\".";
                logger.fatal(error);
                throw new DocBuildException(error);
            }
        }
    }

    public static void validateTemplateFile(File templateFile) {
        validateFileExists(templateFile);
        if (!FilenameUtils.getExtension(templateFile.getName()).equals("template")) {
            String error = "org.mule.docs.Template file does not have valid '.template' extension: \"" + templateFile.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }


    public static boolean makeTargetDirectory(String directoryPath) {
        boolean result = false;
        try {
            File output = new File(directoryPath);
            result = output.mkdirs();
        } catch (SecurityException se) {
            String error = "Do not have permission to create directory \"" + directoryPath + "\"";
            logger.fatal(error, se);
            throw new DocBuildException(error);
        }
        return result;
    }

    public static boolean deleteTargetDirectory(String directoryName) {
        boolean result = false;
        try {
            File output = new File(directoryName);
            result = output.delete();
        } catch (SecurityException se) {
            String error = "Do not have permission to delete directory \"" + directoryName + "\"";
            logger.fatal(error, se);
            throw new DocBuildException(error);
        }
        return result;
    }

    public static void writeFileToDirectory(String filePath, String fileContents) {
        try {
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(fileContents);
            writer.close();
        } catch (IOException ioe) {
            String error = "Could not create file in directory: \"" + filePath + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static String fixIndexBaseName(String baseName) {
        if (baseName.equals("index")) {
            return "";
        }
        return baseName;
    }

    public static String tidyHtml(String html) {
        Tidy tidy = new Tidy();
        boolean xhtml = false;
        tidy.setXHTML(xhtml);
        InputStream htmlStream = new ByteArrayInputStream(html.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        org.w3c.dom.Document doc = tidy.parseDOM(htmlStream, outputStream);
        tidy.pprint(doc, outputStream);
        return outputStream.toString();
    }
}
