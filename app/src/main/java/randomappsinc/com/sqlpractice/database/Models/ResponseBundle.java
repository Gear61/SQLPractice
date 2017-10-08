package randomappsinc.com.sqlpractice.database.models;

// Contains:
// 1. Boolean indicating whether or not the user got the correct answer
// 2. 2D String array containing the USER'S answers (NULL if bad query)
// 3. 2D String array containing the CORRECT answers
public class ResponseBundle {

    private boolean wasCorrect;
    private ResultSet userResults;
    private ResultSet correctAnswers;

    public ResponseBundle (boolean wasCorrect, ResultSet userResults, ResultSet correctAnswers) {
        this.wasCorrect = wasCorrect;
        this.userResults = userResults;
        this.correctAnswers = correctAnswers;
    }

    public Boolean getWasCorrect() {
        return wasCorrect;
    }

    public ResultSet userResults() {
        return userResults;
    }

    public ResultSet correctAnswers() {
        return correctAnswers;
    }
}
