package randomappsinc.com.sqlpractice.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.database.DataSource;
import randomappsinc.com.sqlpractice.database.models.ResultSet;
import randomappsinc.com.sqlpractice.utils.Constants;

public class SandboxResultActivity extends StandardActivity {

    private TableLayout resultTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sandbox_result);

        resultTable = findViewById(R.id.sandbox_results_table);
        TextView userQuery = findViewById(R.id.user_query);
        TextView emptyResult = findViewById(R.id.sandbox_empty_results);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String query = getIntent().getStringExtra(Constants.USER_QUERY_KEY);

        userQuery.setText(query);

        DataSource dataSource = new DataSource(this);
        ResultSet results = dataSource.getResultsOfQuery(query);

        if (results.getData() == null || results.getData().length == 0) {
            emptyResult.setVisibility(View.VISIBLE);
        } else {
            resultTable.setVisibility(View.VISIBLE);
            createTable(results.getColumns(), results.getData());
        }
    }

    public void createTable(String[] columns, String[][] data) {
        TableLayout.LayoutParams dataParams = new TableLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                0.1f);
        dataParams.setMargins(0, 0, 4, 0);

        LinearLayout topRow = new LinearLayout(this);
        for (String column : columns) {
            TextView text = new TextView(this);
            text.setText(column);
            text.setLayoutParams(dataParams);
            text.setTypeface(null, Typeface.BOLD);
            topRow.addView(text);
        }
        topRow.setOrientation(LinearLayout.HORIZONTAL);
        resultTable.addView(topRow);

        for (String[] dataRow : data) {
            LinearLayout tuple = new LinearLayout(this);
            for (String datum : dataRow) {
                TextView text = new TextView(this);
                text.setText(datum);
                text.setLayoutParams(dataParams);
                tuple.addView(text);
            }
            tuple.setOrientation(LinearLayout.HORIZONTAL);
            resultTable.addView(tuple);
        }
    }
}
