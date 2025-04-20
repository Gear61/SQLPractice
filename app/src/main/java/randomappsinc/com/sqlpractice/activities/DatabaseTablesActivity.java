package randomappsinc.com.sqlpractice.activities;

import android.os.Bundle;
import android.widget.TextView;

import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.database.SchemaServer;
import randomappsinc.com.sqlpractice.database.models.Schema;

public class DatabaseTablesActivity extends StandardActivity {

    private TextView allTableDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_tables);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Manual binding
        allTableDescriptions = findViewById(R.id.all_tables);

        populateTableDescriptions();
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

        allTableDescriptions.setText(tableDescriptions.toString());
    }
}
