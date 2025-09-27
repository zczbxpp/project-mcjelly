package pl.zczb.tools.spigot.events.autoevents.chatquiz;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.helpers.GlobalHelper;

import java.util.*;

public class QuizTask extends BukkitRunnable {
    private final QuizManager quizManager;
    private final Random random = new Random();

    private final List<String> words = Arrays.asList(new String[]{"łowisko", "komputer", "program", "wieloryb", "traktor", "puzzle", "fizyka", "ogrodnik"});


    private final List<Integer> validResults = Arrays.asList(new Integer[]{
            Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(4), Integer.valueOf(6), Integer.valueOf(10), Integer.valueOf(23), Integer.valueOf(50), Integer.valueOf(100), Integer.valueOf(150), Integer.valueOf(200)
    });

    private boolean mathTurn = true;

    public QuizTask(QuizManager quizManager) {
        this.quizManager = quizManager;
    }


    public void run() {
        if (this.mathTurn) {
            String question = generateMathQuestion();
            String answer = solveMathQuestion(question);
            this.quizManager.startNewQuiz(question, answer, QuizType.MATH);
            Bukkit.broadcastMessage(GlobalHelper.fixColor("&#F3CCFF&lMATEMATYK &8>> &7Rozwiąż zadanie i odpowiedz napisz na chacie: &f" + question));
        } else {
            String word = this.words.get(this.random.nextInt(this.words.size()));
            String anagram = shuffleWord(word);
            this.quizManager.startNewQuiz(anagram, word, QuizType.POLYGLOT);
            Bukkit.broadcastMessage(GlobalHelper.fixColor("&#D5FFCC&lPOLIGLOTA &8>> &7Napisz na chacie poprawnie przekręcone słowo: &f" + anagram));
        }

        this.mathTurn = !this.mathTurn;
    }

    private String generateMathQuestion() {
        int answer = ((Integer) this.validResults.get(this.random.nextInt(this.validResults.size()))).intValue();

        int a = answer * 2;
        int b = 2;

        return String.format("%d / %d", new Object[]{Integer.valueOf(a), Integer.valueOf(b)});
    }

    private String solveMathQuestion(String question) {
        String[] parts = question.split(" / ");
        int a = Integer.parseInt(parts[0]);
        int b = Integer.parseInt(parts[1]);
        return String.valueOf(a / b);
    }

    private String shuffleWord(String word) {
        List<Character> letters = new ArrayList<>();
        for (char c : word.toCharArray()) letters.add(Character.valueOf(c));
        Collections.shuffle(letters);
        StringBuilder sb = new StringBuilder();
        for (Iterator<Character> iterator = letters.iterator(); iterator.hasNext(); ) {
            char c = ((Character) iterator.next()).charValue();
            sb.append(c);
        }
        String shuffled = sb.toString();
        if (shuffled.equals(word)) {
            return shuffleWord(word);
        }
        return shuffled;
    }
}


