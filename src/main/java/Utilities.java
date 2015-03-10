import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        if(extension.equalsIgnoreCase("ad") || extension.equalsIgnoreCase("asciidoc") || extension.equalsIgnoreCase("adoc"))
            return true;
        return false;
    }

    public static void validateAsciiDocFile(File asciiDocFile) {
        if(!asciiDocFile.exists()) {
            String error = "AsciiDoc file does not exist: \"" + asciiDocFile.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
        if(!fileEndsWithValidAsciidocExtension(asciiDocFile.getName())) {
            String error = "Presumed AsciiDoc file does not have valid extension: \"" + asciiDocFile.getName() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static String getConcatPath(String[] filesOrDirectoriesToAppend) {
        String temp = filesOrDirectoriesToAppend[0];
        for(int i = 1; i < filesOrDirectoriesToAppend.length; i++) {
            if(!temp.endsWith("/")) {
                temp = temp.concat("/").concat(filesOrDirectoriesToAppend[i]);
            } else {
                temp = temp.concat(filesOrDirectoriesToAppend[i]);
            }
        }
        return temp;
    }

    public static boolean isStringNullOrEmptyOrWhitespace(String string) {
        if((string == null) || (string.isEmpty() || (StringUtils.isWhitespace(string)))) {
            return true;
        }
        return false;
    }

    public static String getOnlyContentDivFromHtml(String html) {
        Document doc = Jsoup.parse(html, "UTF-8");
        return doc.getElementById("content").html();
    }

    public static void validateCtorStringInputParam(String[] params, String className) {
        for(String param : params) {
            if (Utilities.isStringNullOrEmptyOrWhitespace(param)) {
                String error = "Constructor input parameter for " + className + " cannot be null, empty, or whitespace.";
                logger.fatal(error);
                throw new DocBuildException(error);
            }
        }
    }

    public static void validateCtorObjectsAreNotNull(Object[] params, String className) {
        for(Object obj : params) {
            if (obj == null) {
                String error = "Constructor input parameter for " + className + " cannot be null.";
                logger.fatal(error);
                throw new DocBuildException(error);
            }
            if(obj.getClass().getSimpleName().contentEquals("String")) {
                validateCtorStringInputParam(new String[] {(String)obj}, className);
            }
        }
    }

    public static String getRandomAlphaNumericString(int length) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for( int i = 0; i < length; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public static boolean isActiveUrlInSection(TocNode parentNode, String activeUrl, boolean isInSection) {
        if(parentNode.getParent() == null && parentNode.getUrl().contentEquals(activeUrl)) {
            return true;
        }
        if(isInSection) {
            return true;
        }
        if(activeUrl.isEmpty()) {
            return false;
        }
        for(TocNode node : parentNode.getChildren()) {
            if(node.getUrl().equalsIgnoreCase(activeUrl)) {
                isInSection = true;
            }
            else if(node.getChildren().size() > 0) {
                isInSection = isActiveUrlInSection(node, activeUrl, isInSection);
            }
        }
        return isInSection;
    }

    public static void validateIfActiveUrlIsInSection(TocNode root, String activeUrl) {
        if(root.getUrl().contentEquals(activeUrl)) {
            return;
        } else if (!isActiveUrlInSection(root, activeUrl, false)) {
            String error = "Active URL does not exist in nodes for TOC: \"" + activeUrl + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static void validateFileExists(File file) {
        if(!file.exists()) {
            String error = "File or directory does not exist: \"" + file.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static void validateIsDirectory(File directory) {
        validateFileExists(directory);
        if(!directory.isDirectory()) {
            String error = "File is not a directory as expected: \"" + directory.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static boolean directoryContainsAsciiDocFile(File directory) {
        boolean isValid = false;
        for(File file : directory.listFiles()) {
            if(Utilities.fileEndsWithValidAsciidocExtension(file.getName())) {
                isValid = true;
            }
        }
        return isValid;
    }

    public static void validateDirectoryContainsAsciiDocFile(File directory) {
        if(!directoryContainsAsciiDocFile(directory)) {
            String error = "Directory does not contain valid AsciiDoc file(s) as expected: \"" + directory.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static boolean directoryContainsTocFile(File directory) {
        boolean isValid = false;
        for(File file : directory.listFiles()) {
            if(file.getName().contentEquals("toc.ad")) {
                isValid = true;
            }
        }
        return isValid;
    }

    public static void validateDirectoryContainsTocFile(File directory) {
        if(!directoryContainsTocFile(directory)) {
            String error = "Directory does not contain a TOC file: \"" + directory.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static void validatePrettyNameExists(File directory) {
        boolean exists = false;
        for(File file : directory.listFiles()) {
            if(FilenameUtils.getExtension(file.getName()).contentEquals("version")) {
                exists = true;
            }
        }
        if(!exists) {
            String error = "Directory does not contain version file: \"" + directory.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }

    public static boolean directoryContainsVersions(File directory) {
        File versionDir = new File(Utilities.getConcatPath(new String[] {directory.getPath(), "v"}));
        if(!versionDir.exists()) {
            return false;
        }
        boolean containsDir = false;
        List<File> versionDirectories = new ArrayList<File>();
        for(File file : versionDir.listFiles()) {
            if(file.isDirectory()) {
                versionDirectories.add(file);
                containsDir = true;
            }
        }
        if(containsDir) {
            for(File file : versionDirectories) {
                if(!directoryContainsAsciiDocFile(file) || !directoryContainsTocFile(file)) {
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
        if(masterDirectory.isDirectory() && masterDirectory.exists()) {
            if(masterDirectory.listFiles().length > 0) {
                boolean containsDirectory = false;
                for(File file : masterDirectory.listFiles()) {
                    if(file.isDirectory()) {
                        containsDirectory = true;
                    }
                }
                if(!containsDirectory) {
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
        if(!FilenameUtils.getExtension(templateFile.getName()).contentEquals("template")) {
            String error = "Template file does not have valid '.template' extension: \"" + templateFile.getPath() + "\".";
            logger.fatal(error);
            throw new DocBuildException(error);
        }
    }


}
