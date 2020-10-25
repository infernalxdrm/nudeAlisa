package AiDungeon;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class AiDungeonParserTest {

    @Test
    void init() throws IOException {
        AiDungeonParser p=new AiDungeonParser();
        p.init();

        new Scanner(System.in).nextLine();
    }
}