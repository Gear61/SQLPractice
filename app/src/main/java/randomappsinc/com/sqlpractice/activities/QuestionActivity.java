package randomappsinc.com.sqlpractice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;

import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.adapters.QuestionsPagerAdapter;
import randomappsinc.com.sqlpractice.database.QuestionServer;
import randomappsinc.com.sqlpractice.utils.Constants;
import randomappsinc.com.sqlpractice.utils.Utils;

// Loads questions for users to answer
public class QuestionActivity extends StandardActivity {

    private ViewPager questionPager;
    private String questionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_container);

        // Manual binding
        questionPager = findViewById(R.id.question_pager);
        questionNumber = getString(R.string.question_number);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        questionPager.setAdapter(new QuestionsPagerAdapter(getSupportFragmentManager()));

        int initialQuestion = getIntent().getIntExtra(Constants.QUESTION_NUMBER_KEY, 0);
        if (initialQuestion == 0) {
            setTitle(String.format(questionNumber, initialQuestion + 1));
        } else {
            questionPager.setCurrentItem(initialQuestion);
        }

        // Manual listener to replace @OnPageChange
        questionPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setTitle(String.format(questionNumber, position + 1));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            questionPager.setCurrentItem(questionPager.getCurrentItem() + 1, true);
        }
    }

    private void openWebpage(String idea) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(WebActivity.IDEA_KEY, idea);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.question_menu, menu);
        Utils.loadMenuIcon(menu, R.id.random, IoniconsIcons.ion_shuffle, this);
        Utils.loadMenuIcon(menu, R.id.library, IoniconsIcons.ion_information_circled, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utils.hideKeyboard(this);
        int currentPosition = questionPager.getCurrentItem();
        switch (item.getItemId()) {
            case R.id.random:
                int newPosition = Utils.getRandomQuestionIndex(currentPosition);
                questionPager.setCurrentItem(newPosition, true);
                return true;
            case R.id.library:
                new MaterialDialog.Builder(this)
                        .title(R.string.materials_title)
                        .items(QuestionServer.getQuestionServer().getQuestion(currentPosition).getIdeas())
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                openWebpage(text.toString());
                            }
                        })
                        .positiveText(R.string.close)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
