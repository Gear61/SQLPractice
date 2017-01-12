package randomappsinc.com.sqlpractice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.joanzapata.iconify.fonts.IoniconsIcons;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import randomappsinc.com.sqlpractice.Adapters.QueryACAdapter;
import randomappsinc.com.sqlpractice.Database.MisterDataSource;
import randomappsinc.com.sqlpractice.Database.Models.ResultSet;
import randomappsinc.com.sqlpractice.Database.Models.Schema;
import randomappsinc.com.sqlpractice.Database.SchemaServer;
import randomappsinc.com.sqlpractice.Misc.Constants;
import randomappsinc.com.sqlpractice.Misc.TutorialServer;
import randomappsinc.com.sqlpractice.Misc.Utils;
import randomappsinc.com.sqlpractice.R;

/**
 * Created on 12/27/2016.
 */

public class SandboxActivity extends StandardActivity {
    @Bind(R.id.query_entry_sandbox) AutoCompleteTextView mQueryAutocompleteTextView;
    @Bind(R.id.parent) View parent;
    @Bind(R.id.all_tables_description) TextView mAllTablesTextView;
    @BindString(R.string.invalid_select)  String invalidSelect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sandbox);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        ResultSet results = (new MisterDataSource()).getResultsOfQuery(userQuery);
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
        Utils.loadMenuIcon(menu, R.id.instructional_materials, IoniconsIcons.ion_information_circled);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.instructional_materials:
                new MaterialDialog.Builder(this)
                        .title(R.string.materials_title)
                        .items(TutorialServer.get().getLessonsArray())
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                openWebPage(text.toString());
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
