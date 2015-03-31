package org.mule.docs.loader;

import org.mule.docs.model.v2.Site;

import java.io.File;

/**
 * Created by Mulesoft.
 */
public interface IModelLoader {
    Site load(File source);
}
