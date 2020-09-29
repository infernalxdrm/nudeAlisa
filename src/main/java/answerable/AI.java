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

    @Override
    public String respond(String message) {
        try {
            return parser.sendAI(message);
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
            parser.init();
        } catch (IOException e) {
        }
    }

    @Override
    public answerable getInstance() {
        return this;
    }
}
