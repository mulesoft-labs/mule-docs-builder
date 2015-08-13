package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.util.Utilities;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/8/15.
 */
public class PdfGenerator {
    private static Logger logger = Logger.getLogger(PdfGenerator.class);
    private File outputDirectory;
    private Map<String, String> createdPdfRegistry;

    public PdfGenerator(File outputDirectory) {
        this.outputDirectory = outputDirectory;
        this.createdPdfRegistry = new HashMap<String, String>();
    }

    public void processAllSections(List<Section> sections) {
        for(Section section : sections) {
            processSection(section);
        }
    }

    public void processSection(Section section) {
        String sectionOutputPath = Utilities.getConcatPath(new String[]{
                this.outputDirectory.getPath(), Utilities.removeLeadingSlashes(section.getUrl())});
        try {
            File sectionOutputDir = new File(sectionOutputPath);
            FileUtils.forceMkdir(sectionOutputDir);
            for (AsciiDocPage page : section.getPages()) {
                createPdf(page, section, sectionOutputPath);
            }
        } catch(IOException e) {
            logger.error("Error creating new directory for section PDFs: " + sectionOutputPath, e);
        }
    }

    public void createPdf(AsciiDocPage page, Section section, String sectionOutputPath) {
        PdfAsciiDocProcessor processor = PdfAsciiDocProcessor.getProcessorInstance();
        String outputFilePath = Utilities.getConcatPath(new String[] { sectionOutputPath, getPdfFileName(page, section) });
        boolean successful = processor.convertAsciiDocStringToPdfFile(page.getAsciiDoc(), page.getFilePath(), outputFilePath);
        if(successful) {
            this.createdPdfRegistry.put(page.getFilePath(), outputFilePath);
        }
    }

    private String getPdfFileName(AsciiDocPage page, Section section) {
        return "mulesoft-docs--" + section.getBaseName() + "--" + page.getBaseName() + ".pdf";
    }

    public Map<String, String> getCreatedPdfRegistry() {
        return createdPdfRegistry;
    }
}
