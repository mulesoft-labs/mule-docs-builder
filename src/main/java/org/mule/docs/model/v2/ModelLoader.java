package org.mule.docs.model.v2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mulesoft.
 */
public class ModelLoader {

    public void load(File sourceDirectory, File outputDirectory){
        Site site = new Site();
        List<Section> sections = new ArrayList<Section>();
        if (sourceDirectory.isDirectory()) {
            for (File file : sourceDirectory.listFiles()) {
                if (isValidSectionDirectory(file)) {
                    Section section = new Section();
                    org.mule.docs.model.Section.fromDirectory(file);
                    sections.add(section);
                }
            }
        }
        site.setSections(sections);
    }

    private boolean isValidSectionDirectory(File directory) {
        if (!directory.isDirectory() ||
                directory.getName().startsWith("_") ||
                directory.getName().contentEquals(".DS_Store")) {
            return false;
        }
        return true;
    }
}
