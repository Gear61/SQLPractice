package randomappsinc.com.sqlpractice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.database.QuestionServer;
import randomappsinc.com.sqlpractice.misc.PreferencesManager;
import randomappsinc.com.sqlpractice.R;

public class QuestionsAdapter extends BaseAdapter {

    private Context context;
    private String[] questionList = new String[QuestionServer.getNumQuestions()];

    public QuestionsAdapter(Context context) {
        this.context = context;
        populateList();
    }

    // Fills in "Question 1, Question 2, etc..." list
    private void populateList() {
        for (int i = 1; i <= QuestionServer.getNumQuestions(); i++) {
            this.questionList[i - 1] = String.format(context.getString(R.string.question_number), i);
        }
    }

    public int getCount() {
        return questionList.length;
    }

    public String getItem(int position) {
        return questionList[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public class QuestionViewHolder {
        @BindView(R.id.question_number) TextView questionNumber;
        @BindView(R.id.tagged_lessons) TextView taggedLessons;
        @BindView(R.id.completion_icon) TextView completionIcon;

        @BindColor(R.color.green) int green;
        @BindColor(R.color.red) int red;

        public QuestionViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadQuestion(int position) {
            if (PreferencesManager.get().hasCompletedQuestion(position)) {
                completionIcon.setText(R.string.check_icon);
                completionIcon.setTextColor(green);
            } else {
                completionIcon.setText(R.string.x_icon);
                completionIcon.setTextColor(red);
            }

            questionNumber.setText(getItem(position));
            taggedLessons.setText(QuestionServer.getQuestionServer().getQuestion(position).getIdeasList());
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        QuestionViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.question_list_item, parent, false);
            holder = new QuestionViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (QuestionViewHolder) view.getTag();
        }
        holder.loadQuestion(position);
        return view;
    }
}
