import java.io.File;
import java.util.*;

/**
 * Created by sean.osterberg on 2/22/15.
 */
public class SiteBuilder {

    public void buildSite() {
        List<Section> sections = getSections(new File(""));

    }

    public List<Section> getSections(File masterDirectory) {
        List<Section> sections = new ArrayList<Section>();
        for(File file : masterDirectory.listFiles()) {
            if(isValidSectionDirectory(file)) {
                Section section = Section.fromDirectory(file);
                sections.add(section);
            }
        }
        return sections;
    }



    private boolean isValidSectionDirectory(File directory) {
        if(directory.getName().startsWith("_")) {
            return false;
        }
        return true;
    }
}
