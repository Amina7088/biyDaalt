package mn.sict;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecentMistakesFirstSorter implements CardOrganizer {

    @Override
    public List<Card> organize(List<Card> cards) {
        List<Card> wrongLast = new ArrayList<>();
        List<Card> rest = new ArrayList<>();

        for (Card card : cards) {
            if (card.isLastWrong()) {
                wrongLast.add(card);
            } else {
                rest.add(card);
            }
        }

        Collections.reverse(wrongLast);

        List<Card> result = new ArrayList<>();
        result.addAll(wrongLast);
        result.addAll(rest);
        return result;
    }
}