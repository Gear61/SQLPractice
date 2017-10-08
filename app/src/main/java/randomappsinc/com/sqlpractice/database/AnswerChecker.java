package randomappsinc.com.sqlpractice.database;

import java.util.HashSet;

import randomappsinc.com.sqlpractice.database.models.ResponseBundle;
import randomappsinc.com.sqlpractice.database.models.ResultSet;

public class AnswerChecker {

    private MisterDataSource dataSource;

    public AnswerChecker() {
        dataSource = new MisterDataSource();
    }

    public ResponseBundle checkAnswer(int questionNumber, String userQuery) {
        // Get both results sets, set up wasCorrect. Innocent until proven guilty
        ResultSet userPackage = dataSource.getResultsOfQuery(userQuery);
        ResultSet correctAnswersPackage = dataSource.getResultsOfQuery(AnswerServer.getAnswer(questionNumber));
        String[][] userResult = userPackage.getData();
        String[][] correctAnswers = correctAnswersPackage.getData();
        boolean wasCorrect = true;

        if (userResult == null || userResult.length == 0) {
            wasCorrect = false;
        } else {
            // HashSet to store the rows fetched by the users
            HashSet<String> UserAnswers = new HashSet<>();
            for (int i = 0; i < userResult.length; i++) {
                // Have flying pie delimiters to separate our columns
                String currentRow = "~~|}";
                for (int j = 0; j < userResult[0].length; j++) {
                    currentRow += (userResult[i][j] + "~~|}");
                }
                UserAnswers.add(currentRow);
            }

            // Make sure the result sets are of the same size
            if (userResult.length != correctAnswers.length) {
                wasCorrect = false;
            }

            for (int i = 0; i < correctAnswers.length; i++) {
                String currentRow = "~~|}";
                for (int j = 0; j < correctAnswers[0].length; j++) {
                    currentRow += (correctAnswers[i][j] + "~~|}");
                }
                if (!UserAnswers.contains(currentRow)) {
                    wasCorrect = false;
                }
            }
        }
        return new ResponseBundle(wasCorrect, userPackage, correctAnswersPackage);
    }
}
