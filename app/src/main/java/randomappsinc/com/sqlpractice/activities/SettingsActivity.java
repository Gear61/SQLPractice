package randomappsinc.com.sqlpractice.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.adapters.SettingsAdapter;
import randomappsinc.com.sqlpractice.utils.Utils;

public class SettingsActivity extends StandardActivity {

    public static final String SUPPORT_EMAIL = "RandomAppsInc61@gmail.com";
    public static final String OTHER_APPS_URL = "https://play.google.com/store/apps/dev?id=9093438553713389916";
    public static final String REPO_URL = "https://github.com/Gear61/SQLPractice";

    private View parent;
    private String feedbackSubject;
    private String sendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        parent = findViewById(R.id.parent);
        ListView settingsOptions = findViewById(R.id.settings_options);
        feedbackSubject = getString(R.string.feedback_subject);
        sendEmail = getString(R.string.send_email);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settingsOptions.setAdapter(new SettingsAdapter(this));
        settingsOptions.setOnItemClickListener((parentView, view, position, id) -> handleItemClick(position));
    }

    public void handleItemClick(int position) {
        Intent intent = null;
        switch (position) {
            case 0:
                String uriText = "mailto:" + SUPPORT_EMAIL + "?subject=" + Uri.encode(feedbackSubject);
                Uri mailUri = Uri.parse(uriText);
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, mailUri);
                sendIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(Intent.createChooser(sendIntent, sendEmail));
                return;
            case 1:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(OTHER_APPS_URL));
                break;
            case 2:
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                if (getPackageManager().queryIntentActivities(intent, 0).isEmpty()) {
                    Utils.showSnackbar(parent, getString(R.string.play_store_error));
                    return;
                }
                break;
            case 3:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(REPO_URL));
                break;
            case 4:
                intent = new Intent(this, DatabaseTablesActivity.class);
                break;
        }
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }
}
