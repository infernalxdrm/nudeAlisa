package answerable;


import core.services.CleverParser;

import java.io.IOException;
import java.util.Arrays;

public class AI implements answerable {
    CleverParser parser;

    public AI() {
        parser = CleverParser.getInstance();
        try {
            parser.init();
        } catch (IOException e) {
        }
    }

    public static StringBuilder fixString(String message) {
        char[] ap = message.toCharArray();
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < ap.length; i++) {
            if (ap[i] == '<') {
                i++;
                while (i + 1 < ap.length && ap[i] != '>') {
                    i++;
                }

            } else {
                b.append(ap[i]);
            }
        }
        return b;
    }

    @Override
    public String respond(String message) {
        StringBuilder b = fixString(message);
        try {
            String reply = parser.sendAI(b.toString().replaceAll("<", "").replaceAll(">", ""));
                int reboot=0;
                /*while (reply.equals("") || reboot++ < 15){
                    restart();
                    reply=parser.sendAI(message);
                }*/
               if (reply.equals("")){
                   reply = "Im thinking ";
                   restart();
               }
               return reply;
        } catch (IOException e) {
            e.printStackTrace();
            return "Ошибочка вышла" + Arrays.toString(e.getStackTrace());
        }
    }

    @Override
    public void setReply(String message) {

    }

    @Override
    public void restart() {
        try {
            parser.getClient().close();
            parser.init();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public answerable getInstance() {
        return this;
    }
}
