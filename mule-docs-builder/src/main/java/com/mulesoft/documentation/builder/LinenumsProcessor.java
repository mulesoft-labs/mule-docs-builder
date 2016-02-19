package com.mulesoft.documentation.builder;

import java.util.List;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.Preprocessor;
import org.asciidoctor.extension.PreprocessorReader;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/18/15.
 */
public class LinenumsProcessor extends Preprocessor {

    @Override
    public PreprocessorReader process(Document document, PreprocessorReader reader) {

        //System.out.println(document.getAttributes());

        List<String> everything = reader.readLines();

        while (everything.size() > 0) {

            String line = everything.remove(everything.size() - 1);

            if (line.trim().startsWith("[source,") && ! line.contains("linenums")) {
                // We probably have a language defined
                line = line.trim();
                line = line.substring(0, line.length() - 1) + ",linenums]";
            } else if (line.trim().startsWith("[source") && ! line.contains("linenums")) {
                line = line.trim();
                String sourceLang = (String) document.getAttributes().get("source-language");
                if(sourceLang != null) {
                    line = line.substring(0, line.length() - 1) +
                            "," + document.getAttributes().get("source-language") +
                            ",linenums]";
                } else {
                    line = line.substring(0, line.length() - 1) +
                            "," + "text,linenums]";
                }
            }
            reader.restoreLine(line);
        }
        return reader;
    }
}

