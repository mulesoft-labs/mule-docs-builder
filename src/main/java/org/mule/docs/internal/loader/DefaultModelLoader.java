package org.mule.docs.internal.loader;

import org.apache.commons.io.FilenameUtils;
import org.mule.docs.loader.IModelLoader;
import org.mule.docs.model.v2.Page;
import org.mule.docs.model.v2.Section;
import org.mule.docs.model.v2.Site;
import org.mule.docs.model.v2.TableOfContents;
import org.mule.docs.utils.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mulesoft.
 */
public class DefaultModelLoader implements IModelLoader {

    @Override
    public Site load(File sourceDirectory) {
        Site site = new Site();
        List<Section> sections = new ArrayList<Section>();
        if (sourceDirectory.isDirectory()) {
            for (File file : sourceDirectory.listFiles()) {
                if (isValidSectionDirectory(file)) {
                    loadSectionFromDir(sections, file);
                }
            }
        }
        File tocFile = new File(sourceDirectory, "toc.ad");
        TableOfContents toc = new TableOfContents();
        toc.setTocFile(tocFile);
        site.setToc(toc);
        site.setSections(sections);
        return site;
    }

    private void loadSectionFromDir(List<Section> sections, File directory) {
        Section section = new Section();
        List<File> validFiles = getValidAsciiDocFilesInSection(new ArrayList<File>(Arrays.asList(directory.listFiles())));
        List<Page> pages = new ArrayList<Page>();
        for (File asciiDocFile : validFiles) {
            Page page = new Page();
            page.setOrigin(asciiDocFile);
            page.setBaseName(FilenameUtils.getBaseName(asciiDocFile.getName()));
            pages.add(page);
        }
        File tocFile = new File(directory, "toc.ad");
        File versionsDirectory = new File(directory, "v");
        if (versionsDirectory.exists()) {
            List<Section> versions = new ArrayList<Section>();
            processVersions(versions, versionsDirectory);
            section.setVersions(versions);
        }
        TableOfContents toc = new TableOfContents();
        toc.setTocFile(tocFile);
        section.setToc(toc);
        section.setPages(pages);
        sections.add(section);
    }

    private void processVersions(List<Section> versions, File versionsDirectory) {
        for (File file : versionsDirectory.listFiles()) {
            if (file.isDirectory()) {
                loadSectionFromDir(versions, file);
            }
        }
    }

    private boolean isValidSectionDirectory(File directory) {
        if (!directory.isDirectory() ||
                directory.getName().startsWith("_") ||
                directory.getName().contentEquals(".DS_Store")) {
            return false;
        }
        return true;
    }

    private static List<File> getValidAsciiDocFilesInSection(List<File> files) {
        List<File> validFiles = new ArrayList<File>();
        for (File file : files) {
            if (!file.isDirectory() &&
                    Utilities.fileEndsWithValidAsciidocExtension(file.getName()) &&
                    !file.getName().contentEquals("toc.ad")) {
                validFiles.add(file);
            }
        }
        return validFiles;
    }
}
