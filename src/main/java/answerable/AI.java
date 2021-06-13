package answerable;


import core.services.CleverParser;
import utils.Emoji;

import java.io.IOException;
import java.util.Arrays;

public class AI implements answerable {
    CleverParser parser;
    String reply;
    private boolean isWaiting;

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

    /**
     *
     * @param message actual message ai response to
     * @param lock  monitor that blocks threads of certain guild
     * @return ai response to the message
     */
    @Override
   public String respond(String message,Object lock) {
        StringBuilder b = fixString(message);
        if (b.toString().equals("")) return Emoji.getRandom();
        try {
            synchronized (lock){reply = parser.sendAI(b.toString().replaceAll("<", "").replaceAll(">", ""));}
                int reboot=0;
                /*while (reply.equals("") || reboot++ < 15){
                    restart();
                    reply=parser.sendAI(message);
                }*/
               if (reply.equals("")){
                   reply = "Im thinking... ";
                   new Thread(this::restart).start();
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
    synchronized public void restart() {
        System.out.print("AI module is restarted\n");
        if (reply!=null)return;
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
