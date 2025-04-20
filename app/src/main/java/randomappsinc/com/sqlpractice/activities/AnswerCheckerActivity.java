package randomappsinc.com.sqlpractice.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.database.AnswerChecker;
import randomappsinc.com.sqlpractice.database.AnswerServer;
import randomappsinc.com.sqlpractice.database.QuestionServer;
import randomappsinc.com.sqlpractice.database.models.ResponseBundle;
import randomappsinc.com.sqlpractice.utils.Constants;
import randomappsinc.com.sqlpractice.utils.PreferencesManager;
import randomappsinc.com.sqlpractice.utils.Utils;

public class AnswerCheckerActivity extends StandardActivity {

    private View parent;
    private TableLayout correctTable;
    private TableLayout theirTable;
    private TextView verdict;
    private TextView theirAnswers;
    private TextView nextQuestion;
    private TextView retry;

    private int questionNum;
    private PreferencesManager preferencesManager;
    private MaterialDialog answerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_checker);

        // Manual view binding
        parent = findViewById(R.id.parent);
        correctTable = findViewById(R.id.correct_answers_table);
        theirTable = findViewById(R.id.their_answers_table);
        verdict = findViewById(R.id.verdict);
        theirAnswers = findViewById(R.id.their_answers);
        nextQuestion = findViewById(R.id.advance_forward);
        retry = findViewById(R.id.retry_question);

        // Set click listeners
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryQuestion();
            }
        });

        findViewById(R.id.view_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                giveUp();
            }
        });

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanceToNextQuestion();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferencesManager = new PreferencesManager(this);

        // Grab relevant data needed to evaluate answers from Question Activity
        questionNum = getIntent().getIntExtra(Constants.QUESTION_NUMBER_KEY, 0);

        final String answer = AnswerServer.getAnswer(questionNum);
        answerDialog = new MaterialDialog.Builder(this)
                .title(R.string.our_answer_query)
                .content(answer)
                .positiveText(android.R.string.yes)
                .neutralText(R.string.copy_answer)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Utils.copyTextToClipboard(answer, AnswerCheckerActivity.this);
                        Utils.showSnackbar(parent, getString(R.string.copy_confirmation));
                    }
                })
                .build();

        String userQuery = getIntent().getStringExtra(Constants.USER_QUERY_KEY);

        // Grab an evaluation of user's answer and display it
        AnswerChecker mrAnswer = new AnswerChecker(this);
        displayResponse(mrAnswer.checkAnswer(questionNum, userQuery));
    }

    private void displayResponse(ResponseBundle score) {
        if (score.getWasCorrect()) {
            preferencesManager.addCompletedQuestion(questionNum);
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
            createTable(theirTable, score.userResults().getColumns(), score.userResults().getData());
        }

        // Logic to display our answers table
        createTable(correctTable, score.correctAnswers().getColumns(), score.correctAnswers().getData());
    }

    public void createTable(TableLayout table, String[] columns, String[][] data) {
        TableLayout.LayoutParams dataParams = new TableLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0.1f);
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

    public void retryQuestion() {
        finish();
    }

    public void giveUp() {
        answerDialog.show();
    }

    public void advanceToNextQuestion() {
        setResult(RESULT_OK);
        finish();
    }
}
