/**
 * Created by sean.osterberg on 2/22/15.
 */

/**
 * This class needs to check the version of a section and determine
 * if a particular page existed in a previous version. Then, depending
 * on the page, output the version selector HTML contextual to the page.
 */
public class VersionSelector {
    public static VersionSelector fromSection(Section section) {
        return new VersionSelector();
    }

    public String htmlForPage(Page page) {
        return "foo";
    }
}
