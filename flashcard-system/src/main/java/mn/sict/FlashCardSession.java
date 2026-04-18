package mn.sict;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlashCardSession {
    private final List<Card> cards;
    private final CardOrganizer organizer;
    private final int repetitions;
    private final boolean invertCards;
    private final Scanner scanner;

    public FlashCardSession(List<Card> cards, CardOrganizer organizer, 
                            int repetitions, boolean invertCards) {
        this.cards = cards; 
        this.organizer = organizer;
        this.repetitions = repetitions;
        this.invertCards = invertCards;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        int[] correctCounts = new int[cards.size()];
        List<Card> remaining = new ArrayList<>(cards);

        System.out.println("\n--- Session Started: " + cards.size() + " cards ---");

        while (!remaining.isEmpty()) {
            List<Card> ordered = organizer.organize(remaining);

            for (Card card : ordered) {
                String q = invertCards ? card.getAnswer() : card.getQuestion();
                String a = invertCards ? card.getQuestion() : card.getAnswer();

                System.out.println("\nQuestion: " + q);
                System.out.print("Your Answer: ");
                String input = scanner.nextLine().trim();

                int idx = cards.indexOf(card);
                if (card.checkAnswer(input)) {
                    System.out.println(" Correct!");
                    correctCounts[idx]++;
                } else {
                    System.out.println("Wrong! The correct answer was: " + a);
                    correctCounts[idx] = 0;
                }
            }
            remaining.removeIf(c -> correctCounts[cards.indexOf(c)] >= repetitions);
        }
        System.out.println("\nCongratulations! You have mastered this deck.");
    }
}