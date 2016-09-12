package com.mulesoft.documentation.builder.previewer;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.asciidoctor.ast.Document;
import org.asciidoctor.ast.StructuralNode;
import org.asciidoctor.extension.BlockProcessor;
import org.asciidoctor.extension.ContentModel;
import org.asciidoctor.extension.Contexts;
import org.asciidoctor.extension.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mulesoft.documentation.builder.util.Utilities;

@Contexts(Contexts.CONTEXT_LISTING)
@ContentModel(ContentModel.COMPOUND)
public class TabsProcessor extends BlockProcessor {
    private static Logger logger = LoggerFactory.getLogger(TabsProcessor.class);

    private AsciiDocProcessor processor;

    public TabsProcessor() {
        processor = AsciiDocProcessor.getProcessorInstance();
    }

    @Override
    public Object process(StructuralNode parent, Reader reader, Map<String, Object> attributes) {
        Document doc = parent.getDocument();
        Set<String> tabTitles = new HashSet<String>();

        long tabsetIndex = ((long) doc.getAttr("tabset-counter", 0l)) + 1l;
        doc.setAttr("tabset-counter", tabsetIndex, true);

        StructuralNode tabset = createBlock(parent, "open", Collections.<String>emptyList());
        tabset.setAttr("tabset-index", tabsetIndex, true);

        parseContent(tabset, reader.readLines());

        List<StructuralNode> tabs = tabset.getBlocks();

        int i = 0;
        for (StructuralNode tab : tabs) {
            // NOTE get the unsubstituted title
            String tabTitle = (String) tab.getAttr("title");
            if (!tabTitles.add(tabTitle)) {
                logger.warn("Duplicate tab title detected in " + doc.getAttr("docfile", "<stdin>") + ".");
            }

            String tabId = "tab-" + tabsetIndex + "_" + (i + 1);
            String tabContent = Utilities.getOnlyContentDivFromHtml(processor.convertAsciiDocString((String) tab.getContent()));
            String tabPanel = "<div class=\"tab-pane " + (i == 0 ? "in active " : "") + "fade no-padding\" id=\"" + tabId + "\">" +
                    tabContent +
                    "</div>";

            tab = createBlock(parent, "pass", tabPanel);
            tab.setId(tabId);
            tab.setTitle(tabTitle);

            tabs.set(i, tab);
            i++;
        }

        String tabsetPanelOpen = "<div class=\"panel panel-default no-padding\">\n" +
                "    <div class=\"panel-heading no-padding\">\n" +
                "        <!-- Nav tabs -->\n" +
                "        <ul class=\"nav nav-tabs\" role=\"tablist\">\n";

        i = 0;
        for (StructuralNode tab : tabs) {
            tabsetPanelOpen += "<li" + (i == 0 ? " class=\"active\"" : "") + ">" +
                    "<a href=\"#" + tab.getId() + "\" role=\"tab\" data-toggle=\"tab\">" + tab.getTitle() + "</a>" +
                    "</li>\n";
            i++;
        }

        tabsetPanelOpen += "</ul>\n</div>\n<div class=\"panel-body tab-content no-padding\">";
        String tabsetPanelClose = "</div></div>";

        return createBlock(parent, "pass", tabsetPanelOpen + (String) tabset.getContent() + tabsetPanelClose);
    }
}
