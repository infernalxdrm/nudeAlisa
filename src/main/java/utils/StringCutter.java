package utils;

import java.util.LinkedList;
import java.util.List;

public class StringCutter {
    public static List<String> cut(String s, int index) {
        List<String> strings = new LinkedList<>();
        for (int a = 0; a < s.length(); ) {

            String s2 = s.substring(a, (a + index) > s.length() ? s.length() - 1 : a + index);
            if ((a + index) > s.length()) {
                strings.add(s2);
                break;
            }
            String s3 = s2.substring(0, s2.lastIndexOf("\n") > 0 ? s2.lastIndexOf("\n") : index);

            strings.add(s3);

            a += s3.length();


        }

        return strings;
    }
}
