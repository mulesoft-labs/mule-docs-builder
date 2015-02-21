/**
 * Created by sean.osterberg on 2/17/15.
 */
public class PageTemplate {
    private String templateFilepath;
    private String templateContents;
    private PageTemplateType templateType;

    public String getTemplateFilepath() {
        return templateFilepath;
    }

    public void setTemplateFilepath(String templateFilepath) {
        this.templateFilepath = templateFilepath;
    }

    public String getTemplateContents() {
        return templateContents;
    }

    public void setTemplateContents(String templateContents) {
        this.templateContents = templateContents;
    }

    public PageTemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(PageTemplateType templateType) {
        this.templateType = templateType;
    }
}
