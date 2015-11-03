package randomappsinc.com.sqlpractice.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import randomappsinc.com.sqlpractice.Adapters.QueryACAdapter;
import randomappsinc.com.sqlpractice.Database.QuestionServer;
import randomappsinc.com.sqlpractice.Database.SchemaServer;
import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.Utils.Util;

/**
 * Created by alexanderchiou on 10/31/15.
 */
// Loads questions for users to answer
public class QuestionActivity extends AppCompatActivity
{
    final Context context = this;
    SchemaServer m_SS = SchemaServer.getSchemaServer();
    QuestionServer m_QS = QuestionServer.getQuestionServer();
    int currentQuestion;
    String queryPreSet;

    // Question form views
    TextView questionNumber;
    TextView tableDesign;
    TextView questionPrompt;
    AutoCompleteTextView queryHelper;

    // Menu items, don't want to find multiple times

    public boolean killKeyboard()
    {
        View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        Rect r = new Rect();
        view.getWindowVisibleDisplayFrame(r);

        int heightDiff = view.getRootView().getHeight() - (r.bottom - r.top);
        if (heightDiff > 100)
        {
            InputMethodManager inputManager = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_form);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        currentQuestion = intent.getIntExtra("QUESTION_NUM", 0);

        questionNumber = (TextView) findViewById(R.id.question_number);
        tableDesign = (TextView) findViewById(R.id.table_design);
        questionPrompt = (TextView) findViewById(R.id.problem);
        queryHelper = (AutoCompleteTextView) findViewById(R.id.query_entry);
        queryPreSet = intent.getStringExtra("USER_QUERY");

        setUpQuestion();
    }

    public void checkAnswer(View view)
    {
        if (Util.validSELECT(queryHelper.getText().toString()))
        {
            Intent intent = new Intent(context, AnswerCheckerActivity.class);
            intent.putExtra("QUESTION_NUM", currentQuestion);
            intent.putExtra("USER_QUERY", queryHelper.getText().toString());
            startActivityForResult(intent, 1);
        }
        else
        {
            Util.showDialog("Please enter a SELECT statement.", context, "");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            advanceQuestionFoward();
        }
    }

    // Sets up a question given the number
    private void setUpQuestion()
    {
        // Set up simple title
        questionNumber.setText("Question #" + String.valueOf(currentQuestion + 1));

        // Get descriptions of the tables we're supposed to use.
        String tableDescriptions = "";
        int[] relevantTables = m_QS.getQuestion(currentQuestion).giveNeededTables();
        for (int i = 0; i < relevantTables.length; i++)
        {
            if (i != 0)
            {
                tableDescriptions += "\n\n";
            }
            tableDescriptions += m_SS.serveTable(relevantTables[i]).description();
        }
        tableDesign.setText(tableDescriptions);

        // Load the problem
        questionPrompt.setText(m_QS.getQuestion(currentQuestion).giveQuestionText());

        // Set up Auto Complete
        QueryACAdapter adapter = new QueryACAdapter(context, android.R.layout.simple_dropdown_item_1line,
                m_SS.serveSomeTables(relevantTables), queryHelper);
        queryHelper.setAdapter(adapter);
        if (queryPreSet != null)
            queryHelper.setText(queryPreSet);
    }

    public void advanceQuestionFoward() {
        int numQuestions = QuestionServer.getNumQuestions();
        currentQuestion++;
        if (currentQuestion == numQuestions)
        {
            currentQuestion = 0;
        }
        setUpQuestion();
        queryHelper.setText("");
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
        killKeyboard();
        int numQuestions = QuestionServer.getNumQuestions();
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.backward:
                currentQuestion--;
                if (currentQuestion < 0)
                {
                    currentQuestion = numQuestions - 1;
                }
                setUpQuestion();
                queryHelper.setText("");
                return true;
            case R.id.forward:
                advanceQuestionFoward();
                return true;
            default:
                break;
        }
        invalidateOptionsMenu();
        return super.onOptionsItemSelected(item);
    }
}