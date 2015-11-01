package randomappsinc.com.sqlpractice.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import randomappsinc.com.sqlpractice.Adapters.QuestionAdapter;
import randomappsinc.com.sqlpractice.Database.MisterDataSource;
import randomappsinc.com.sqlpractice.R;

public class MainActivity extends AppCompatActivity {
    final Context context = this;
    private QuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        MisterDataSource m_dataSource = new MisterDataSource(context);
        m_dataSource.refreshTables();

        // Populate the list, attach adapter to it
        setContentView(R.layout.question_list);
        final ListView questionList = (ListView) findViewById(R.id.questionList);
        questionAdapter = new QuestionAdapter(context);
        questionList.setAdapter(questionAdapter);
        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
                    throws IllegalArgumentException, IllegalStateException
            {
                Intent intent = new Intent(context, QuestionActivity.class);
                intent.putExtra("QUESTION_NUM", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        questionAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}