package pl.zczb.tools.spigot.events.autoevents.chatquiz;

public class QuizManager {
    private String currentQuestion;
    private String correctAnswer;
    private boolean solved;
    private QuizType currentType;

    public synchronized void startNewQuiz(String question, String answer, QuizType type) {
        this.currentQuestion = question;
        this.correctAnswer = answer.toLowerCase();
        this.solved = false;
        this.currentType = type;
    }

    public synchronized boolean isSolved() {
        return this.solved;
    }

    public synchronized boolean tryAnswer(String answer) {
        if (this.solved) return false;
        if (answer.toLowerCase().equals(this.correctAnswer)) {
            this.solved = true;
            return true;
        }
        return false;
    }

    public synchronized String getCurrentQuestion() {
        return this.currentQuestion;
    }

    public synchronized QuizType getCurrentType() {
        return this.currentType;
    }
}


