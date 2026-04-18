package mn.sict;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FlashcardSystemTest {

    // 1. Card классын логик тест
    @Test
    @DisplayName("Card answer validation test")
    void testCardLogic() {
        Card card = new Card("Hello", "Сайн уу");
        assertTrue(card.checkAnswer("Сайн уу"), "Зөв хариултыг таних ёстой");
        assertTrue(card.checkAnswer(" сайн уу "), "Зай авсан ч зөв гэж үзэх ёстой");
        assertFalse(card.checkAnswer("Bye"), "Буруу хариултыг таних ёстой");
    }

    @Test
    @DisplayName("JSON Save and Load test")
    void testFileOperations() {
        CardLoader loader = new CardLoader();
        String testFile = "test_data.json";
        
        Map<String, List<Card>> data = new HashMap<>();
        List<Card> cards = new ArrayList<>();
        cards.add(new Card("Test Q", "Test A"));
        data.put("DemoDeck", cards);

        loader.saveAll(testFile, data);

        Map<String, List<Card>> loadedData = loader.loadAll(testFile);
        
        assertNotNull(loadedData);
        assertTrue(loadedData.containsKey("DemoDeck"));
        assertEquals("Test Q", loadedData.get("DemoDeck").get(0).getQuestion());

        new File(testFile).delete();
    }

    @Test
    @DisplayName("Sorter logic (Random and Worst-First) test")
    void testSorters() {
        RandomSorter randomSorter = new RandomSorter();
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 50; i++) cards.add(new Card("Q" + i, "A" + i));
        
        List<Card> shuffled = randomSorter.organize(new ArrayList<>(cards));
        assertEquals(cards.size(), shuffled.size());
        assertNotEquals(cards, shuffled, "Random sorter should change order");

        WorstFirstSorter worstSorter = new WorstFirstSorter();
        Card easy = new Card("Easy", "E");
        Card hard = new Card("Hard", "H");
        
        for(int i = 0; i < 5; i++) {
            easy.checkAnswer("E");
        }
        
        for(int i = 0; i < 3; i++) {
            hard.checkAnswer("Wrong Answer");
        }
        
        List<Card> testList = new ArrayList<>(Arrays.asList(easy, hard));
        List<Card> sorted = worstSorter.organize(testList);

        assertEquals("Hard", sorted.get(0).getQuestion(), "Worst card (Hard) should be first");
    }
    @Test
        void testRecentMistakesSorter() {
            RecentMistakesFirstSorter sorter = new RecentMistakesFirstSorter();
            Card card = new Card("Q", "A");
            card.checkAnswer("Wrong"); 
            
            List<Card> sorted = sorter.organize(Arrays.asList(new Card("Easy", "E"), card));
            assertEquals("Q", sorted.get(0).getQuestion());
        }
        
}