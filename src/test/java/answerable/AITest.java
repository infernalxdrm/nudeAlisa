package answerable;

import static org.junit.jupiter.api.Assertions.*;

class AITest {

    @org.junit.jupiter.api.Test
    void respond() {
        answerable ai= new AI().getInstance();
        System.out.println(ai.respond("Hello"));
    }
}