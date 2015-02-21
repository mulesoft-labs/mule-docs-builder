import java.util.*;
import java.io.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by sean.osterberg on 2/15/15.
 */
public class SectionBuilder {
    private static Logger logger = Logger.getLogger(SectionBuilder.class);
    private String baseUrl;
    private List<PageTemplate> pageTemplates;

    public SectionBuilder(List<PageTemplate> pageTemplates, String baseUrl) {
        this.pageTemplates = pageTemplates;
        this.baseUrl = baseUrl;
    }

    public List<DocSection> getDocSectionsFromListOfDirectories(List<File> directories) {
        List<DocSection> docSections = new ArrayList<DocSection>();
        for(File directory : directories) {
            if(!directory.getName().startsWith("_")) {
                DocSection section = getCurrentDocSectionFromDirectory(directory);
                section.setPagesInSection(getInitialDocPagesForSection(section));
                docSections.add(section);
            }
        }
        return docSections;
    }

    //

    private DocSection getCurrentDocSectionFromDirectory(File directory) {
        DocSection section = getDocSectionFromDirectory(directory, "Current");
        section.setOtherVersions(getOtherVersionsInSection(directory));
        logger.info("Processed directory for section \"" + directory.getName() + "\".");
        return section;
    }

    private DocSection getOtherVersion(File directory) {
        String versionName = directory.getName();
        DocSection otherVersion = getDocSectionFromDirectory(directory, versionName);
        logger.info("Processed directory for other version: \"" + directory.getName() + "\".");
        return otherVersion;
    }

    public DocSection getDocSectionFromDirectory(File directory, String versionName) {
        validateSectionDirectory(directory);
        validateVersionName(directory, versionName);
        DocSection section = new DocSection(directory.getPath(), versionName);
        TocBuilder tocBuilder = new TocBuilder(directory.getName(), Utilities.getConcatenatedFilepath(new String[]{directory.getPath(), "toc.ad"}));
        section.setSectionPathFromBaseUrl(Utilities.getConcatenatedFilepath(new String[] { this.baseUrl, directory.getName()}));
        section.setRootNodeForToc(tocBuilder.getRootNode());
        section.setTocHtmlForInactiveSection(tocBuilder.getTocHtmlForInactiveSection());
        section.setSectionName(tocBuilder.getRootNode().getTitle()); // Title for the section comes from the first node in the TOC
        return section;
    }

    private List<DocPage> getInitialDocPagesForSection(DocSection section) {
        PageBuilder builder = new PageBuilder(this.pageTemplates, this.baseUrl);
        return builder.getInitialDocPagesForSection(section);
    }

    public List<DocSection> getOtherVersionsInSection(File directory) {
        List<DocSection> otherVersions = new ArrayList<DocSection>();
        File versionsDirectory = new File(Utilities.getConcatenatedFilepath(new String[] { directory.getPath(), "v"}));
        if(versionsDirectory.exists()) {
            File[] versionDirectories = versionsDirectory.listFiles();
            for(File versionDirectory : versionDirectories) {
                if(versionDirectory.isDirectory()) {
                    otherVersions.add(getOtherVersion(versionDirectory));
                }
            }
        }
        return otherVersions;
    }

    private void validateSectionDirectory(File directory) {
        if(!directory.exists()) {
            String error = "Directory to process section does not exist: \"" + directory.getPath() + "\".";
            this.logger.fatal(error);
            throw new DocBuilderException(error);
        }else if(!directoryContainsAsciiDocFiles(directory)) {
            String error = "Directory to process section does not contain valid AsciiDoc files: \"" + directory.getPath() + "\".";
            this.logger.fatal(error);
            throw new DocBuilderException(error);
        } else if(!directoryContainsTocFile(directory)) {
            String error = "Directory to process section does not contain valid TOC file: \"" + directory.getPath() + "\".";
            this.logger.fatal(error);
            throw new DocBuilderException(error);
        }
    }

    private void validateVersionName(File directory, String versionName) {
        if((versionName == null) || (versionName.isEmpty()) || (StringUtils.isWhitespace(versionName))) {
            String error = "Version name for directory is empty, null, or whitespace: \"" + directory.getPath() + "\".";
            this.logger.fatal(error);
            throw new DocBuilderException(error);
        }
    }

    private boolean directoryContainsTocFile(File directory) {
        File tocFile = new File(Utilities.getConcatenatedFilepath(new String[]{directory.getPath(), "toc.ad"}));
        if(tocFile.exists()) {
            return true;
        }
        return false;
    }

    private boolean directoryContainsAsciiDocFiles(File directory) {
        File[] filesInDirectory = directory.listFiles();
        boolean containsAsciiDocFiles = false;
        for(File file : filesInDirectory) {
            if(Utilities.fileEndsWithValidAsciidocExtension(file.getName())) {
                containsAsciiDocFiles = true;
            }
        }
        return containsAsciiDocFiles;
    }
}
