package org.mule.docs.model.v2;

import java.io.File;
import java.util.List;

/**
 * Created by Mulesoft.
 */
public class Page extends AbstractBaseDocElement {

    private File origin;
    private String baseName;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getOrigin() {
        return origin;
    }

    public void setOrigin(File origin) {
        this.origin = origin;
    }

    private List<Asset> assets;

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    @Override
    public void accept(IPageElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Page:" + origin.getAbsolutePath();
    }
}
