import org.junit.Test;

import java.io.File;

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
    public void htmlForPage_WithValidSection_ReturnsNewInstance() {
        VersionSelector selector = VersionSelector.fromSection(getValidSection());
        String html = selector.htmlForPage(new Page());
        assertTrue(!Utilities.isStringNullOrEmptyOrWhitespace(html));
    }

    public Section getValidSection() {
        File file = new File(Utilities.getConcatPath(new String[]{TestUtilities.getPathToMasterFolder(), "cloudhub"}));
        return Section.fromDirectory(file);
    }
}
