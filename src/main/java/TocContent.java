/**
 * Created by sean.osterberg on 2/11/15.
 */
public class TocContent {
    private TocNode rootNode;
    private String rootTitle;
    private String inactiveTocHtml;

    public TocContent(TocNode rootNode, String rootTitle, String inactiveTocHtml) {
        this.rootNode = rootNode;
        this.rootTitle = rootTitle;
        this.inactiveTocHtml = inactiveTocHtml;
    }

    public TocNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TocNode rootNode) {
        this.rootNode = rootNode;
    }

    public String getRootTitle() {
        return rootTitle;
    }

    public void setRootTitle(String rootTitle) {
        this.rootTitle = rootTitle;
    }

    public String getInactiveTocHtml() {
        return inactiveTocHtml;
    }

    public void setInactiveTocHtml(String inactiveTocHtml) {
        this.inactiveTocHtml = inactiveTocHtml;
    }
}
