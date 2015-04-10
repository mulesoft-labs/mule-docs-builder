package org.mule.docs.generator;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

import org.mule.docs.writer.SiteBuilder;

/**
 * Doc Site Builder Mojo
 */
@Mojo(name = "build", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class SiteBuilderMojo
        extends AbstractMojo {

    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", required = true)
    private File outputDirectory;

    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${basedir}", property = "sourceDir", required = true)
    private File sourceDirectory;

    public void execute()
            throws MojoExecutionException {
        File f = outputDirectory;

        if (!f.exists()) {
            f.mkdirs();
        }

        if(!sourceDirectory.exists()){
            throw new MojoExecutionException("Source directory doesn't exists: " + sourceDirectory);
        }
        SiteBuilder.buildSite(sourceDirectory.getAbsoluteFile(), outputDirectory.getAbsoluteFile());
    }
}
