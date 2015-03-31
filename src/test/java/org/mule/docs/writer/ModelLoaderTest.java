package org.mule.docs.writer;

import org.junit.Test;
import org.mule.docs.model.v2.DefaultPageElementVisitor;
import org.mule.docs.model.v2.IPageElementVisitor;
import org.mule.docs.model.v2.ModelLoader;
import org.mule.docs.model.v2.Site;

import java.io.File;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Mulesoft.
 */
public class ModelLoaderTest {

    @Test
    public void buildSite_withRealFolders_BuildsSite() {
        File source = new File(getClass().getClassLoader().getResource("master-folder").getFile());
        File output = new File(getClass().getClassLoader().getResource("").getFile(), "output");

        SiteBuilder2 builder = new SiteBuilder2(source, output);
        builder.buildSite();
    }
}
