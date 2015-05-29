package org.mule.docs.processor;

import org.junit.Test;
import org.mule.docs.model.AsciiDocPage;
import org.mule.docs.utils.TestUtilities;
import org.mule.docs.utils.Utilities;

import java.io.File;

/**
 * Created by sean.osterberg on 5/6/15.
 */
public class TabHtmlProcessorTest {

    @Test
    public void process_WithValidTabExample_ReturnsExpectedBlocks() {
        AsciiDocProcessor adocProcessor = AsciiDocProcessor.getProcessorInstance();
        TabProcessor processor = new TabProcessor();
        String fileContents = Utilities.getFileContentsFromFile(getValidFile());
        String result = processor.process(fileContents);
        File outputFile = new File(Utilities.getConcatPath(TestUtilities.getTestResourcesPath(), "html-processors", "tabs-output.adoc"));
        Utilities.writeFileToDirectory(outputFile.getAbsolutePath(), result);
        adocProcessor.convertFile(outputFile);
        String foo = "foo";
    }

    @Test
    public void yellBlockTest() {
        AsciiDocPage page = AsciiDocPage.fromFile(getValidFile());
    }

    private File getValidFile() {
        String path = "/Users/sean.osterberg/mulesoft-docs/dev/src/test/resources/text-files/tab-test.adoc";
        return new File(path);
    }

}
