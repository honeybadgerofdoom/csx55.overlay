package csx55.overlay.util;

import java.util.List;

public class Helpers {

    public static String getListString(List<String> strList) {
        String lst = "[";
        int index = 0;
        for (String str : strList) {
            lst += str;
            if (index < strList.size() - 1) {
                lst += ", ";
            }
            index++;
        }
        lst += "]";
        return lst;
    }

}