package org.mule.docs.model.v2;

import java.io.File;

/**
 * Created by Mulesoft.
 */
public class TableOfContents extends AbstractBaseDocElement {

    private File tocFile;

    public File getTocFile() {
        return tocFile;
    }

    public void setTocFile(File tocFile) {
        this.tocFile = tocFile;
    }

    @Override
    public void accept(IPageElementVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "TOC:" + tocFile.getAbsolutePath();
    }
}
