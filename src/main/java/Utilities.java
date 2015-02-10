import org.apache.commons.io.*;
import org.apache.log4j.Logger;

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
        for(File file : files) {
            try {
            FileReader reader = new FileReader(file);
            fileContents.add(IOUtils.toString(reader));
            } catch (FileNotFoundException ffe) {
                logger.fatal("The file \"" + file.getName() + "\" was not found.", ffe);
            } catch (IOException ioe) {
                logger.fatal("Cannot get file contents for \"" + file.getName() + "\".");
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
            logger.fatal("Do not have permission to create directory \"" + directoryName + "\"", se);
        }
        return result;
    }

    public static boolean deleteTargetDirectory(String directoryName) {
        boolean result = false;
        try {
            File output = new File(directoryName);
            result = output.delete();

        } catch (SecurityException se) {
            logger.fatal("Do not have permission to delete directory \"" + directoryName + "\"", se);
        }
        return result;
    }

    public static boolean fileEndsWithDesiredExtension(String fileName, String[] extensions) {
        String extension = FilenameUtils.getExtension(fileName);
        boolean isValid = false;
        for(String ext : extensions) {
            if(extension.equalsIgnoreCase(ext)) {
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

    public static void writeFileToDirectory(String filePath, String fileContents) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
        } catch(IOException ioe) {
            String error = "Could not create file in directory: \"" + filePath + "\".";
            logger.fatal(error);
            throw new IOException();
        }
        writer.write(fileContents);
        writer.close();
    }
}
