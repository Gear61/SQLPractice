package randomappsinc.com.sqlpractice.Database;

import randomappsinc.com.sqlpractice.Database.Models.Question;

/**
 * Created by alexanderchiou on 10/31/15.
 */
// This class contains the questions our app contains
public class QuestionServer
{
    // Our singleton
    public static QuestionServer instance = null;

    // Hooray for strange 0 indexing.
    // Question 1 -> Table 2 = {0, 1}, so the first element of this array would be 1
    private static int[][] questionTablePairings =
            {{1},
                    {1},
                    {1},
                    {1},
                    {1},
                    {1},
                    {1},
                    {1},
                    {1},
                    {1},
                    {1},
                    {1},
                    {2},
                    {2, 3},
                    {2, 3}};

    // Questions stored here in this ghetto hard-coded array
    private static String[] questions =
            {"Write a query that outputs all of the contents of the table.",
                    "Write a query that returns the names of all the professors in the table.",
                    "Write a query that returns all departments in the table (no duplicates).",
                    "Write a query that returns the number of professors whose salary is greater than 150000.",
                    "Write a query that returns all departments and their aggregate salaries (in this column order).",
                    "Write a query that returns the name and salary (in this column order) of the professor with the " +
                            "highest salary.",
                    "Write a query that returns the name and salary (in this column order) of the professor with the " +
                            "highest salary in the \"Computer Science\" department.",
                    "Write a query that returns the name and salaries (in this column order) of the Top 5 highest earning" +
                            " professors.",
                    "Write a query that returns the name and salary (in this column order) of the lowest earning professor.",
                    "Write a query that returns the department Professor \"Zaniolo\" works in.",
                    "Write a query that returns all the professor names that begin with the letter 'C'.",
                    "Write a query that returns the third highest salary in the table.",
                    "Write a query that returns all people who share their last name with somebody else.",
                    "Write a query that returns the first and last name of all people who have checked out a book by Terry Crews.",
                    "Write a query that returns all the books that haven't been checked out."};

    private Question[] allQuestions = new Question[questions.length];

    private QuestionServer ()
    {
        for (int i = 0; i < questions.length; i++)
        {
            allQuestions[i] = new Question(questions[i], questionTablePairings[i]);
        }
    }

    public static QuestionServer getQuestionServer()
    {
        if (instance == null)
        {
            instance = new QuestionServer();
        }
        return instance;
    }

    public static int getNumQuestions()
    {
        return questions.length;
    }

    public Question getQuestion(int position)
    {
        return allQuestions[position];
    }
}
