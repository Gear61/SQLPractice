package randomappsinc.com.sqlpractice.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.Database.Models.Schema;
import randomappsinc.com.sqlpractice.Database.SchemaServer;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 11/3/15.
 */
public class DatabaseTablesActivity extends AppCompatActivity {
    @Bind(R.id.all_tables) TextView allTableDescriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_tables);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        populateTableDescriptions();
    }

    public void populateTableDescriptions() {
        StringBuilder tableDescriptions = new StringBuilder();
        Schema[] allTables = SchemaServer.getSchemaServer().serveAllTables();
        for (int i = 0; i < allTables.length; i++)
        {
            if (i != 0)
            {
                tableDescriptions.append("\n\n");
            }
            tableDescriptions.append(allTables[i].getDescription());
        }
        allTableDescriptions.setText(tableDescriptions.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.blank_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
