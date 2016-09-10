package com.mulesoft.documentation.builder;

import java.util.HashMap;
import java.util.Map;

import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.ContentModel;
import org.asciidoctor.extension.Contexts;
import org.asciidoctor.extension.Reader;

@Contexts(Contexts.CONTEXT_LITERAL)
@ContentModel(ContentModel.VERBATIM)
public class TabProcessor extends BlockProcessor {
    @Override
    public Object process(StructuralNode parent, Reader reader, Map<String, Object> attributes) {
        Map<Object, Object> options = new HashMap<Object, Object>();
        options.put(CONTENT_MODEL, ContentModel.VERBATIM);
        return createBlock(parent, "literal", reader.readLines(), attributes, options);
    }

}
