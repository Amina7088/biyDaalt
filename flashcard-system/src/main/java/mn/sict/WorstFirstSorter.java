package mn.sict;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WorstFirstSorter implements CardOrganizer {
    @Override
    public List<Card> organize(List<Card> cards) {
        cards.sort(new Comparator<Card>() {
            @Override
            public int compare(Card c1, Card c2) {
                return Integer.compare(c1.getCorrectCount(), c2.getCorrectCount());
            }
        });
        return cards;
    }
}