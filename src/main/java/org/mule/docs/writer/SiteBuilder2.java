package org.mule.docs.writer;

import org.apache.commons.io.FilenameUtils;
import org.mule.docs.loader.IModelLoader;
import org.mule.docs.loader.LoaderFactory;
import org.mule.docs.model.Page;
import org.mule.docs.model.Section;
import org.mule.docs.model.SiteTableOfContents;
import org.mule.docs.model.v2.IPageElementVisitor;
import org.mule.docs.model.v2.Site;
import org.mule.docs.utils.Utilities;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class SiteBuilder2 {

    private File sourceDirectory;
    private File outputDirectory;
    private IModelLoader modelLoader;
    private ISiteWriter siteWriter;
    private List<IPageElementVisitor> preProcessors;
    private List<IPageElementVisitor> enrichers;

    public SiteBuilder2(File sourceDirectory, File outputDirectory) {
        this.sourceDirectory = sourceDirectory;
        this.outputDirectory = outputDirectory;
        preProcessors = loadPreprocessors();
        enrichers = loadEnrichers();
        modelLoader = LoaderFactory.newInstance();
        siteWriter = SiteWriterFactory.newInstance();
    }

    private List<IPageElementVisitor> loadEnrichers() {
        return new ArrayList<IPageElementVisitor>();
    }

    private List<IPageElementVisitor> loadPreprocessors() {
        return new ArrayList<IPageElementVisitor>();
    }

    /**
     * Builds the site
     */
    public void buildSite() {

        Site site = modelLoader.load(sourceDirectory);

        for (IPageElementVisitor preProcessor : preProcessors) {
            preProcessor.visit(site);
        }

        for (IPageElementVisitor enricher : enrichers) {
            enricher.visit(site);
        }

        siteWriter.write(site, outputDirectory);
    }

}
