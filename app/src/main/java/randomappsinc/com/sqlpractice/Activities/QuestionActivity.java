package randomappsinc.com.sqlpractice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.Adapters.QueryACAdapter;
import randomappsinc.com.sqlpractice.Database.QuestionServer;
import randomappsinc.com.sqlpractice.Database.SchemaServer;
import randomappsinc.com.sqlpractice.Misc.Constants;
import randomappsinc.com.sqlpractice.Misc.Util;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 10/31/15.
 */
// Loads questions for users to answer
public class QuestionActivity extends AppCompatActivity
{
    private SchemaServer schemaServer;
    private QuestionServer questionServer;
    private QueryACAdapter queryACAdapter;
    int currentQuestion;

    // Question form views
    @Bind(R.id.table_design) TextView tableDesign;
    @Bind(R.id.problem) TextView questionPrompt;
    @Bind(R.id.query_entry) AutoCompleteTextView queryHelper;
    @BindString(R.string.question_number) String questionPrefix;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_form);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(questionPrefix + String.valueOf(currentQuestion + 1));
        schemaServer = SchemaServer.getSchemaServer();
        questionServer = QuestionServer.getQuestionServer();

        Intent intent = getIntent();
        currentQuestion = intent.getIntExtra(Constants.QUESTION_NUMBER_KEY, 0);
        setUpQuestion();
    }

    public void checkAnswer(View view) {
        if (Util.validSELECT(queryHelper.getText().toString())) {
            Intent intent = new Intent(this, AnswerCheckerActivity.class);
            intent.putExtra(Constants.QUESTION_NUMBER_KEY, currentQuestion);
            intent.putExtra(Constants.USER_QUERY_KEY, queryHelper.getText().toString());
            startActivityForResult(intent, 1);
        }
        else {
            Util.showDialog("Please enter a SELECT statement.", this, "");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            changeQuestion(1);
        }
    }

    // Sets up a question given the number
    private void setUpQuestion()
    {
        // Get descriptions of the tables we're supposed to use.
        String tableDescriptions = "";
        int[] relevantTables = questionServer.getQuestion(currentQuestion).giveNeededTables();
        for (int i = 0; i < relevantTables.length; i++)
        {
            if (i != 0)
            {
                tableDescriptions += "\n\n";
            }
            tableDescriptions += schemaServer.serveTable(relevantTables[i]).description();
        }
        tableDesign.setText(tableDescriptions);

        // Load the problem
        questionPrompt.setText(questionServer.getQuestion(currentQuestion).giveQuestionText());

        // Set up Auto Complete
        queryACAdapter = new QueryACAdapter(this, android.R.layout.simple_dropdown_item_1line,
                schemaServer.serveSomeTables(relevantTables), queryHelper);
        queryHelper.setAdapter(queryACAdapter);
    }

    public void changeQuestion(int increment) {
        int numQuestions = QuestionServer.getNumQuestions();
        currentQuestion += increment;
        if (currentQuestion == numQuestions)
        {
            currentQuestion = 0;
        }
        else if (currentQuestion < 0)
        {
            currentQuestion = numQuestions - 1;
        }
        setUpQuestion();
        queryHelper.setText("");
        setTitle(questionPrefix + String.valueOf(currentQuestion + 1));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question_menu, menu);
        menu.findItem(R.id.backward).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_arrow_left)
                        .colorRes(R.color.white)
                        .actionBarSize());
        menu.findItem(R.id.forward).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_arrow_right)
                        .colorRes(R.color.white)
                        .actionBarSize());
        return true;
    }

    // Handles menu clicks. Home (back) button goes back to question list, back/forward go through the questions
    public boolean onOptionsItemSelected(MenuItem item)
    {
        Util.hideKeyboard(this);
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                return true;
            case R.id.backward:
                changeQuestion(-1);
                return true;
            case R.id.forward:
                changeQuestion(1);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}