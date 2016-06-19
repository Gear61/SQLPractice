package randomappsinc.com.sqlpractice.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import randomappsinc.com.sqlpractice.Adapters.QuestionsAdapter;
import randomappsinc.com.sqlpractice.Database.MisterDataSource;
import randomappsinc.com.sqlpractice.Misc.Constants;
import randomappsinc.com.sqlpractice.Misc.PreferencesManager;
import randomappsinc.com.sqlpractice.Misc.Utils;
import randomappsinc.com.sqlpractice.R;

public class MainActivity extends StandardActivity {
    @Bind(R.id.parent) View parent;
    @Bind(R.id.question_list) ListView questionList;

    private QuestionsAdapter questionsAdapter;

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

        questionsAdapter = new QuestionsAdapter(this);
        questionList.setAdapter(questionsAdapter);

        if (PreferencesManager.get().shouldAskToRate()) {
            showPleaseRateDialog();
        }
    }

    private void showPleaseRateDialog() {
        new MaterialDialog.Builder(this)
                .content(R.string.please_rate)
                .negativeText(R.string.no_im_good)
                .positiveText(R.string.will_rate)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Uri uri =  Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (!(getPackageManager().queryIntentActivities(intent, 0).size() > 0)) {
                            Utils.showSnackbar(parent, getString(R.string.play_store_error));
                            return;
                        }
                        startActivity(intent);
                    }
                })
                .show();
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
        questionsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Utils.loadMenuIcon(menu, R.id.settings, FontAwesomeIcons.fa_gear);
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