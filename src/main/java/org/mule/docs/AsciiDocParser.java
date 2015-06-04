package org.mule.docs;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.ast.Block;
import org.asciidoctor.extension.Reader;

import java.util.*;

/**
 * Created by sean.osterberg on 6/3/15.
 */
public interface AsciiDocParser {
    Block nextBlock(Reader reader, AbstractBlock parent, Map<String, Object> attributes);
}
