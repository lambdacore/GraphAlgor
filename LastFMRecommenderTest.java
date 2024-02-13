// Jonathan Harrington A4 - Algo Graphs - 06/16/2023
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

// task 8 make test cases - try jupiter.api?
public class LastFMRecommenderTest {
    // create objects
    private LastFMRecommender recommender;

    // contructor
    @BeforeEach
    public void setUp() throws IOException {
        recommender = new LastFMRecommender("artists.dat", "user_artists.dat", "user_friends.dat");
    }

    @Test
    public void testListFriends() {

        // Capture standard output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        recommender.listFriends(2);

        // expected output
        String expectedOutput = "Friends of user 2:\n" +
                "275\n" +
                "428\n" +
                "515\n" +
                "761\n" +
                "831\n" +
                "909\n" +
                "1209\n" +
                "1210\n" +
                "1230\n" +
                "1327\n" +
                "1585\n" +
                "1625\n" +
                "1869\n";
        assertEquals(expectedOutput, outContent.toString());

        // Reset standard output
        System.setOut(System.out);
    }

    @Test
    public void testCommonFriends_None() {
        // Capture standard output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        recommender.commonFriends(2, 3);

        // Verify the output (should be an empty list)
        String expectedOutput = "Common friends of user 2 and user 3:\n" +
                "None\n";
        assertEquals(expectedOutput, outContent.toString());

        // Reset standard output
        System.setOut(System.out);
    }

    @Test
    public void testCommonFriends_User2_User3() {
        // Capture standard output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        recommender.commonFriends(45, 46);

        // Verify output
        String expectedOutput =
                "Common friends of user 45 and user 46:\n" +
                        "271\n" +
                        "400\n" +
                        "413\n" +
                        "537\n" +
                        "1080\n" +
                        "1253\n";

        assertEquals(expectedOutput, outContent.toString());

        // Reset standard output
        System.setOut(System.out);
    }
    // test to make sure that two users with common artists print them out
    @Test
    public void testListArtists() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // user id 38 and 43
        recommender.listArtists(38, 43);

        // Verify output
        String expectedOutput =
                "Common artists of user 38 and user 43:\n" +
                        "Daft Punk\n" +
                        "Coldplay\n" +
                        "Gorillaz\n" +
                        "Lady Gaga\n" +
                        "Muse\n" +
                        "The Beatles\n" +
                        "The Killers\n" +
                        "Black Eyed Peas\n" +
                        "30 Seconds to Mars\n";

        assertEquals(expectedOutput, outContent.toString());

        // Reset standard output
        System.setOut(System.out);
    }
    // Simply test to make sure that reversing the order does not change anything
    @Test
    public void testListArtists_Reverse() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // user id 38 and 43
        recommender.listArtists(43, 38);

        // Verify output
        String expectedOutput =
                "Common artists of user 43 and user 38:\n" +
                        "Daft Punk\n" +
                        "Coldplay\n" +
                        "Gorillaz\n" +
                        "Lady Gaga\n" +
                        "Muse\n" +
                        "The Beatles\n" +
                        "The Killers\n" +
                        "Black Eyed Peas\n" +
                        "30 Seconds to Mars\n";

        assertEquals(expectedOutput, outContent.toString());

        // Reset the standard output
        System.setOut(System.out);
    }
    // test case if the two users do not have any common artist
    @Test
    public void testListArtists_None() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // user id 38 and 40
        recommender.listArtists(38, 40);

        // Verify output
        String expectedOutput =
                "Common artists of user 38 and user 40:\n" +
                "None\n";

        assertEquals(expectedOutput, outContent.toString());

        // Reset standard output
        System.setOut(System.out);
    }
    // get the 10 top for a given data given
    // will print the number of
    @Test
    public void testListTop10() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        recommender.listTop10();

        // Verify the output
        String expectedOutput =
                "Top 10 artists:\n" +
                        "Britney Spears: 2393140\n" +
                        "Depeche Mode: 1301308\n" +
                        "Lady Gaga: 1291387\n" +
                        "Christina Aguilera: 1058405\n" +
                        "Paramore: 963449\n" +
                        "Madonna: 921198\n" +
                        "Rihanna: 905423\n" +
                        "Shakira: 688529\n" +
                        "The Beatles: 662116\n" +
                        "Katy Perry: 532545\n";

        assertEquals(expectedOutput, outContent.toString());

        // Reset the standard output
        System.setOut(System.out);    }

    @Test
    public void testRecommend10() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        recommender.recommend10(200);

        // Verify the output
        String expectedOutput =
                "Recommendations for user 200:\n" +
                        "Slayer\n" +
                        "Iron Maiden\n" +
                        "Megadeth\n" +
                        "Annihilator\n" +
                        "Judas Priest\n" +
                        "System of a Down\n" +
                        "Guns N' Roses\n" +
                        "Motörhead\n" +
                        "Black Sabbath\n" +
                        "Deep Purple\n";

        assertEquals(expectedOutput, outContent.toString());

        // Reset the standard output
        System.setOut(System.out);    }
}
