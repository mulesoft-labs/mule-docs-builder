/**
 * Created by sean.osterberg on 2/20/15.
 */
public class TestUtilities {

    public static String getTestResourcesPath() {
        return System.getProperty("user.dir") + "/src/test/resources/";
    }

    public static String getPathToMasterFolder() {
        return getTestResourcesPath() + "master-folder";
    }
}
