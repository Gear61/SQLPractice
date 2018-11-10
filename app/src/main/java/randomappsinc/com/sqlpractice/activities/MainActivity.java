package randomappsinc.com.sqlpractice.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.adapters.QuestionsAdapter;
import randomappsinc.com.sqlpractice.database.DataSource;
import randomappsinc.com.sqlpractice.dialogs.LibraryDialog;
import randomappsinc.com.sqlpractice.utils.Constants;
import randomappsinc.com.sqlpractice.utils.PreferencesManager;
import randomappsinc.com.sqlpractice.utils.ToastUtils;
import randomappsinc.com.sqlpractice.utils.Utils;

public class MainActivity extends StandardActivity
        implements LibraryDialog.Listener, QuestionsAdapter.Listener {

    @BindView(R.id.question_list) RecyclerView questionList;
    @BindString(R.string.question_number) String questionTemplate;

    private QuestionsAdapter questionsAdapter;
    private LibraryDialog libraryDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        PreferencesManager preferencesManager = new PreferencesManager(this);
        if (preferencesManager.isFirstTimeUser()) {
            new MaterialDialog.Builder(this)
                    .title(R.string.welcome)
                    .content(R.string.ask_for_help)
                    .positiveText(android.R.string.yes)
                    .show();
        }

        DataSource dataSource = new DataSource(this);
        dataSource.refreshTables();

        questionsAdapter = new QuestionsAdapter(this, questionTemplate, this);
        questionList.setAdapter(questionsAdapter);

        if (preferencesManager.shouldAskToRate()) {
            showPleaseRateDialog();
        }

        libraryDialog = new LibraryDialog(this, this);
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
                            ToastUtils.showLongToast(MainActivity.this, R.string.play_store_error);
                            return;
                        }
                        startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public void onQuestionClicked(int position) {
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
    public void openWebPage(String helpURL) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(WebActivity.IDEA_KEY, helpURL);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        Utils.loadMenuIcon(menu, R.id.library, IoniconsIcons.ion_ios_bookmarks, this);
        Utils.loadMenuIcon(menu, R.id.settings, IoniconsIcons.ion_android_settings, this);
        Utils.loadMenuIcon(menu, R.id.sandbox_mode, IoniconsIcons.ion_android_desktop, this);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.library:
                libraryDialog.show();
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.sandbox_mode:
                startActivity(new Intent(this, SandboxActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
