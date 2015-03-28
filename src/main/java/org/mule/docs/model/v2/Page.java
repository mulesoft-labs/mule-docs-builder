package org.mule.docs.model.v2;

import com.sun.source.tree.AssertTree;

import java.util.List;

/**
 * Created by Mulesoft.
 */
public class Page {
    private String content;
    private String baseName;
    private List<Asset> assets;

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

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }
}
