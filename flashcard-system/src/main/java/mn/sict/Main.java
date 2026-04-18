package mn.sict;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import java.util.*;
import java.util.concurrent.Callable;

@Command(name = "flashcard", mixinStandardHelpOptions = true, 
         version = "flashcard 2.5",
         description = "Interactive Multi-Deck Flashcard System.")
public class Main implements Callable<Integer> {

    @Option(names = {"--repetitions"}, description = "Number of correct answers required", defaultValue = "1")
    private int repetitions;

    @Option(names = {"--invertCards"}, description = "Swap Question and Answer")
    private boolean invertCards;

    @Override
    public Integer call() throws Exception {
        CardLoader loader = new CardLoader();
        Scanner scanner = new Scanner(System.in);
        String filePath = "flashcards.json";

        boolean running = true;
        while (running) {
            // Файлаа үргэлж шинээр уншина (өөрчлөлтийг тухай бүрт нь шинэчлэх)
            Map<String, List<Card>> allDecks = loader.loadAll(filePath);

            System.out.println("\n===================================");
            System.out.println("   FLASHCARD LEARNING SYSTEM");
            System.out.println("===================================");
            System.out.println("1. Study (Practice existing cards)");
            System.out.println("2. Edit/Add (Manage decks and cards)");
            System.out.println("3. Exit");
            System.out.print("Select mode: ");
            
            String choice = scanner.nextLine();
            int mode;
            try {
                mode = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter 1, 2, or 3.");
                continue;
            }

            if (mode == 3) {
                System.out.println("Goodbye! Happy learning! 👋");
                running = false;
                break;
            }

            String selectedDeckName = "";
            if (allDecks.isEmpty() && mode == 1) {
                System.out.println("⚠️ No decks found! Please create a deck first.");
                mode = 2; 
            }

            // Багц сонгох хэсэг
            if (mode == 1 || mode == 2) {
                if (allDecks.isEmpty()) {
                    System.out.print("Enter name for your first deck: ");
                    selectedDeckName = scanner.nextLine();
                    allDecks.put(selectedDeckName, new ArrayList<Card>());
                } else {
                    System.out.println("\n--- Available Decks ---");
                    List<String> deckNames = new ArrayList<String>(allDecks.keySet());
                    for (int i = 0; i < deckNames.size(); i++) {
                        System.out.println((i + 1) + ". " + deckNames.get(i));
                    }
                    System.out.println((deckNames.size() + 1) + ". [Create New Deck]");
                    System.out.println((deckNames.size() + 2) + ". [Back to Main Menu]");

                    System.out.print("Select a deck: ");
                    int deckChoice;
                    try {
                        deckChoice = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    if (deckChoice == deckNames.size() + 2) {
                        continue; 
                    } else if (deckChoice > deckNames.size()) {
                        System.out.print("Enter new deck name: ");
                        selectedDeckName = scanner.nextLine();
                        allDecks.put(selectedDeckName, new ArrayList<Card>());
                        mode = 2; 
                    } else {
                        selectedDeckName = deckNames.get(deckChoice - 1);
                    }
                }

                List<Card> currentCards = allDecks.get(selectedDeckName);

                if (mode == 2) {
                    // --- EDIT / ADD MODE ---
                    boolean adding = true;
                    while (adding) {
                        System.out.print("\nQuestion: ");
                        String q = scanner.nextLine();
                        System.out.print("Answer: ");
                        String a = scanner.nextLine();
                        currentCards.add(new Card(q, a));

                        System.out.print("Add another to '" + selectedDeckName + "'? (y/n): ");
                        if (!scanner.nextLine().equalsIgnoreCase("y")) adding = false;
                    }
                    loader.saveAll(filePath, allDecks);
                    System.out.println("✅ Deck '" + selectedDeckName + "' updated and saved.");

                } else {
                    if (currentCards.isEmpty()) {
                        System.out.println("⚠️ This deck is empty! Add some cards first.");
                    } else {
                        System.out.println("\nChoose Study Order:");
                        System.out.println("1. Random (Mix it up)");
                        System.out.println("2. Worst-first (Focus on your mistakes)");
                        System.out.println("3. Recent-mistakes-first (Focus on latest errors)");
                        System.out.print("Selection: ");
                        
                        String orderChoice = scanner.nextLine();
                        CardOrganizer organizer;
                        
                        switch (orderChoice) {
                            case "2" -> {
                                organizer = new WorstFirstSorter();
                                System.out.println("Mode: Worst-first");
                            }
                            case "3" -> {
                                organizer = new RecentMistakesFirstSorter();
                                System.out.println("Mode: Recent-mistakes-first");
                            }
                            default -> {
                                organizer = new RandomSorter();
                                System.out.println("Mode: Random");
                            }
                        }

                        FlashCardSession session = new FlashCardSession(currentCards, organizer, repetitions, invertCards);
                        session.run();
                        
                        loader.saveAll(filePath, allDecks);
                    }
                }
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}