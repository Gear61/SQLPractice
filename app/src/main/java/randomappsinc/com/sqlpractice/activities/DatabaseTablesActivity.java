package randomappsinc.com.sqlpractice.activities;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.database.models.Schema;
import randomappsinc.com.sqlpractice.database.SchemaServer;
import randomappsinc.com.sqlpractice.R;

public class DatabaseTablesActivity extends StandardActivity {

    @BindView(R.id.all_tables) TextView allTableDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_tables);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
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
