package randomappsinc.com.sqlpractice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.adapters.QueryACAdapter;
import randomappsinc.com.sqlpractice.database.DataSource;
import randomappsinc.com.sqlpractice.database.SchemaServer;
import randomappsinc.com.sqlpractice.database.models.ResultSet;
import randomappsinc.com.sqlpractice.database.models.Schema;
import randomappsinc.com.sqlpractice.utils.Constants;
import randomappsinc.com.sqlpractice.utils.TutorialServer;
import randomappsinc.com.sqlpractice.utils.Utils;

public class SandboxActivity extends StandardActivity {

    @BindView(R.id.query_entry_sandbox) AutoCompleteTextView mQueryAutocompleteTextView;
    @BindView(R.id.parent) View parent;
    @BindView(R.id.all_tables_description) TextView mAllTablesTextView;
    @BindString(R.string.invalid_select)  String invalidSelect;

    private MaterialDialog libraryDialog;
    private DataSource dataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sandbox);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        libraryDialog = new MaterialDialog.Builder(this)
                .title(R.string.library)
                .items(TutorialServer.get().getLessonsArray())
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        openWebPage(text.toString());
                    }
                })
                .positiveText(R.string.close)
                .build();
        dataSource = new DataSource(this);

        populateTableDescriptions();

        // Set up autocomplete
        QueryACAdapter adapter = new QueryACAdapter(this, android.R.layout.simple_dropdown_item_1line,
                SchemaServer.getSchemaServer().serveAllTables(), mQueryAutocompleteTextView);
        mQueryAutocompleteTextView.setAdapter(adapter);
        mQueryAutocompleteTextView.setText("");
    }

    @Override
    protected void onPause() {
        Utils.hideKeyboard(this);
        super.onPause();
    }

    @OnClick(R.id.submit_query_sandbox)
    public void submitQuery() {
        Utils.hideKeyboard(this);
        String userQuery = mQueryAutocompleteTextView.getText().toString().trim();
        ResultSet results = dataSource.getResultsOfQuery(userQuery);
        if (results.getData() != null) {
            Intent intent = new Intent(this, SandboxResultActivity.class);
            intent.putExtra(Constants.USER_QUERY_KEY, userQuery);
            startActivity(intent);
        } else {
            Utils.showLongSnackbar(parent, String.format(invalidSelect, results.getException()));
        }
    }

    private void openWebPage(String helpURL) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(WebActivity.IDEA_KEY, helpURL);
        startActivity(intent);
    }

    public void populateTableDescriptions() {
        StringBuilder tableDescriptions = new StringBuilder();
        Schema[] allTables = SchemaServer.getSchemaServer().serveAllTables();
        for (int i = 0; i < allTables.length; i++) {
            if (i != 0) {
                tableDescriptions.append("\n\n");
            }
            tableDescriptions.append(allTables[i].getDescription());
        }
        mAllTablesTextView.setText(tableDescriptions.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sandbox_menu, menu);
        Utils.loadMenuIcon(menu, R.id.library, IoniconsIcons.ion_ios_bookmarks, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.library:
                libraryDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
