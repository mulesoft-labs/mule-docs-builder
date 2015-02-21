import java.io.*;
import java.util.*;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 * Created by sean.osterberg on 1/9/15.
 */
public class SiteBuilder {
    private static Logger logger = Logger.getLogger(SiteBuilder.class);
    private String sourceDirectory;
    private String outputDirectory;
    private String baseUrl;
    private List<PageTemplate> pageTemplates;
    public static String templateFilePath = "/Users/sean.osterberg/mulesoft-docs/_site/_templates/default.html";

    public static void main(String[] args) {
        String tempSourceDirectory = "/Users/sean.osterberg/mulesoft-docs/_source";
        String tempOutputDirectory = "/Users/sean.osterberg/mulesoft-docs/_site/";
        SiteBuilder builder = new SiteBuilder(tempSourceDirectory, tempOutputDirectory, "");
        builder.buildDocSite();
    }

    public SiteBuilder(String sourceDirectory, String outputDirectory, String baseUrl) {
        this.sourceDirectory = sourceDirectory;
        this.outputDirectory = outputDirectory;
        this.baseUrl = baseUrl;
        this.pageTemplates = initializePageTemplates();
    }

    public void buildDocSite() {
        List<File> childDirectories = getDirectoriesInSourceDirectory();
        SectionBuilder sectionBuilder = new SectionBuilder(this.pageTemplates, this.baseUrl);
        List<DocSection> docSections = sectionBuilder.getDocSectionsFromListOfDirectories(childDirectories);
        updateAllVersionsOfPagesWithCompleteToc(docSections);
        setFinalHtmlContentForAllSectionsAndVersions(docSections);
        writeDocsToDirectory(docSections);
    }

    public void getTocOrderForRootSections() {

    }

    //---- Final write operations ----//

    public void writeDocsToDirectory(List<DocSection> docSections) {
        for(DocSection section : docSections) {
            File outputDirectory = new File(Utilities.getConcatenatedFilepath(
                    new String[] {this.outputDirectory, section.getSectionPathFromBaseUrl()}));
            Utilities.makeTargetDirectory(outputDirectory);
            for(DocPage page : section.getPagesInSection()) {
                writePageToDirectory(page, outputDirectory);
                logger.info("Wrote page to directory: \"" + page.getOutputFilename() + "\" --> " + outputDirectory);
            }
            if(section.getOtherVersions().size() > 0) {
                writeDocsToDirectory(section.getOtherVersions());
            }
        }
    }

    public void writePageToDirectory(DocPage page, File directory) {
        Utilities.writeFileToDirectory(Utilities.getConcatenatedFilepath(
                new String[] { directory.getPath(), page.getOutputFilename() }), page.getInitialContentHtml());
    }

    public List<File> getDirectoriesInSourceDirectory() {
        File sourceDirectory = new File(this.sourceDirectory);
        validateChildDirectoriesInSourceDirectory(sourceDirectory);
        List<File> childDirectories = new ArrayList<File>();
        for(File directory : sourceDirectory.listFiles()) {
            if(directory.isDirectory()) {
                validateSourceDirectory(directory);
                childDirectories.add(directory);
            }
        }
        return childDirectories;
    }

    //---- Initialize page templates ----//

    private List<PageTemplate> initializePageTemplates() {
        String templateDirectory = Utilities.getConcatenatedFilepath(new String[] {this.sourceDirectory, "_templates"});
        validateTemplatesAreInDirectory(templateDirectory);
        List<PageTemplate> templates = new ArrayList<PageTemplate>();
        for(File templateFile : new File(templateDirectory).listFiles()) {
           templates.add(initializePageTemplate(templateFile));
        }
        return templates;
    }

    private PageTemplate initializePageTemplate(File templateFile){
        PageTemplate template = new PageTemplate();
        template.setTemplateContents(Utilities.getFileContentsFromFile(templateFile));
        template.setTemplateFilepath(templateFile.getPath());
        for(PageTemplateType type : PageTemplateType.values()) {
            String typeName = type.toString();
            String baseName = FilenameUtils.getBaseName(templateFile.getName());
            if(typeName.equalsIgnoreCase(baseName)) {
                template.setTemplateType(type);
            }
        }
        return template;
    }

    public List<PageTemplate> getPageTemplates() {
        return this.pageTemplates;
    }

    //---- Set final HTML ----//

    public void setFinalHtmlContentForAllSectionsAndVersions(List<DocSection> sections) {
        for(DocSection section : sections) {
            setFinalHtmlContentForPagesInSection(section);
            if(section.getOtherVersions().size() > 0) {
                for(DocSection oldSection : section.getOtherVersions()) {
                    setFinalHtmlContentForPagesInSection(oldSection);
                }
            }
        }
    }

    public void setFinalHtmlContentForPagesInSection(DocSection section) {
        for(DocPage page : section.getPagesInSection()) {
            PageBuilder builder = new PageBuilder(this.pageTemplates, this.baseUrl);
            if((page.getFinalContentHtml() == null) || (page.getFinalContentHtml().isEmpty())) {
                page.setFinalContentHtml(builder.getCompletePageContent(page));
            } else {
                String error = "Cannot set final HTML for DocPage that has existing content: \"" + page.getTitle() + "\".";
                this.logger.fatal(error);
                throw new DocBuilderException(error);
            }
        }
    }

    //---- TOC Updates ----//

    public void updateAllVersionsOfPagesWithCompleteToc(List<DocSection> docSections) {
        updateCurrentVersionPagesWithCompleteToc(docSections);
        updateOtherVersionPagesWithCompleteToc(docSections);
    }

    public void updateCurrentVersionPagesWithCompleteToc(List<DocSection> docSections) {
        for(int i = 0; i < docSections.size(); i++) {
            DocSection currentSection = docSections.get(i);
            for(DocPage page : currentSection.getPagesInSection()) {
                updatePageWithCompleteToc(page, i, docSections);
            }
        }
    }

    public void updateOtherVersionPagesWithCompleteToc(List<DocSection> docSections) {
        for(DocSection section : docSections) {
            for(int i = 0; i < section.getOtherVersions().size(); i++) {
                DocSection currentSection = section.getOtherVersions().get(i);
                for(DocPage page : currentSection.getPagesInSection()) {
                    updatePageWithCompleteToc(page, i, docSections);
                }
            }
        }
    }

    public void updatePageWithCompleteToc(DocPage page, int sectionIndex, List<DocSection> docSections) {
        validatePageBeforeUpdatingToc(page);
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < docSections.size(); i++) {
            if(i != sectionIndex) {
                builder.append(docSections.get(i).getTocHtmlForInactiveSection());
            } else {
                builder.append(page.getInitialTocHtml());
            }
        }
        page.setFinalTocHtml(builder.toString());
    }

    //---- Validations ----//

    private void validateTemplatesAreInDirectory(String templateDirectory) {
        File templateDir = new File(templateDirectory);
        if(!templateDir.exists()) {
            String error = "Template directory doesn't exist: \"" + templateDirectory + "\".";
            logger.fatal(error);
            throw new DocBuilderException(error);
        }
        File[] templateFiles = templateDir.listFiles();
        if(templateFiles.length == 0) {
            String error = "Template directory doesn't contain template files: \"" + templateDirectory + "\".";
            logger.fatal(error);
            throw new DocBuilderException(error);
        }
    }

    private void validatePageBeforeUpdatingToc(DocPage page) {
        if(page.getInitialTocHtml() == null) {
            String error = "DocPage's TOC HTML is null, cannot update with complete TOC for \"" + page.getTitle() + "\".";
            logger.fatal(error);
            throw new DocBuilderException(error);
        }
    }

    private void validateChildDirectoriesInSourceDirectory(File sourceDirectory) {
        File[] files = sourceDirectory.listFiles();
        if((files == null) || (files.length == 0)) {
            String error = "There are no valid child directories in master source directory to process.";
            logger.fatal(error);
            throw new DocBuilderException(error);
        }
    }

    private void validateSourceDirectory(File directory) {
        File tocFile = new File(Utilities.getConcatenatedFilepath(new String[]{directory.getPath(), "toc.ad"}));
        if(!tocFile.exists()) {
            String error = "Source directory doesn't contain a TOC file: \"" + directory.getName() + "\".";
            logger.fatal(error);
            throw new DocBuilderException(error);
        }
    }
}

/*
public void foo() {
    MasterTableOfContents masterToc = MasterTableOfContents.fromFile("blah");
    List<DocSection> sections = DocSection.loadSectionsFromToc(masterToc);

    PageContent page = PageContent.fromAsciiDocFile("foo.ad");

    SectionTableOfContents toc = SectionTableOfContents.fromFile("foo.toc");
}


public class Section {
    public List<PageContent> pageContents;
    public TableOfContents tableOfContents;
    public String title;

}

public class Page {
    public TableOfContents tableOfContents;
    public Breadcrumb breadCrumb;
    public String title;
    public PageContent pageContent;

    public string toHtml(Template template) {

    }
}

public class Template {

    public string toHtml(Page page) {}

}*/