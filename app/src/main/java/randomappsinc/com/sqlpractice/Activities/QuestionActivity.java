package randomappsinc.com.sqlpractice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnPageChange;
import randomappsinc.com.sqlpractice.Adapters.QuestionsPagerAdapter;
import randomappsinc.com.sqlpractice.Database.QuestionServer;
import randomappsinc.com.sqlpractice.Misc.Constants;
import randomappsinc.com.sqlpractice.Misc.Utils;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 10/31/15.
 */
// Loads questions for users to answer
public class QuestionActivity extends StandardActivity {
    @Bind(R.id.question_pager) ViewPager questionPager;
    @BindString(R.string.question_number) String questionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_container);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionPager.setAdapter(new QuestionsPagerAdapter(getFragmentManager()));

        int initialQuestion = getIntent().getIntExtra(Constants.QUESTION_NUMBER_KEY, 0);
        questionPager.setCurrentItem(initialQuestion);
    }

    @OnPageChange(R.id.question_pager)
    public void onQuestionChanged() {
        String pageTitle = questionNumber + (String.valueOf(questionPager.getCurrentItem() + 1));
        setTitle(pageTitle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            questionPager.setCurrentItem(questionPager.getCurrentItem() + 1, true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question_menu, menu);
        Utils.loadMenuIcon(menu, R.id.random, FontAwesomeIcons.fa_random);
        Utils.loadMenuIcon(menu, R.id.backward, FontAwesomeIcons.fa_arrow_left);
        Utils.loadMenuIcon(menu, R.id.forward, FontAwesomeIcons.fa_arrow_right);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Utils.hideKeyboard(this);
        int currentPosition = questionPager.getCurrentItem();
        switch (item.getItemId()) {
            case R.id.random:
                int newPosition = Utils.getRandomQuestionIndex(currentPosition);
                questionPager.setCurrentItem(newPosition, true);
                return true;
            case R.id.backward:
                int previousQuestion = currentPosition == 0 ? QuestionServer.getNumQuestions() - 1 : currentPosition - 1;
                questionPager.setCurrentItem(previousQuestion);
                return true;
            case R.id.forward:
                int nextQuestion = currentPosition == QuestionServer.getNumQuestions() - 1 ? 0 : currentPosition + 1;
                questionPager.setCurrentItem(nextQuestion);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}