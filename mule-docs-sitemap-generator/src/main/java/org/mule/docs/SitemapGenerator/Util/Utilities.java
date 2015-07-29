package org.mule.docs.sitemapgenerator.Util;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 7/25/2015.
 */

public class Utilities {
    static Logger logger = Logger.getLogger(Utilities.class);

    public static void validateFileExists(File file) {
        if(!file.exists()) {
            String error = "File or directory does not exist: \"" + file.getPath() + "\".";
            logger.fatal(error);
            throw new RuntimeException(error);
        }
    }

    public static void validateIsDirectory(File directory) {
        validateFileExists(directory);
        if(!directory.isDirectory()) {
            String error = "File is not a directory as expected: \"" + directory.getPath() + "\".";
            logger.fatal(error);
            throw new RuntimeException(error);
        }
    }

    public static String getConcatPath(String[] filesOrDirectoriesToAppend) {
        String temp = filesOrDirectoriesToAppend[0];
        for(int i = 1; i < filesOrDirectoriesToAppend.length; i++) {
            if(!temp.isEmpty() && !temp.endsWith("/")) {
                temp = temp.concat("/").concat(filesOrDirectoriesToAppend[i]);
            } else {
                temp = temp.concat(filesOrDirectoriesToAppend[i]);
            }
        }
        return temp;
    }

}
