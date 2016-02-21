package randomappsinc.com.sqlpractice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import randomappsinc.com.sqlpractice.Adapters.QuestionAdapter;
import randomappsinc.com.sqlpractice.Database.MisterDataSource;
import randomappsinc.com.sqlpractice.Misc.Constants;
import randomappsinc.com.sqlpractice.Misc.PreferencesManager;
import randomappsinc.com.sqlpractice.R;

public class MainActivity extends StandardActivity {
    @Bind(R.id.question_list) ListView questionList;

    private QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_list);
        ButterKnife.bind(this);

        if (PreferencesManager.get().getFirstTimeUser()) {
            new MaterialDialog.Builder(this)
                    .title(R.string.welcome)
                    .content(R.string.ask_for_help)
                    .positiveText(android.R.string.yes)
                    .show();
            PreferencesManager.get().setFirstTimeUser(false);
        }

        MisterDataSource m_dataSource = new MisterDataSource();
        m_dataSource.refreshTables();

        questionAdapter = new QuestionAdapter(this);
        questionList.setAdapter(questionAdapter);
    }

    @OnItemClick(R.id.question_list)
    public void onItemClick(int position) {
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra(Constants.QUESTION_NUMBER_KEY, position);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        questionAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.settings).setIcon(
                new IconDrawable(this, FontAwesomeIcons.fa_gear)
                        .colorRes(R.color.white)
                        .actionBarSize());
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}