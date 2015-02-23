import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.instanceOf;
/**
 * Created by sean.osterberg on 2/22/15.
 */
public class VersionSelectorTests {

    @Test
    public void fromSection_WithValidSection_ReturnsNewInstance() {
        VersionSelector selector = VersionSelector.fromSection(getValidSection());
        assertThat(selector, instanceOf(VersionSelector.class));
    }

    @Test
    public void htmlForPage_WithValidSection_ContainsHtml() {
        VersionSelector selector = VersionSelector.fromSection(getValidSection());
        String html = selector.htmlForPage(new Page());
        assertTrue(!Utilities.isStringNullOrEmptyOrWhitespace(html));
    }

    @Test
    public void compareLatestPagesWithVersions_WithValidSection_IsValid() {
        Section section = getValidSection();
        List<PageVersion> versions = new ArrayList<PageVersion>();
        VersionSelector.compareLatestPagesWithVersions(section, versions);
        assertTrue(versions.size() == 2);
    }
    


    public Section getValidSection() {
        File file = new File(Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub"}));
        return Section.fromDirectory(file);
    }
}
