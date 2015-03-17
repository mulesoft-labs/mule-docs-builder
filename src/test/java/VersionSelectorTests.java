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
        String html = selector.htmlForPage(AsciiDocPage.fromFile(new File(Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub", "cloudhub.ad"}))));
        assertTrue(!Utilities.isStringNullOrEmptyOrWhitespace(html));
    }

    @Test
    public void compareLatestPagesWithVersions_WithValidSection_IsValid() {
        Section section = getValidSection();
        List<PageVersion> versions = new ArrayList<PageVersion>();
        VersionSelector.compareLatestPagesWithVersions(section, versions);
        assertTrue(versions.size() == 3);
    }

    @Test
    public void compareLatestPagesWithVersions_WithValidSection_ContainsUniqueFileInOtherVersion() {
        Section section = getValidSection();
        List<PageVersion> versions = new ArrayList<PageVersion>();
        VersionSelector.compareLatestPagesWithVersions(section, versions);
        boolean result = false;
        for(PageVersion version : versions) {
            if(version.getBaseName().contentEquals("mule-studio")) {
                result = true;
            }
        }
        assertTrue(result);
    }

    @Test(expected = DocBuildException.class)
    public void compareLatestPagesWithVersions_WithInvalidSection_ThrowsException() {
        Section section = getInvalidSection();
        List<PageVersion> versions = new ArrayList<PageVersion>();
        VersionSelector.compareLatestPagesWithVersions(section, versions);
    }

    @Test
    public void getAllVersionMappingsForSection_WithValidSection_IsValid() {
        Section section = getValidSection();
        List<PageVersion> versions = VersionSelector.getAllVersionMappingsForSection(section);
        assertTrue(versions.size() == 3);
        boolean result = false;
        for(PageVersion version : versions) {
            if(version.getBaseName().contentEquals("mule-studio")) {
                result = true;
            }
        }
        assertTrue(result);
    }

    @Test(expected = DocBuildException.class)
    public void getAllVersionMappingsForSection_WithInvalidSection_ThrowsException() {
        Section section = getInvalidSection();
        List<PageVersion> versions = VersionSelector.getAllVersionMappingsForSection(section);
    }

    public Section getValidSection() {
        File file = new File(Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub"}));
        return Section.fromDirectory(file);
    }

    public Section getInvalidSection() {
        File file = new File(Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "_templates"}));
        return Section.fromDirectory(file);
    }
}
