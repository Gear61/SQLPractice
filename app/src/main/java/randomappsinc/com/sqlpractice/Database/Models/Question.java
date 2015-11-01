package randomappsinc.com.sqlpractice.Database.Models;

/**
 * Created by alexanderchiou on 10/31/15.
 */
// Represents a question.
// Has the question text and an array of integers signifying which tables it's linked to
public class Question
{
    private String questionText;
    private int[] neededTables;

    public Question (String questionText, int[] neededTables)
    {
        this.questionText = questionText;
        this.neededTables = neededTables;
    }

    public String giveQuestionText ()
    {
        return questionText;
    }

    public int[] giveNeededTables ()
    {
        return neededTables;
    }
}
