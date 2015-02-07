import java.util.regex.Pattern;

/**
 * Created by sean.osterberg on 2/6/15.
 */
public class TestHelpers {

    public static String getTestResourcesPath() {
        return System.getProperty("user.dir") + "/src/test/test-resources/";
    }

    public static boolean isValidHtml(String input) {
        Pattern htmlPattern = Pattern.compile(".*\\<[^>]+>.*", Pattern.DOTALL);
        return htmlPattern.matcher(input).matches();
    }
}
