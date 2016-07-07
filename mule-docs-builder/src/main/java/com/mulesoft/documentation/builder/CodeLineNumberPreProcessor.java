package com.mulesoft.documentation.builder;

import org.asciidoctor.ast.Document;
import org.asciidoctor.extension.*;

@Contexts(Contexts.CONTEXT_LISTING)
@ContentModel(ContentModel.COMPOUND)
public class CodeLineNumberPreProcessor extends Preprocessor {

    private AsciiDocProcessor processor;

    public CodeLineNumberPreProcessor() {
        processor = AsciiDocProcessor.getProcessorInstance();
    }

    @Override
    public void process(Document document, PreprocessorReader reader) {
        document.getAttributes().put("coderay-linenums-mode", "table");
    }
}

