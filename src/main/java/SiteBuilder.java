/**
 * Created by sean.osterberg on 1/9/15.
 */

import org.apache.commons.io.*;
import java.io.IOException;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Logger;
import org.apache.log4j.BasicConfigurator;
import org.jsoup.*;
import org.jsoup.select.*;
import org.jsoup.nodes.*;


public class SiteBuilder {
    static Logger logger = Logger.getLogger(SiteBuilder.class.getName());
    public static Process _process;
    /*
    public static String sourceAsciiDocDirectory = "/Users/sean.osterberg/Documents/MuleSoft Docs/New Doc Site/source-asciidoc/";
    public static String outputDirectoryForConvertedHtml = "/Users/sean.osterberg/Documents/MuleSoft Docs/New Doc Site/converted-output/";
    public static String outputDirectoryForProcessedHtml = "/Users/sean.osterberg/Documents/MuleSoft Docs/New Doc Site/processed-output/";
    public static String jekyllSiteDirectory = "/Users/sean.osterberg/Documents/MuleSoft Docs/New Doc Site/Jekyll Site/";
    */

    public static String sourceAsciiDocDirectory = "/Users/sean.osterberg/mulesoft-docs/_source";
    public static String outputDirectoryForConvertedHtml = "/Users/sean.osterberg/mulesoft-docs/_temp/converted_html/";
    public static String outputDirectoryForProcessedHtml = "/Users/sean.osterberg/mulesoft-docs/_temp/processed_html/";
    public static String jekyllSiteDirectory = "/Users/sean.osterberg/mulesoft-docs/_site/";
    public static String templateFilePath = "/Users/sean.osterberg/mulesoft-docs/_site/_templates/default.html";

    public static void main(String[] args) {
        BasicConfigurator.configure();
        SiteBuilder builder = new SiteBuilder();
        builder.buildMuleDocs();
        //builder.buildToc();

        /*
        Read the source folder
            For each first child in the source folder
                process it like any other folder

        */
    }

    public void buildDocsInDirectory(String directory) {
        
    }

    public void buildMuleDocs() {
        /*
        writeBookendsToLog("(!) Starting to build Mule Docs...");
        writeToLog("Source Directory: " + sourceAsciiDocDirectory);
        writeToLog("Converted Directory: " + outputDirectoryForConvertedHtml);
        writeToLog("Processed Directory: " + outputDirectoryForProcessedHtml + "\n");
        createHtmlFilesFromAsciiDocFiles(sourceAsciiDocDirectory, outputDirectoryForConvertedHtml);
        createTocForJekyllSite(outputDirectoryForConvertedHtml, jekyllSiteDirectory);
        createProcessedHtmlFilesForCustomBackend(outputDirectoryForConvertedHtml, outputDirectoryForProcessedHtml, templateFilePath);
        copyHtmlFilesToJekyllDirectory(outputDirectoryForProcessedHtml, jekyllSiteDirectory);
        writeBookendsToLog("(!) Finished building Mule Docs.");*/

        writeBookendsToLog("(!) Starting to build Mule Docs...");
        writeToLog("Source Directory: " + sourceAsciiDocDirectory);
        writeToLog("Converted Directory: " + outputDirectoryForConvertedHtml);
        writeToLog("Processed Directory: " + outputDirectoryForProcessedHtml + "\n");
        createHtmlFilesFromAsciiDocFiles(sourceAsciiDocDirectory, outputDirectoryForConvertedHtml);
        createTocForJekyllSite(outputDirectoryForConvertedHtml, jekyllSiteDirectory);
        createProcessedHtmlFilesForCustomBackend(outputDirectoryForConvertedHtml, outputDirectoryForProcessedHtml, templateFilePath);
        copyHtmlFilesToJekyllDirectory(outputDirectoryForProcessedHtml, jekyllSiteDirectory);
        writeBookendsToLog("(!) Finished building Mule Docs.");
    }

    /**
     * Creates HTML files from the source AsciiDoc files by calling AsciiDoctor from the command line.
     * @param sourceDirectory The source directory that contains AsciiDoc files.
     * @param destinationDirectory The destination directory to put the generated HTML files.
     */
    private void createHtmlFilesFromAsciiDocFiles(String sourceDirectory, String destinationDirectory) {
        int count = 0;
        writeMilestoneToLog("Converting AsciiDoc to HTML...");
        File sourceDir = new File(sourceDirectory);

        try {
            for (File dir : sourceDir.listFiles()) {
                if (dir.isDirectory()) {
                    String currentDestDirectory = destinationDirectory + dir.getName();
                    makeTargetDirectory(currentDestDirectory);
                    for (File f : dir.listFiles()) {
                        if (fileEndsWithDesiredExtension(f.getName())) {
                            count++;
                            String[] cmd = createAsciiDoctorCommand(f.getCanonicalPath(), currentDestDirectory);
                            runCommand(cmd);
                            writeToLog("Created new HTML file for " + f.getName());
                        }
                    }
                }
            }
        } catch (IOException ioe) {
            writeToLog(ioe.getMessage());
        }
        writeToLog("Finished converting " + count + " files.");
    }


    /**
     * Creates HTML files from the source AsciiDoc files by calling AsciiDoctor from the command line.
     * @param sourceDirectory The source directory that contains AsciiDoc files.
     * @param destinationDirectory The destination directory to put the generated HTML files.
     */ /*
    private void createHtmlFilesFromAsciiDocFiles(String sourceDirectory, String destinationDirectory) {
        int count = 0;
        writeMilestoneToLog("Converting AsciiDoc to HTML...");
        try {
            File targetDirectory = new File(sourceDirectory);
            for (File f : targetDirectory.listFiles()) {
                if(fileEndsWithDesiredExtension(f.getName())) {
                    count++;
                    String[] cmd = createAsciiDoctorCommand(f.getCanonicalPath(), destinationDirectory);
                    makeTargetDirectory(destinationDirectory);
                    runCommand(cmd);
                    writeToLog("Created new HTML file for " + f.getName());
                }
            }

        } catch (IOException ioe) {
            writeToLog(ioe.getMessage());
        }
        writeToLog("Finished converting " + count + " files.");
    } */

    /**
     * Copies HTML files from the source directory to the destination directory.
     * @param sourceDirectory The source directory that contains HTML files ready for publishing.
     * @param jekyllSiteDirectory The destination directory, such as the Jekyll site folder.
     */
    private void copyHtmlFilesToJekyllDirectory(String sourceDirectory, String jekyllSiteDirectory) {
        int count = 0;
        writeMilestoneToLog("Copying HTML files to Jekyll directory...");
        try {
            File directory = new File(sourceDirectory);
            for (File file : directory.listFiles()) {
                if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("html")) {
                    Path source = Paths.get(sourceDirectory + file.getName());
                    Path destination = Paths.get(jekyllSiteDirectory + file.getName());
                    CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING };
                    Files.copy(source, destination, options);
                    count++;
                    writeToLog("Copied HTML file to Jekyll directory: " + file.getName());
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        writeToLog("Finished copying " + count + " files.");
    }

    /**
     * Creates the command line statement for Asciidoctor.
     * @param filePath The AsciiDoc file to run the command against.
     * @param destinationDirectory The destination directory to put the generated AsciiDoc file.
     * @return The command to execute.
     */
    private String[] createAsciiDoctorCommand(String filePath, String destinationDirectory) {
        List<String> cmd = new ArrayList<String>();
        cmd.add("asciidoctor");
        cmd.add(filePath);
        cmd.add("--destination-dir");
        cmd.add(destinationDirectory);
        cmd.add("-a");
        cmd.add("!stylesheet");

        return cmd.toArray(new String[cmd.size()]);
    }

    /**
     * Creates the table of contents for the Jekyll site.
     * @param convertedDirectory The source directory that contains the HTML TOC file.
     * @param jekyllSiteDirectory The destination directory for the Jekyll site.
     */
    private void createTocForJekyllSite(String convertedDirectory, String jekyllSiteDirectory) {
        writeMilestoneToLog("Generating Table of Contents...");
        int count = 0;
        try {
            File tocFile = new File(convertedDirectory + "toc.html");
            Document doc = Jsoup.parse(tocFile, "UTF-8");
            Element tocElement = doc.getElementById("content");
            count = transformTocLinks(tocElement);
            updateTocInJekyllTemplate(jekyllSiteDirectory, "default.html", tocElement);
        } catch (IOException ioe) {

        }
        writeToLog("Finished updating " + count + " links in the TOC.");
    }

    /**
     * Transforms the links in the TOC to reflect the generated links from Jekyll.
     * @param tocElement The TOC HTML that will be transformed with the updated links.
     */
    private int transformTocLinks(Element tocElement) {
        int count = 0;
        Elements links = tocElement.select("a[href]");
        for(Element link : links) {
            String currentValue = link.attr("href");
            currentValue = currentValue.replace(".html", "/");
            currentValue = "/" + currentValue;
            link.attr("href", currentValue);
            count++;
        }
        return count;
    }

    /**
     * Updates the TOC html within the specified Jekyll template file using the input TOC html.
     * @param jekyllDirectory The directory to the Jekyll directory from which the site is being built.
     * @param templateFileName The file name of the template to update.
     * @param tocElement The modified TOC HTML that is used to replace the template's TOC HTML.
     */
    private void updateTocInJekyllTemplate(String jekyllDirectory, String templateFileName, Element tocElement) {
        try {
            String templateFileFullPath = jekyllDirectory + "_layouts/" + templateFileName;
            File file = new File(templateFileFullPath);
            Document doc = Jsoup.parse(file, "UTF-8");
            Element templateToc = doc.getElementById("nav-mule");
            if(templateToc != null) {
                tocElement.getElementById("content").attr("id", "nav-mule"); // Replace "content" id of div with "nav-mule"
                templateToc.replaceWith(tocElement);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(templateFileFullPath), "UTF-8"));
                writer.write(doc.toString());
                writer.close();
            } else {
                String message = templateFileFullPath + " didn't have a valid element to process.";
                writeToLog(message);
                throw new IOException(message);
            }
        } catch (IOException ioe) {

        }
    }


    /**
     * Processes the original generated HTML files and modifies them for publishing to Jekyll.
     * @param sourceDirectory The source directory that contains the original HTML files.
     * @param destinationDirectory The destination directory for the processed HTML files.
     */
    private void createProcessedHtmlFiles(String sourceDirectory, String destinationDirectory) {
        writeMilestoneToLog("Processing HTML files for Jekyll...");
        makeTargetDirectory(destinationDirectory);
        File directory = new File(sourceDirectory);
        int count = 0;
        for(File file : directory.listFiles()) {
            if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("html")) {
                try {
                    count++;
                    String resizedFilePath = destinationDirectory + file.getName();
                    String resizedHtml = getResizedHtml(file);
                    String frontMatter = getJekyllFrontMatter(file);

                    createProcessedHtmlFileFromResizedHtmlAndFrontMatter(frontMatter, resizedHtml, resizedFilePath);
                    writeToLog("Created processed HTML file: " + file.getName());

                } catch (IOException ioe) {
                    writeToLog("Couldn't create processed HTML file: " + file.getName());
                }
            }
        }
        writeToLog("Finished processing " + count + " files.");
    }

    private void createProcessedFilesForDirectory(String sourceDirectory) {

    }


    /**
     * Processes the original generated HTML files and modifies them for publishing to Jekyll.
     * @param sourceDirectory The source directory that contains the original HTML files.
     * @param destinationDirectory The destination directory for the processed HTML files.
     */
    private void createProcessedHtmlFilesForCustomBackend(String sourceDirectory, String destinationDirectory, String templateFilePath) {
        writeMilestoneToLog("Processing HTML files for custom backend...");
        makeTargetDirectory(destinationDirectory);
        File directory = new File(sourceDirectory);
        int count = 0;
        Map<String, String> tocSections = new HashMap<String, String>();

        for(File dir : directory.listFiles()) {
            if(dir.isDirectory()) {
                try {
                    String[] tocInactive = getTocForInactiveSection(dir.getCanonicalPath());
                    tocSections.put(tocInactive[0], tocInactive[1]);
                } catch (IOException ioe) {

                }
            }
        }
        for(File dir: directory.listFiles()) {
            if(dir.isDirectory()) {
                for (File file : dir.listFiles()) {
                    if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("html")) {
                        try {
                            count++;
                            String resizedFilePath = destinationDirectory + file.getName();
                            String resizedHtml = getResizedHtml(file);

                            PrintWriter output = new PrintWriter(new FileWriter(resizedFilePath));
                            PageBuilder builder = new PageBuilder(templateFilePath, "");
                            String title = getDocTitle(file);

                            TocBuilder tocBuilder = new TocBuilder("");
                            File tocFile = new File(dir + "/toc.html");
                            String resizedTocHtml = getResizedHtml(tocFile);
                            Document doc = Jsoup.parse(resizedTocHtml, "UTF-8");
                            TocNode firstNode = tocBuilder.getRootNodeFromProcessedToc(doc);
                            String sectionTitle = firstNode.getTitle();
                            String tocHtml = tocBuilder.getTocHtml(firstNode, file.getName()).toString();

                            tocHtml = getTocForCurrentSectionAndTopic(sectionTitle, tocHtml, tocSections);

                            String breadcrumbHtml = tocBuilder.getBreadcrumbsForTopic(firstNode, file.getName()).toString();

                            StringBuilder html = builder.generatePage(templateFilePath, resizedHtml, title, tocHtml, breadcrumbHtml, file.getName());
                            output.print(html);
                            output.close();

                            writeToLog("Created processed HTML file: " + file.getName());

                        } catch (IOException ioe) {
                            writeToLog("Couldn't create processed HTML file: " + file.getName());
                        }
                    }
                }
                writeToLog("Finished processing " + count + " files.");
            }
        }
    }

    public String getTocForCurrentSectionAndTopic(String sectionTitle, String tocHtmlForSelectedTopicAndSection, Map<String, String> allTocs) {
        StringBuilder completeToc = new StringBuilder();
        for(Map.Entry<String, String> entry : allTocs.entrySet()) {
            if(!entry.getKey().equalsIgnoreCase(sectionTitle)) {
                completeToc.append(entry.getValue());
            } else {
                completeToc.append(tocHtmlForSelectedTopicAndSection);
            }
        }
        return completeToc.toString();
    }

    // TOC Builder that creates a one-off TOC navigation per page, depending on the depth of the page and its parent/children
    public String[] getTocForInactiveSection(String sourceDirectory) throws IOException {
        TocBuilder tocBuilder = new TocBuilder("");
        File tocFile = new File(sourceDirectory + "/toc.html");
        String resizedHtml = getResizedHtml(tocFile);
        Document doc = Jsoup.parse(resizedHtml, "UTF-8");
        TocNode firstNode = tocBuilder.getRootNodeFromProcessedToc(doc);
        String sectionTitle = firstNode.getTitle();
        String tocHtml = tocBuilder.getTocHtml(firstNode, "").toString();
        return new String[] { sectionTitle, tocHtml };
    }


    private void createProcessedHtmlFileFromResizedHtmlAndFrontMatter(String frontMatter, String html, String resizedFilePath) throws IOException {
        PrintWriter output = new PrintWriter(new FileWriter(resizedFilePath));
        output.print(frontMatter);
        output.print(html);
        output.close();
    }

    private String getResizedHtml(File file) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        Element content = doc.getElementById("content");
        return content.html();
    }

    private String getDocHtml(File file) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        return doc.html();
    }

    private String getDocTitle(File file) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        return doc.title();
    }

    private String getJekyllFrontMatter(File file) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        String frontMatter = "---\n";
        frontMatter = frontMatter.concat("layout: default\n");
        frontMatter = frontMatter.concat("title: " + doc.title() + "\n");
        frontMatter = frontMatter.concat("---\n\n");
        return frontMatter;
    }

    // Helper Methods

    private static boolean fileEndsWithDesiredExtension(String fileName) {
        String extension = FilenameUtils.getExtension(fileName);
        if(extension.equalsIgnoreCase("ad") || extension.equalsIgnoreCase("asciidoc"))
            return true;
        return false;
    }

    private static void makeTargetDirectory(String directoryName) {
        try {
            File output = new File(directoryName);
            output.mkdir();

        } catch (SecurityException se) {
            writeToLog("Cannot create directory.");
        }
    }

    private static void writeToLog(String message) {
        System.out.println(message);
    }

    private static void writeMilestoneToLog(String message) {
        String dashes = "";
        for(int i = 0; i < message.length(); i++) {
            dashes = dashes.concat("-");
        }
        writeToLog(dashes + "\n" + message + "\n" + dashes);
    }

    private static void writeBookendsToLog(String message) {
        String dashes = "";
        for(int i = 0; i < message.length(); i++) {
            dashes = dashes.concat("*");
        }
        writeToLog("\n" + dashes + "\n\n" + message + "\n\n" + dashes + "\n");
    }

    /**
     * Runs a command and redirects the output to a file.
     * @param cmd               The command to run.
     * @param outputFilename    The file to save the output to.
     * @throws IOException
     * @throws InterruptedException
     */
    private static void runCommandWithRedirect(String[] cmd, String outputFilename) throws IOException, InterruptedException {
        if(cmd.length > 0 && !outputFilename.isEmpty()) {
            _process = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(_process.getInputStream()));
            FileOutputStream output = new FileOutputStream(outputFilename);
            String line;
            while ((line = stdInput.readLine()) != null)
                output.write(line.getBytes());
            output.close();
        } else
            throw new IOException("Input cmd string or outputFilename is empty.");
    }

    /**
     *Runs a command and returns the input from the command.
     * @param cmd   The command to run.
     * @return      Returns a BufferedReader for the command input.
     * @throws IOException
     */
    private static List<String> runCommand(String[] cmd) throws IOException {
        if(cmd.length > 0) {
            _process = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(_process.getInputStream()));
            List<String> outputLines = new ArrayList<String>();
            String line;
            while((line = stdInput.readLine()) != null){
                outputLines.add(line);
            }
            return outputLines;
        } else
            throw new IOException("Input cmd string is empty.");
    }

    /**
     * Runs a command, adds each line of the output to a List, and then returns the List.
     * @param cmd   The command to run.
     * @return      Returns a List<String> of output from the command.
     * @throws IOException
     */
    private static List<String> runCommandAndPutOutputInStringArray(String[] cmd) throws IOException{
        if(cmd.length > 0) {
            String temp;
            return runCommand(cmd);
        } else
            throw new IOException("Input cmd string is empty.");
    }
}
