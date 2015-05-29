/**
 * Created by sean.osterberg on 5/28/15.
 */
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.ast.ContentPart;
import org.asciidoctor.ast.StructuredDocument;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.Reader;

import java.util.*;

/**
 * Created by sean.osterberg on 5/6/15.
 */
public class TabProcessor extends BlockProcessor {
    private final Asciidoctor asciidoctor;

    private static Map<String, Object> configs = new HashMap<String, Object>();

    static {
        configs.put("contexts", Arrays.asList(":listing"));
        configs.put("content_model", ":compound");
    }

    public TabProcessor(String name, Asciidoctor asciidoctor) {
        super(name, configs);
        this.asciidoctor = asciidoctor;
    }

    @Override
    public Object process(AbstractBlock parent, Reader reader, Map<String, Object> attributes) {
        //String buffer = reader.read();
        //StructuredDocument doc = asciidoctor.readDocumentStructure(buffer, attributes);

        AbstractBlock block = createBlock(parent, "literal", reader.read(), attributes, new HashMap<Object, Object>());
        System.out.println(block.blocks());

        List<String> lines = reader.readLines();
        String upperLines = null;
        for (String line : lines) {
            if (upperLines == null) {
                upperLines = line.toUpperCase();
            }
            else {
                upperLines = upperLines + "\n" + line.toUpperCase();
            }
        }

        return createBlock(parent, "paragraph", Arrays.asList(upperLines), attributes, new HashMap<Object, Object>());
    }
}