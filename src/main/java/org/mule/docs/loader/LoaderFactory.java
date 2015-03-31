package org.mule.docs.loader;

import org.mule.docs.internal.loader.DefaultModelLoader;

import java.io.File;

/**
 * Created by Mulesoft.
 */
public class LoaderFactory {

    public static IModelLoader newInstance(){
        return new DefaultModelLoader();
    }

}
