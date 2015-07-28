package org.mule.docs.SitemapGenerator;

import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.mule.docs.SitemapGenerator.Utilities.Utilities;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Copyright (C) MuleSoft, Inc - All Rights Reserved
 * Created by Sean Osterberg on 7/25/2015.
 */

public class Generator {
    static Logger logger = Logger.getLogger(Generator.class);
    private String rootUrl;
    private File targetDirectory;

    public Generator(String rootUrl, String targetDirectory) {
        this.rootUrl = rootUrl;
        this.targetDirectory = new File(targetDirectory);
        Utilities.validateIsDirectory(this.targetDirectory);
    }

    /**
     * Creates a sitemap file in the target directory.
     */
    public void generate() {
        try {
            WebSitemapGenerator wsg = new WebSitemapGenerator(this.rootUrl, this.targetDirectory);
            List<WebSitemapUrl> urls = getUrlsForSite();
            for(WebSitemapUrl url : urls) {
                wsg.addUrl(url);
            }
            logger.info("Wrote " + urls.size() + " urls to sitemap.xml in " + this.targetDirectory);
            List<File> outputFiles = wsg.write();

        } catch (MalformedURLException e) {
            throw new RuntimeException("The root URL is malformed -- site map generator is unable to start.", e);
        }
    }

    /**
     * Gets a list of urls for the site, including the last modified date.
     * @return A list of WebSitemapUrls.
     */
    public List<WebSitemapUrl> getUrlsForSite() {
        List<WebSitemapUrl> urls = new ArrayList<WebSitemapUrl>();
        try {
            Set<String> paths = getPathsForHtmlFiles();
            for(String path : paths) {
                File sitePage = new File(path);
                String url = getUrlFromPath(sitePage.getAbsolutePath());
                Date lastModified = new Date(sitePage.lastModified());
                WebSitemapUrl entry = new WebSitemapUrl.Options(url).lastMod(lastModified).build();
                urls.add(entry);
            }
        } catch(MalformedURLException e) {
            throw new RuntimeException("Failed to build WebSitemapUrl entry. ", e);
        }
        return urls;
    }


    /**
     * Gets a set of html files from the target directory and all subdirectories.
     * @return A set of unique directory paths for every html file in the target directory.
     */
    public Set<String> getPathsForHtmlFiles() {
        Set<String> paths = new HashSet<String>();
        if(this.targetDirectory.isDirectory()) {
            getHtmlFilesInDirectory(this.targetDirectory, paths);
        }
        return paths;
    }

    private void getHtmlFilesInDirectory(File directory, Set<String> paths) {
        getFilesWithExtensionInDirectory(directory, "html", paths);
    }

    /**
     * Gets all files with the desired extension from a directory and its subdirectories and adds them to a set.
     * @param directory The directory to get the html files from.
     * @param paths The unique set of paths to html files.
     */
    public void getFilesWithExtensionInDirectory(File directory, String extension, Set<String> paths) {
        if(directory.isDirectory()) {
            File[] files = directory.listFiles();
            if(files != null) {
                for (File f : files) {
                    if(FilenameUtils.getExtension(f.getName()).equals(extension)) {
                        paths.add(f.getAbsolutePath());
                    } else if(f.isDirectory()) {
                        getFilesWithExtensionInDirectory(f, extension, paths);
                    }
                }
            }
        }
    }

    /**
     * Gets a fully-qualified URL from a path, combining the rootUrl with the desired section and topic path.
     * @param path A set of paths from which to create URLs.
     * @return A set of fully-qualified URLs.
     */
    public String getUrlFromPath(String path) {
        String basePathToRemove = this.targetDirectory.getAbsolutePath();
        path = path.replace(basePathToRemove, "");
        path = path.replace(".html", "");
        path = removeIndexHtmlPathFromUrl(path);
        path = this.rootUrl + path;

        return path;
    }

    private String removeIndexHtmlPathFromUrl(String url) {
        if(url.endsWith("index")) {
            int start = url.length() - 5;
            url = url.substring(0, start);
        }
        return url;
    }
}
