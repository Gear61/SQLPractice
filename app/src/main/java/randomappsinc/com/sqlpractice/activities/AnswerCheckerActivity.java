package randomappsinc.com.sqlpractice.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.database.AnswerChecker;
import randomappsinc.com.sqlpractice.database.AnswerServer;
import randomappsinc.com.sqlpractice.database.QuestionServer;
import randomappsinc.com.sqlpractice.database.models.ResponseBundle;
import randomappsinc.com.sqlpractice.misc.Constants;
import randomappsinc.com.sqlpractice.misc.PreferencesManager;
import randomappsinc.com.sqlpractice.misc.Utils;

// Evaluates the answer that the user gave from QuestionActivity
public class AnswerCheckerActivity extends StandardActivity {

    @BindView(R.id.parent) View parent;
    @BindView(R.id.correct_answers_table) TableLayout correctTable;
    @BindView(R.id.their_answers_table) TableLayout theirTable;
    @BindView(R.id.verdict) TextView verdict;
    @BindView(R.id.their_answers) TextView theirAnswers;
    @BindView(R.id.advance_forward) Button nextQuestion;
    @BindView(R.id.retry_question) Button retry;

    private int questionNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_checker);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Grab relevant data needed to evaluate answers from Question Activity
        questionNum = getIntent().getIntExtra(Constants.QUESTION_NUMBER_KEY, 0);
        String userQuery = getIntent().getStringExtra(Constants.USER_QUERY_KEY);

        // Grab an evaluation of user's answer and display it
        AnswerChecker mrAnswer = new AnswerChecker();
        displayResponse(mrAnswer.checkAnswer(questionNum, userQuery));
    }

    private void displayResponse(ResponseBundle score) {
        if (score.getWasCorrect()) {
            PreferencesManager.get().addCompletedQuestion(questionNum);
            verdict.setText(R.string.correct_answer);
            if (questionNum != QuestionServer.getNumQuestions() - 1) {
                nextQuestion.setVisibility(View.VISIBLE);
            }
        } else {
            retry.setVisibility(View.VISIBLE);
            verdict.setText(R.string.incorrect_answer);
        }

        // They got it wrong
        if (score.userResults().getData() == null) {
            theirAnswers.setText(R.string.invalid_query);
        } else if (score.userResults().getData().length == 0) {
            theirAnswers.setText(R.string.empty_resultset);
        } else {
            theirAnswers.setText(R.string.query_results_preamble);
            // Logic to display their table
            createTable(theirTable, score.userResults().getColumns(), score.userResults().getData());
        }
        // Logic to display our answers table
        createTable(correctTable, score.correctAnswers().getColumns(), score.correctAnswers().getData());
    }

    public void createTable(TableLayout table, String[] columns, String[][] data) {
        TableLayout.LayoutParams dataParams = new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f);
        dataParams.setMargins(0, 0, 4, 0);

        LinearLayout topRow = new LinearLayout(this);
        for (String column : columns) {
            TextView text = new TextView(this);
            text.setText(column);
            text.setLayoutParams(dataParams);
            text.setTypeface(null, Typeface.BOLD);
            topRow.addView(text);
        }
        topRow.setOrientation(LinearLayout.HORIZONTAL);

        // add the TableRow to the TableLayout
        table.addView(topRow);

        for (String[] dataRow : data) {
            LinearLayout tuple = new LinearLayout(this);
            for (String datum : dataRow) {
                TextView text = new TextView(this);
                text.setText(datum);
                text.setLayoutParams(dataParams);
                tuple.addView(text);
            }
            tuple.setOrientation(LinearLayout.HORIZONTAL);
            table.addView(tuple);
        }
    }

    @OnClick(R.id.retry_question)
    public void retryQuestion() {
        finish();
    }

    @OnClick(R.id.view_answer)
    public void giveUp() {
        final String answer = AnswerServer.getAnswer(questionNum);
        new MaterialDialog.Builder(this)
                .title(R.string.our_answer_query)
                .content(answer)
                .positiveText(android.R.string.yes)
                .neutralText(R.string.copy_answer)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Utils.copyTextToClipboard(answer);
                        Utils.showSnackbar(parent, getString(R.string.copy_confirmation));
                    }
                })
                .show();
    }

    @OnClick(R.id.advance_forward)
    public void advanceToNextQuestion() {
        setResult(RESULT_OK);
        finish();
    }
}
