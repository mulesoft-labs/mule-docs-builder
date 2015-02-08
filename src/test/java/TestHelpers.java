import java.util.regex.Pattern;
import java.io.*;

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

    public static String getPathForTestResourcesFile(String[] files) {
        String temp = getTestResourcesPath();
        for(String file : files) {
            if(new File(temp + file).isDirectory()) {
                temp = temp + file + "/";
            } else {
                temp = temp + file;
            }
        }
        return temp;
    }
}
