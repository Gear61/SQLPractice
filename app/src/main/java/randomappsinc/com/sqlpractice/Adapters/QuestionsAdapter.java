package randomappsinc.com.sqlpractice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.Database.QuestionServer;
import randomappsinc.com.sqlpractice.Misc.PreferencesManager;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 10/31/15.
 */
public class QuestionsAdapter extends BaseAdapter {
    private Context context;
    private String[] questionList = new String[QuestionServer.getNumQuestions()];

    // Creates the "Question 1, Question 2, etc..." list
    public QuestionsAdapter(Context context) {
        this.context = context;
        populateList();
    }

    // Fills in "Question 1, Question 2, etc..." list
    private void populateList() {
        for (int i = 1; i <= QuestionServer.getNumQuestions(); i++) {
            this.questionList[i-1] = String.format(context.getString(R.string.question_number), i);
        }
    }

    public int getCount()
    {
        return questionList.length;
    }

    public String getItem(int position)
    {
        return questionList[position];
    }

    public long getItemId(int position)
    {
        return position;
    }

    public class QuestionViewHolder {
        @Bind(R.id.question_number) TextView questionNumber;
        @Bind(R.id.completion_icon) IconTextView completionIcon;

        @BindString(R.string.check_icon) String checkIcon;
        @BindString(R.string.x_icon) String xIcon;

        @BindColor(R.color.green) int green;
        @BindColor(R.color.red) int red;

        public QuestionViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadQuestion(int position) {
            if (PreferencesManager.get().hasCompletedQuestion(position)) {
                completionIcon.setText(checkIcon);
                completionIcon.setTextColor(green);
            } else {
                completionIcon.setText(xIcon);
                completionIcon.setTextColor(red);
            }

            // Load in "Question X"
            questionNumber.setText(getItem(position));
        }
    }

    // Renders the ListView item that the user has scrolled to or is about to scroll to
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
