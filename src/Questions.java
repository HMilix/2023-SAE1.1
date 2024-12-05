import java.util.Arrays;
import java.util.Random;

public class Questions {
    int difficulty;
    String question;
    String[] answers;
    boolean asked;
    boolean math;

    Questions(int difficulty, String question, String[] answers) {
        this.difficulty = difficulty;
        this.question = question;
        this.answers = answers;
        this.asked = false;
        this.math = false;
        if (question.equals("math")) {
            this.math = true;
            refreshMath();
        }
    }

    private static Random random = new Random();

    public void refreshMath() {
            int mathRandom1 = 0;
            int mathRandom2 = 0;
            switch (this.difficulty) {
                case 1:
                    mathRandom1 = 10 + (random.nextInt(1000));
                    mathRandom2 = 10 + (random.nextInt(1000));
                    this.answers = new String[]{(mathRandom1 + mathRandom2) + ""};
                    this.question = mathRandom1 + " + " + mathRandom2 + " = ";
                    break;
                
                case 2:
                    mathRandom1 = 1 + (random.nextInt(20));
                    mathRandom2 = 1 + (random.nextInt(20));
                    this.answers = new String[]{(mathRandom1 * mathRandom2) + ""};
                    this.question = mathRandom1 + " * " + mathRandom2 + " = ";
                    break;

                case 3:
                    do {
                        mathRandom1 = 1 + (random.nextInt(20));
                        mathRandom2 = 1 + (random.nextInt(20));
                    } while (!(mathRandom1 > mathRandom2));
                    this.answers = new String[]{(mathRandom1 % mathRandom2) + ""};
                    this.question = mathRandom1 + " % " + mathRandom2 + " = ";
                    break;
            }
    }

    public boolean ask() {
        if (this.asked) { //Non Random Question Asked?
            return false;
        }
        if (this.math) { //Math Question Refresh
            this.refreshMath();
        }
        System.out.print(this.question + (this.math ? "" : " : "));
        if (!this.math) {
            this.setAsked(true);    
        }
        return true;
    }

    public boolean valid(String answer) {
        if (!(answer.equals(""))) {
            return Arrays.stream(answers).anyMatch(answer.toLowerCase()::equals); //Check if answer is in answers
        }
        return false;
    }

    //Getters
    public int getDifficulty() {
        return difficulty;
    }
    public String getQuestion() {
        return question;
    }
    public String getAnswerString() {
        String result = "";
        for (int i = 0; i < answers.length; i++) {
            result += answers[i] + " "; 
        }
        return result;
    }
    public String[] getAnswers() {
        return answers;
    }
    public boolean getAsked() {
        return this.asked;
    }
    
    //Setters
    public void setAsked(boolean asked) {
        this.asked = asked;
    }
}