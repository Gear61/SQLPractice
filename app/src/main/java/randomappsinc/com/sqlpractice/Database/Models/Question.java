package randomappsinc.com.sqlpractice.Database.Models;

/**
 * Created by alexanderchiou on 10/31/15.
 */
// Represents a question.
// Has the question text and an array of integers signifying which tables it's linked to
public class Question {
    private String questionText;
    private int[] neededTables;
    private String[] ideas;

    public Question (String questionText, int[] neededTables, String[] ideas) {
        this.questionText = questionText;
        this.neededTables = neededTables;
        this.ideas = ideas;
    }

    public String giveQuestionText() {
        return questionText;
    }

    public int[] giveNeededTables() {
        return neededTables;
    }

    public String[] getIdeas() {
        return ideas;
    }
}
