package mn.sict;

public class Card {
    private String question;
    private String answer;
    private int totalAttempts = 0;
    private int correctCount = 0;
    private boolean lastWrong = false;

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public boolean checkAnswer(String userInput) {
        this.totalAttempts++;
        if (this.answer.equalsIgnoreCase(userInput.trim())) {
            this.correctCount++;
            this.lastWrong = false;
            return true;
        } else {
            this.lastWrong = true;
            return false;
        }
    }

    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    
    public int getTotalAttempts() { return totalAttempts; }
    public int getCorrectCount() { return correctCount; }
    
    public boolean isLastWrong() { return lastWrong; }
    public void setLastWrong(boolean lastWrong) { this.lastWrong = lastWrong; }

    public double getSuccessRate() {
        if (totalAttempts == 0) return 0.0;
        return (double) correctCount / totalAttempts;
    }
}