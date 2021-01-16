package utils;

public class htmlToDiscord {
    public static String normalize(String a) {
        return a.replaceAll("<br>", "\n")
                .replaceAll("<em>", "*")
                .replaceAll("</em>", "*")
                .replaceAll("<span class=\"spoiler\">", "||")
                .replaceAll("</span>", "||")
                .replaceAll("&#47;", "/")
                .replaceAll("target=\"_blank\" rel=\"nofollow noopener noreferrer\">", "")
                .replaceAll("<a href=\"", "")
                .replaceAll("</a>", "")
                .replaceAll("<strong>", "**")
                .replaceAll("</strong>", "**")
                .replaceAll("<span class=\"s\">", "||");
    }
}
