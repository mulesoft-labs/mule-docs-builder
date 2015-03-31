package org.mule.docs.writer;

import org.mule.docs.model.v2.Site;

import java.io.File;

/**
 * Created by Mulesoft.
 */
public interface ISiteWriter {

    boolean write(Site site,File outputDirectory);
}
