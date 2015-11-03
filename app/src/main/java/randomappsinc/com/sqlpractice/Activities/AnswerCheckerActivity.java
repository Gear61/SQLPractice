package randomappsinc.com.sqlpractice.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import randomappsinc.com.sqlpractice.Database.AnswerChecker;
import randomappsinc.com.sqlpractice.Database.AnswerServer;
import randomappsinc.com.sqlpractice.Database.MisterDataSource;
import randomappsinc.com.sqlpractice.Database.Models.ResponseBundle;
import randomappsinc.com.sqlpractice.Database.QuestionServer;
import randomappsinc.com.sqlpractice.Misc.Constants;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 10/31/15.
 */
// Evaluates the answer that the user gave from QuestionActivity
public class AnswerCheckerActivity extends AppCompatActivity
{
    final Context context = this;
    private int questionNum;
    private MaterialDialog answerDialog;

    @Bind(R.id.verdict) TextView verdict;
    @Bind(R.id.their_answers) TextView theirAnswers;
    @Bind(R.id.advance_forward) Button nextQuestion;
    @Bind(R.id.retry_question) Button retry;
    @Bind(R.id.give_up) Button giveUp;

    @BindString(R.string.correct_answer) String correctAnswer;
    @BindString(R.string.incorrect_answer) String wrongAnswer;
    @BindString(R.string.invalid_query) String invalidQuery;
    @BindString(R.string.empty_resultset) String emptyResults;
    @BindString(R.string.query_results_preamble) String resultsPreamble;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluation);
        ButterKnife.bind(this);

        // Grab relevant data needed to evaluate answers from Question Activity
        Intent intent = getIntent();
        questionNum = intent.getIntExtra(Constants.QUESTION_NUMBER_KEY, 0);
        String userQuery = intent.getStringExtra(Constants.USER_QUERY_KEY);

        // Grab an evaluation of user's answer and display it
        AnswerChecker mrAnswer = new AnswerChecker(context);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayResponse(mrAnswer.checkAnswer(questionNum, userQuery));
        answerDialog = new MaterialDialog.Builder(this)
                .title(R.string.our_answer_query)
                .content(AnswerServer.getAnswer(questionNum))
                .positiveText(android.R.string.yes)
                .build();
    }

    private void displayResponse(ResponseBundle score)
    {
        if (score.getWasCorrect())
        {
            MisterDataSource updateAnswer = new MisterDataSource(context);
            updateAnswer.addAnswer(questionNum);
            verdict.setText(correctAnswer);
            if (questionNum != QuestionServer.getNumQuestions() - 1)
            {
                nextQuestion.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            retry.setVisibility(View.VISIBLE);
            giveUp.setVisibility(View.VISIBLE);
            verdict.setText(wrongAnswer);
        }

        // They got it wrong
        if (score.userResults().getData() == null)
        {
            theirAnswers.setText(invalidQuery);
        }
        else if (score.userResults().getData().length == 0)
        {
            theirAnswers.setText(emptyResults);
        }
        else
        {
            theirAnswers.setText(resultsPreamble);
            // Logic to display their table
            createTable((TableLayout) findViewById(R.id.their_answers_table),
                    score.userResults().getColumns(), score.userResults().getData());
        }
        // Logic to display our answers table
        createTable((TableLayout) findViewById(R.id.correct_answers_table),
                score.correctAnswers().getColumns(), score.correctAnswers().getData());
    }

    public void createTable(TableLayout table, String[] columns, String[][] data)
    {
        TableLayout.LayoutParams params1 = new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f);

        LinearLayout topRow = new LinearLayout(this);
        for (String column : columns)
        {
            TextView text = new TextView(this);
            text.setText(column);
            text.setLayoutParams(params1);
            text.setTypeface(null, Typeface.BOLD);
            topRow.addView(text);
        }
        topRow.setOrientation(LinearLayout.HORIZONTAL);

        // add the TableRow to the TableLayout
        table.addView(topRow);

        for (String[] dataRow : data)
        {
            LinearLayout tuple = new LinearLayout(this);
            for (String datum : dataRow)
            {
                TextView text = new TextView(this);
                text.setText(datum);
                text.setLayoutParams(params1);
                tuple.addView(text);
            }
            tuple.setOrientation(LinearLayout.HORIZONTAL);
            table.addView(tuple);
        }
    }

    @OnClick(R.id.retry_question)
    public void retryQuestion(View view)
    {
        finish();
    }

    @OnClick(R.id.give_up)
    public void giveUp(View view) {
        answerDialog.show();
    }

    @OnClick(R.id.advance_forward)
    public void advanceToNextQuestion(View view)
    {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Make sure that hitting the home/back button brings us back to the question we were working on
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        invalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }
}
