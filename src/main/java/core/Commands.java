package core;

import java.util.HashMap;

public class Commands {
    static final HashMap<String,Command> commands=new HashMap<>();
    public static void   setup(){
       commands.put("hello", event->event.getMessage().
               getChannel().flatMap(chanel->
               chanel.createMessage("Hello"+event.getMessage().getAuthor().get().getMention())
                       .then()));
        //Add all commands here
    }
}
