package org.mule.docs.writer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.mule.docs.processor.DocBuildException;
import org.mule.docs.utils.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean.osterberg on 3/9/15.
 */
public class Template {

    private String contents;
    private TemplateType type;

    public Template(String contents, TemplateType type) {
        this.contents = contents;
        this.type = type;
    }

    public static List<Template> fromDirectory(File directory) {
        List<Template> templates = new ArrayList<Template>();
        Utilities.validateIsDirectory(directory);
        for (File templateFile : directory.listFiles()) {
            if (FilenameUtils.getExtension(templateFile.getName()).contentEquals("template")) {
                templates.add(fromFile(templateFile));
            }
        }
        return templates;
    }

    public static Template fromFile(File file) {
        Utilities.validateTemplateFile(file);
        String contents = getTemplateContents(file);
        TemplateType type = getTemplateType(file);
        return new Template(contents, type);
    }

    private static String getTemplateContents(File file) {
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            throw new DocBuildException("Unable to read content",e);
        }
    }

    private static TemplateType getTemplateType(File file) {
        String baseName = FilenameUtils.getBaseName(file.getName());
        if (baseName.equalsIgnoreCase("default")) {
            return TemplateType.DEFAULT;
        } else if (baseName.contentEquals("landing_page")) {
            return TemplateType.LANDING_PAGE;
        }
        String error = "org.mule.docs.writer.Template file's type is not valid: \"" + file.getName() + "\".";
        throw new DocBuildException(error);
    }

    private static void validateInputParams(Object[] params) {
        Utilities.validateCtorObjectsAreNotNull(params, Template.class.getSimpleName());
    }

    public String getContents() {
        return contents;
    }

    public TemplateType getType() {
        return type;
    }
}
