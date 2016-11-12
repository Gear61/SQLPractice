package randomappsinc.com.sqlpractice.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import randomappsinc.com.sqlpractice.Adapters.LessonsAdapter;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 11/12/16.
 */

public class LibraryActivity extends StandardActivity {
    @Bind(R.id.lessons_list) ListView lessons;

    private LessonsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new LessonsAdapter(this);
        lessons.setAdapter(adapter);
    }

    @OnItemClick(R.id.lessons_list)
    public void onLessonClicked(int position) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(WebActivity.IDEA_KEY, adapter.getItem(position));
        startActivity(intent);
    }
}
