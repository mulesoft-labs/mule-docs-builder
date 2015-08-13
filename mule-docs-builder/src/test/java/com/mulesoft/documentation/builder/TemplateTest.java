package com.mulesoft.documentation.builder;

import com.mulesoft.documentation.builder.util.Utilities;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import java.io.File;

import static org.hamcrest.CoreMatchers.instanceOf;

/**
 * Created by sean.osterberg on 3/9/15.
 */
public class TemplateTest {

    @Test
    public void fromFile_WithValidFile_ReturnsNewInstance() {
        Template template = Template.fromFile(new File(Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "master-folder", "_templates", "default.template"})));
        assertThat(template, instanceOf(Template.class));
    }

    @Test(expected = DocBuildException.class)
    public void fromFile_WithInvalidFileExtension_ThrowsException() {
        Template template = Template.fromFile(new File(Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "master-folder", "anypoint-platform", "cloudhub.ad"})));
    }

    @Test
    public void fromFile_WithValidFile_ContainsContent() {
        Template template = getValidTemplate();
        String validHtml = "{{ page.title }}";
        assertTrue(template.getContents().contains(validHtml));
    }

    @Test
    public void fromFile_WithDefaultTemplate_IsDefault() {
        Template template = getValidTemplate();
        assertTrue(template.getType().equals(TemplateType.DEFAULT));
    }

    private Template getValidTemplate() {
        return Template.fromFile(new File(Utilities.getConcatPath(new String[]{TestUtilities.getTestResourcesPath(), "master-folder", "_templates", "default.template"})));
    }
}
