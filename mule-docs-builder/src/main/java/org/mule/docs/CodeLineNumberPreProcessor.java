package org.mule.docs;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.ast.Block;
import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.*;

import org.mule.docs.util.Utilities;

import java.util.*;

@Contexts(Contexts.CONTEXT_LISTING)
@ContentModel(ContentModel.COMPOUND)
public class CodeLineNumberPreProcessor extends Preprocessor {

    private AsciiDocProcessor processor;

    public CodeLineNumberPreProcessor() {
        processor = AsciiDocProcessor.getProcessorInstance();
    }

    @Override
    public PreprocessorReader process(Document document, PreprocessorReader reader) {
        document.getAttributes().put("coderay-linenums-mode", "table");
        return reader;
    }
}

