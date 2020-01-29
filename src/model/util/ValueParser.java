package model.util;

public class ValueParser {
    private static String[] words;
    private static int index;
    private static String str;

    public static void setString(String string) {
        string = string.replaceAll(",", " ");
        string = string.replaceAll("}", " ");
        string = string.replaceAll("\\{", " ");
        string = string.replaceAll("\\s+=\\s+", "=");
        str = string;
        words = string.split("\\s+|\\n+|\\t+");
        index = 0;
    }

    public static String getValue(String attribute) {
        attribute = attribute + "=";
        int beginInd = str.lastIndexOf(attribute);
        int endInd = str.lastIndexOf(" ", beginInd);
        return str.substring(beginInd + 1, endInd - 1);
    }

    public boolean hasNext() {
        return index < words.length;
    }

    public static String nextValue() {
        return words[index].substring(words[index++].lastIndexOf('=') + 1);
    }
}
