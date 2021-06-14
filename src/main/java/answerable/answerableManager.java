package answerable;


import AiDungeon.AiDungeonParser;
import core.services.CleverParser;
import discord4j.common.util.Snowflake;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class answerableManager {
    private static final short ALIVE_TIME = 10; //10 minutes
    private AI aiMessage;
    private short timeSinceLastCall;
    private static final Map<Snowflake, answerableManager> MANAGERS = new ConcurrentHashMap<>();
    public static answerableManager of(Snowflake id){
        return MANAGERS.computeIfAbsent(id,ignored->new answerableManager(ignored));
    }

    /**
     *
     *creates AImangager and deletes it after 10 minuts of afk to save memory lol
     * @param id key in MANAGERS set
     */
    answerableManager(Snowflake id){
        if (!CleverParser.isCreated())CleverParser.create();
        aiMessage=new AI();
        new Thread(()->{
            while (MANAGERS.containsKey(id)) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timeSinceLastCall++;
                if (timeSinceLastCall>=ALIVE_TIME){
                    MANAGERS.remove(id);
                    System.gc();
                }

            }
        }).start();
    }
    public answerable getAnwerable(){
        timeSinceLastCall=0;//reset the timer when AI is called
        return aiMessage.getInstance();
    }
}
