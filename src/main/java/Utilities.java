import org.apache.commons.io.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.util.*;

/**
 * Created by sean.osterberg on 2/6/15.
 */
public class Utilities {
    static Logger logger = Logger.getLogger(Utilities.class);

    public static String getFileContentsFromFile(File file) {
        String contents = "";
        try {
            FileReader reader = new FileReader(file);
            contents = IOUtils.toString(reader);
        } catch (FileNotFoundException ffe) {
            logger.fatal("The file \"" + file.getName() + "\" was not found.", ffe);
        } catch (IOException ioe) {
            logger.fatal("Cannot get file contents for \"" + file.getName() + "\".");
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
                throw new DocBuilderException(error);
            } catch (IOException ioe) {
                String error = "Cannot get file contents for \"" + file.getName() + "\".";
                logger.fatal(error);
                throw new DocBuilderException(error);
            }
        }
        return fileContents;
    }

    public static boolean makeTargetDirectory(String directoryName) {
        boolean result = false;
        try {
            File output = new File(directoryName);
            result = output.mkdir();
        } catch (SecurityException se) {
            String error = "Do not have permission to create directory \"" + directoryName + "\"";
            logger.fatal(error, se);
            throw new DocBuilderException(error);
        }
        return result;
    }

    public static boolean makeTargetDirectory(File directory) {
        boolean result = false;
        try {
            result = directory.mkdir();

        } catch (SecurityException se) {
            String error = "Do not have permission to create directory \"" + directory.getPath() + "\"";
            logger.fatal(error, se);
            throw new DocBuilderException(error);
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
            throw new DocBuilderException(error);
        }
        return result;
    }

    public static boolean fileEndsWithDesiredExtension(String fileName, String[] extensions) {
        String extension = FilenameUtils.getExtension(fileName);
        boolean isValid = false;
        for (String ext : extensions) {
            if (extension.equalsIgnoreCase(ext)) {
                isValid = true;
            }
        }
        return isValid;
    }

    public static StringBuilder replaceText(StringBuilder original, String toReplace, String replacement) {
        String modified = original.toString().replace(toReplace, replacement);
        return new StringBuilder(modified);
    }

    public static String replaceFileExtension(String filename, String newExtension) {
        String temp;
        temp = FilenameUtils.removeExtension(filename);
        return temp += newExtension;
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
            throw new DocBuilderException(error);
        }
    }

    public static String getConcatenatedFilepath(String[] filesOrDirectoriesToAppend) {
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

    public static String getConcatenatedUrl(String[] pathsToAppend) {
        String temp = "";
        for(String file : pathsToAppend)
        {
            if(FilenameUtils.getExtension(file).isEmpty()) {
                temp = temp + file + "/";
            } else {
                temp = temp + file;
            }
        }
        return temp;
    }

    public static boolean fileEndsWithValidAsciidocExtension(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        if(extension.equalsIgnoreCase("ad") || extension.equalsIgnoreCase("asciidoc") || extension.equalsIgnoreCase("adoc"))
            return true;
        return false;
    }

    public static String getRandomAlphaNumericString(int length) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for( int i = 0; i < length; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    public static String getRawConvertedHtmlFromAsciidocFilepath(String filePath) {
        AsciidocSerializer serializer = new AsciidocSerializer();
        String htmlContent = serializer.getConvertedHtmlForSingleAsciidocFile(new File(filePath));
        return Jsoup.parse(htmlContent, "UTF-8").html();
    }

    public static String getRawConvertedHtmlFromAsciidocString(String asciiDoc) {
        AsciidocSerializer serializer = new AsciidocSerializer();
        String htmlContent = serializer.getConvertedHtmlForAsciidocString(asciiDoc);
        return Jsoup.parse(htmlContent, "UTF-8").html();
    }


    public static String getProcessedConvertedHtmlFromAsciidoc(String filePath) {
        AsciidocSerializer serializer = new AsciidocSerializer();
        String htmlContent = serializer.getConvertedHtmlForSingleAsciidocFile(new File(filePath));
        htmlContent = getOnlyContentDivFromHtml(htmlContent);
        return Jsoup.parse(htmlContent, "UTF-8").html();
    }

    public static Document getProcessedJsoupDocFromConvertedAsciiDocHtml(String filePath) {
        return Jsoup.parse(getProcessedConvertedHtmlFromAsciidoc(filePath), "UTF-8");
    }

    public static String getTitleFromHtml(String html) {
        Document doc = Jsoup.parse(html, "UTF-8");
        return doc.title();
    }

    public static String getOnlyContentDivFromHtml(String html) {
        Document doc = Jsoup.parse(html, "UTF-8");
        return doc.getElementById("content").html();
    }

    public static boolean isStringNullOrEmptyOrWhitespace(String string) {
        if((string == null) || (string.isEmpty() || (StringUtils.isWhitespace(string)))) {
            return true;
        }
        return false;
    }
}
