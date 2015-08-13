package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.util.Utilities;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 8/7/15.
 */
public class PdfGeneratorTest {

    @Test
    public void createPdf_WithValidPageAndSection_CreatesPageWithValidRegistry() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
        Section section = getValidSection();
        String sectionFilePath = Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "pdf-output", "cloudhub"});
        PdfGenerator pdfGenerator = new PdfGenerator(new File(""));
        pdfGenerator.createPdf(page, section, sectionFilePath);
        String createdFileName = "mulesoft-docs--" + section.getBaseName() + "--" + page.getBaseName() + ".pdf";
        String createdPath = Utilities.getConcatPath(new String[] { sectionFilePath, createdFileName });
        Map<String, String> createdPdfRegistry = pdfGenerator.getCreatedPdfRegistry();
        String originalFile = Utilities.getConcatPath(new String[] { TestUtilities.getPathToMasterFolder(), "cloudhub", "index.adoc" });
        String createdPdf = Utilities.getConcatPath(new String[] { sectionFilePath, createdFileName });

        assertEquals(createdPdf, createdPdfRegistry.get(originalFile));
        assertTrue(new File(createdPath).exists());
    }

    @Test
    public void processSection_WithValidPagesAndSection_CreatesPagesWithValidRegistry() {
        Section section = getValidSection();
        String pdfFilePath = Utilities.getConcatPath(new String[] { TestUtilities.getTestResourcesPath(), "pdf-output", });
        PdfGenerator pdfGenerator = new PdfGenerator(new File(pdfFilePath));
        pdfGenerator.processSection(section);
        File createdDirectory = new File(Utilities.getConcatPath(new String[] { pdfFilePath, section.getUrl() }));
        File[] files = createdDirectory.listFiles();
        Map<String, String> createdPdfRegistry = pdfGenerator.getCreatedPdfRegistry();

        String originalFile = Utilities.getConcatPath(new String[] { TestUtilities.getPathToMasterFolder(), "cloudhub", "index.adoc" });
        String createdPdf = Utilities.getConcatPath(new String[] { pdfFilePath, "cloudhub", "mulesoft-docs--cloudhub--index.pdf"});

        assertEquals(createdPdf, createdPdfRegistry.get(originalFile));
        assertEquals(4, files.length); // 2x the number because of the .pdfmarks files
    }

    @Test
    public void processAllSections_WithValidSections_CreatesPages() {
        Section section = getValidSection();
        Section section2 = Section.fromDirectory(new File(Utilities.getConcatPath(new String[] { TestUtilities.getPathToMasterFolder(), "anypoint-platform" })));
        List<Section> sections = new ArrayList<Section>();
        sections.add(section);
        sections.add(section2);


        String pdfFilePath = Utilities.getConcatPath(new String[] { TestUtilities.getTestResourcesPath(), "pdf-output", });
        PdfGenerator pdfGenerator = new PdfGenerator(new File(pdfFilePath));
        pdfGenerator.processAllSections(sections);

        File createdDirectory = new File(Utilities.getConcatPath(new String[] { pdfFilePath, section.getUrl() }));
        File createdDirectory2 = new File(Utilities.getConcatPath(new String[] { pdfFilePath, section2.getUrl() }));

        File[] files = createdDirectory.listFiles();
        assertEquals(4, files.length); // 2x the number because of the .pdfmarks files
        File[] files2 = createdDirectory2.listFiles();
        assertEquals(4, files2.length); // 2x the number because of the .pdfmarks files
    }

    private File getValidFile() {
        String path = Utilities.getConcatPath(new String[] { TestUtilities.getPathToMasterFolder(), "cloudhub", "index.adoc" });
        return new File(path);
    }

    public Section getValidSection() {
        File validDirectory = new File(Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub"}));
        return Section.fromDirectory(validDirectory);
    }
}
