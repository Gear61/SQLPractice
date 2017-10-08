package randomappsinc.com.sqlpractice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.misc.TutorialServer;

public class LessonsAdapter extends BaseAdapter {

    private Context context;
    private List<String> lessons;

    public LessonsAdapter(Context context) {
        this.context = context;
        this.lessons = TutorialServer.get().getLessons();
    }

    public int getCount()
    {
        return lessons.size();
    }

    public String getItem(int position) {
        return lessons.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public class LessonViewHolder {
        @BindView(R.id.lesson_name) TextView lessonName;

        public LessonViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadLesson(int position) {
            lessonName.setText(getItem(position));
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        LessonViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.lesson_cell, parent, false);
            holder = new LessonViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (LessonViewHolder) view.getTag();
        }
        holder.loadLesson(position);
        return view;
    }
}
