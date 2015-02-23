import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

/**
 * Created by sean.osterberg on 2/22/15.
 */

public class Section {
    private static Logger logger = Logger.getLogger(Section.class);
    private List<AsciiDocPage> pages;
    private TocNode rootNode;
    private String filepath;
    private List<Section> versions;

    public Section(List<AsciiDocPage> pages, TocNode rootNode, List<Section> versions, String filepath) {
        validateInputParams(new Object[] {pages, rootNode, versions, filepath});
        this.pages = pages;
        this.rootNode = rootNode;
        this.versions = versions;
        this.filepath = filepath;
        logger.info("Created Section for directory \"" + filepath + "\".");
    }

    public List<AsciiDocPage> getPages() {
        return pages;
    }

    public TocNode getRootNode() {
        return rootNode;
    }

    public String getFilepath() {
        return filepath;
    }

    public List<Section> getVersions() {
        return versions;
    }

    public static Section fromDirectory(File directory) {
        validateDirectory(directory);
        return getSectionFromDirectory(directory);
    }

    public static List<Section> fromMasterDirectory(File masterDirectory) {
        validateMasterDirectory(masterDirectory);
        List<Section> sections = new ArrayList<Section>();
        if(masterDirectory.isDirectory()) {
            for (File directory : masterDirectory.listFiles()) {
                if(directory.isDirectory() && !directory.getName().startsWith("_")) {
                    sections.add(fromDirectory(directory));
                }
            }
        }
        return sections;
    }

    private static Section getSectionFromDirectory(File directory) {
        List<File> validFiles = getValidAsciiDocFilesInSection(new ArrayList<File>(Arrays.asList(directory.listFiles())));
        List<AsciiDocPage> pages = AsciiDocPage.fromFiles(validFiles);
        File tocFile = new File(Utilities.getConcatPath(new String[] {directory.getPath(), "toc.ad"}));
        TocNode rootNode = SectionTableOfContents.fromAsciiDocFile(tocFile).getRootTocNode();
        List<Section> versions = new ArrayList<Section>();
        if(Utilities.directoryContainsVersions(directory)) {
            getVersions(directory, versions);
        }
        return new Section(pages, rootNode, versions, directory.getPath());
    }

    private static List<File> getValidAsciiDocFilesInSection(List<File> files) {
        List<File> validFiles = new ArrayList<File>();
        for(File file : files) {
            if(!file.isDirectory() &&
                    Utilities.fileEndsWithValidAsciidocExtension(file.getName()) &&
                    !file.getName().contentEquals("toc.ad")) {
                validFiles.add(file);
            }
        }
        return validFiles;
    }

    private static void getVersions(File directory, List<Section> versions) {
        File versionDirectory = new File(Utilities.getConcatPath(new String[] {directory.getPath(), "v"}));
        for(File file : versionDirectory.listFiles()) {
            if(file.isDirectory()) {
                versions.add(getSectionFromDirectory(file));
            }
        }
    }

    private static void validateMasterDirectory(File masterDirectory) {
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

    private static void validateDirectory(File directory) {
        Utilities.validateIsDirectory(directory);
        Utilities.validateDirectoryContainsAsciiDocFile(directory);
        Utilities.validateDirectoryContainsTocFile(directory);
    }

    private static void validateInputParams(Object[] params) {
        Utilities.validateCtorObjectsAreNotNull(params, Section.class.getSimpleName());
    }
}
