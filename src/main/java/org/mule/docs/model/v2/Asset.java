package org.mule.docs.model.v2;

/**
 * Created by Mulesoft.
 */
public class Asset extends AbstractBaseDocElement {

    private String content;
    private String baseName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    @Override
    public void accept(IPageElementVisitor visitor) {
        visitor.visit(this);
    }
}
