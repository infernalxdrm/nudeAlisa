package answerable;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

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

    @Test
    void sort() {
        String s = "Ellison, Kate. The butterfly clues, New York: Egmont USA, 2012, print.\n" +
                "Collins, Suzanne. Catching fire, New York: Scholastic, 2013, print.\n" +
                "Anthony, Jessica. Chopsticks, New York: Razorbill, 2012 print.\n" +
                "King, Stephen. Christine, New York: New American Library, 1983, print.\n" +
                "Clare, Cassandra. City of ashes, New York: Simon Pulse, 2009, print.\n" +
                "Eliot, T.S Collected poems, New York: Harcourt, 1963, print.\n" +
                "Walker, Alice. The color purple, Orlando: Harcourt, Florida, 2003, print.\n" +
                "Dickinson, Emily. The complete poems of Emily Dickinson, Boston, Massachusetts: Little," +
                "Brown, 1997, print.\n" +
                "Van Draanen, Wendelin. Confessions of a serial kisser, New York: Alfred A. Knopf, 2008, print.\n" +
                "Dostoyevsky, Fyodor. Crime and punishment, New York: Bantam Classic, 2003, print.";
        ArrayList list = new ArrayList<String>(Arrays.asList(s.split("\n")));
        Collections.sort(list, new Comparator<String>() {
            public int compare(String e1, String e2) {
                int c = new Character(e1.charAt(0)).compareTo(e2.charAt(0));
                if (c != 0)
                    return c;
                return new Character(e2.charAt(e2.length() - 1)).compareTo(e1.charAt(e1.length() - 1));
            }
        });
        list.forEach(System.out::println);


    }
}