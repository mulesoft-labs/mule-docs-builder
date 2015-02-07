import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean.osterberg on 2/6/15.
 */
public class DocPageBuilder {


    private List<DocPage> createDocPagesFromConvertedHtml(String[] docHtml) {
        List<DocPage> docPages = new ArrayList<DocPage>();
        for(String doc : docHtml) {
            DocPage docPage = new DocPage();
            docPage.setContentHtml(doc);
            docPages.add(docPage);
        }
        return docPages;
    }

    private DocPage createDocPageFromConvertedHtml(String docHtml) {
        DocPage docPage = new DocPage();
        docPage.setContentHtml(docHtml);
        return docPage;
    }
}
