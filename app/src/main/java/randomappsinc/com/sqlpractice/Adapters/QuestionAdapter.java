package randomappsinc.com.sqlpractice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import butterknife.Bind;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.Database.MisterDataSource;
import randomappsinc.com.sqlpractice.Database.QuestionServer;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 10/31/15.
 */
public class QuestionAdapter extends BaseAdapter
{
    private Context context;
    private String[] questionList = new String[QuestionServer.getNumQuestions()];
    private String xIcon;
    private String checkIcon;
    private int red;
    private int green;

    // Creates the "Question 1, Question 2, etc..." list
    public QuestionAdapter(Context context)
    {
        this.context = context;
        this.xIcon = context.getString(R.string.x_icon);
        this.checkIcon = context.getString(R.string.check_icon);
        this.red = context.getResources().getColor(R.color.red);
        this.green = context.getResources().getColor(R.color.green);
        populateList();
    }

    // Fills in "Question 1, Question 2, etc..." list
    private void populateList()
    {
        for (int i = 1; i <= QuestionServer.getNumQuestions(); i++)
        {
            this.questionList[i-1] = "Question " + String.valueOf(i);
        }
    }

    public int getCount()
    {
        return questionList.length;
    }

    public Object getItem(int position)
    {
        return questionList[position];
    }

    public long getItemId(int position)
    {
        return position;
    }

    public static class ViewHolder
    {
        @Bind(R.id.question_number) TextView questionNumber;
        @Bind(R.id.completion_icon) FontAwesomeText completionIcon;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    // Renders the ListView item that the user has scrolled to or is about to scroll to
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.question_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        MisterDataSource theJudge = new MisterDataSource(context);
        if (theJudge.hasUserCompletedQuestion(position)) {
            holder.completionIcon.setIcon(checkIcon);
            holder.completionIcon.setTextColor(green);
        }
        else {
            holder.completionIcon.setIcon(xIcon);
            holder.completionIcon.setTextColor(red);
        }

        // Load in "Question X"
        holder.questionNumber.setText(questionList[position]);
        return view;
    }
}
