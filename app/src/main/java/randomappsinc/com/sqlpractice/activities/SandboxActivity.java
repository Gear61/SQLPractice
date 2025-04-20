package randomappsinc.com.sqlpractice.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.joanzapata.iconify.fonts.IoniconsIcons;

import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.adapters.QueryAutocompleteAdapter;
import randomappsinc.com.sqlpractice.database.DataSource;
import randomappsinc.com.sqlpractice.database.SchemaServer;
import randomappsinc.com.sqlpractice.database.models.ResultSet;
import randomappsinc.com.sqlpractice.database.models.Schema;
import randomappsinc.com.sqlpractice.dialogs.LibraryDialog;
import randomappsinc.com.sqlpractice.utils.Constants;
import randomappsinc.com.sqlpractice.utils.Utils;

public class SandboxActivity extends StandardActivity implements LibraryDialog.Listener {

    private AutoCompleteTextView queryAutocompleteTextView;
    private View parent;
    private TextView allTablesTextView;
    private String invalidSelect;

    private LibraryDialog libraryDialog;
    private DataSource dataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sandbox);

        // Manual view binding
        queryAutocompleteTextView = findViewById(R.id.query_entry_sandbox);
        parent = findViewById(R.id.parent);
        allTablesTextView = findViewById(R.id.all_tables_description);
        invalidSelect = getString(R.string.invalid_select);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        libraryDialog = new LibraryDialog(this, this);
        dataSource = new DataSource(this);

        populateTableDescriptions();

        // Set up autocomplete
        QueryAutocompleteAdapter adapter = new QueryAutocompleteAdapter(this,
                android.R.layout.simple_dropdown_item_1line,
                SchemaServer.getSchemaServer().serveAllTables(),
                queryAutocompleteTextView);
        queryAutocompleteTextView.setAdapter(adapter);
        queryAutocompleteTextView.setText("");

        // Submit button listener
        findViewById(R.id.submit_query_sandbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitQuery();
            }
        });
    }

    public void submitQuery() {
        Utils.hideKeyboard(this);
        String userQuery = queryAutocompleteTextView.getText().toString().trim();
        ResultSet results = dataSource.getResultsOfQuery(userQuery);
        if (results.getData() != null) {
            Intent intent = new Intent(this, SandboxResultActivity.class);
            intent.putExtra(Constants.USER_QUERY_KEY, userQuery);
            startActivity(intent);
        } else {
            Utils.showLongSnackbar(parent, String.format(invalidSelect, results.getException()));
        }
    }

    @Override
    public void openWebPage(String helpURL) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(WebActivity.IDEA_KEY, helpURL);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
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
        allTablesTextView.setText(tableDescriptions.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sandbox_menu, menu);
        Utils.loadMenuIcon(menu, R.id.library, IoniconsIcons.ion_ios_bookmarks, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.library) {
            libraryDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
