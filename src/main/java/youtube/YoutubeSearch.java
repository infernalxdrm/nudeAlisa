package youtube;

import java.io.IOException;

public class YoutubeSearch {
    static void searchByTitle(String title) throws IOException {
        utils.httpUtil.getHtmlStream("https://www.youtube.com/results?search_query=" + title.replaceAll(" ", "%")
        ).forEach(System.out::println);
    }


}