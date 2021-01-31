package answerable;

import org.junit.jupiter.api.Test;

class AITest {

    @org.junit.jupiter.api.Test
    void respond() {
        answerable ai= new AI().getInstance();
        System.out.println(ai.respond("Hello"));
    }

    @Test
    void fixString() {
        System.out.println(AI.fixString("hello  mom:224242424: xdddd:2113131:"));
    }
}