package parsers.netstalking.generators;

import java.util.Random;

public class screenshootsGenerator implements generator{
    public String generate() {
        String s = "q w e r t y u i o p a s d f g h j k l z x c v b n m";
        String[] chars = s.split(" ");
        int[] nubers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        String line = "https://prnt.sc/";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            if (random.nextInt(2) == 1) {
                builder.append(chars[random.nextInt(chars.length)]);
            } else {
                builder.append(nubers[random.nextInt(nubers.length)]);
            }
        }
        return stringBuilder.append(line).append(builder.toString()).toString();
    }

}
