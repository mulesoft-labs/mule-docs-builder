/**
 * Created by sean.osterberg on 2/4/15.
 */

import org.apache.commons.io.*;
import java.io.IOException;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.jsoup.*;
import org.jsoup.select.*;
import org.jsoup.nodes.*;

public class PageBuilder {
    public PageBuilder() {

    }

    public void buildPages() {

    }

    public StringBuilder generatePage(String templateFilePath,
                                   String contentHtml,
                                   String title,
                                   String tocHtml,
                                   String breadcrumbHtml,
                                   String fileName) throws FileNotFoundException, IOException {

        StringBuilder templateFile = readFile(templateFilePath);
        templateFile = replaceText(templateFile, "{{ page.title }}", title);
        templateFile = replaceText(templateFile, "{{ page.toc }}", tocHtml);
        templateFile = replaceText(templateFile, "{{ page.breadcrumb }}", breadcrumbHtml);
        templateFile = replaceText(templateFile, "{{ page.content }}", contentHtml);

        return templateFile;
    }

    public StringBuilder readFile(String filePath) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null)
        {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        return sb;
    }

    private StringBuilder replaceText(StringBuilder original, String toReplace, String replacement) {
        String modified = original.toString().replace(toReplace, replacement);
        return new StringBuilder(modified);
    }

}
