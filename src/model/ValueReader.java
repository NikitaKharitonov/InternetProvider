package model;

public class ValueReader {
    private static String[] words;
    private static int index;

    public static void setString(String string) {
        string = string.replaceAll(",", "");
        string = string.replaceAll("}", "");
        words = string.split(" ");
        index = 0;
    }

    public boolean hasNext() {
        return index < words.length;
    }

    public static String nextValue() {
        return words[index].substring(words[index++].lastIndexOf('=') + 1);
    }
}
