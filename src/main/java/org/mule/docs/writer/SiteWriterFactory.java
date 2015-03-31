package org.mule.docs.writer;

import org.mule.docs.internal.writer.DefaultSiteWriter;
import org.mule.docs.model.v2.Site;

import java.io.File;

/**
 * Created by Mulesoft.
 */
public class SiteWriterFactory {

    public static ISiteWriter newInstance() {
        return new DefaultSiteWriter();
    }
}
