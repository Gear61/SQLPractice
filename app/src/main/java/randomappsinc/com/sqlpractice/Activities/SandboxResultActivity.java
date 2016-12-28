package randomappsinc.com.sqlpractice.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.Database.MisterDataSource;
import randomappsinc.com.sqlpractice.Database.Models.ResultSet;
import randomappsinc.com.sqlpractice.Misc.Constants;
import randomappsinc.com.sqlpractice.R;

/**
 * Created on 12/28/2016.
 */

public class SandboxResultActivity extends StandardActivity {

    @Bind(R.id.result_table_sandbox)    TableLayout mResultTable;
    @Bind(R.id.user_query)    TextView mUserQuery;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sandbox_result);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String query = getIntent().getStringExtra(Constants.USER_QUERY_KEY);

        mUserQuery.setText(query);
        ResultSet results = (new MisterDataSource()).getResultsOfQuery(query);
        createTable((TableLayout) findViewById(R.id.result_table_sandbox),
                results.getColumns(), results.getData());
    }

    public void createTable(TableLayout table, String[] columns, String[][] data) {
        TableLayout.LayoutParams dataParams = new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f);
        dataParams.setMargins(0, 0, 5, 0);

        LinearLayout topRow = new LinearLayout(this);
        for (String column : columns) {
            TextView text = new TextView(this);
            text.setText(column);
            text.setLayoutParams(dataParams);
            text.setTypeface(null, Typeface.BOLD);
            topRow.addView(text);
        }
        topRow.setOrientation(LinearLayout.HORIZONTAL);

        // add the TableRow to the TableLayout
        table.addView(topRow);

        for (String[] dataRow : data) {
            LinearLayout tuple = new LinearLayout(this);
            for (String datum : dataRow) {
                TextView text = new TextView(this);
                text.setText(datum);
                text.setLayoutParams(dataParams);
                tuple.addView(text);
            }
            tuple.setOrientation(LinearLayout.HORIZONTAL);
            table.addView(tuple);
        }
    }
}
