package randomappsinc.com.sqlpractice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

    // Creates the "Question 1, Question 2, etc..." list
    public QuestionAdapter(Context context)
    {
        this.context = context;
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
        public ImageView item1;
        public TextView item2;
    }

    // Renders the ListView item that the user has scrolled to or is about to scroll to
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        final ViewHolder holder;
        if (v == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.question_item, null);
            holder = new ViewHolder();
            holder.item1 = (ImageView) v.findViewById(R.id.completion_status);
            holder.item2 = (TextView) v.findViewById(R.id.question_number);
            v.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)v.getTag();
        }

        final String question = questionList[position];
        if (question != null)
        {
            MisterDataSource theJudge = new MisterDataSource(context);
            if (theJudge.checkAnswer(position))
                holder.item1.setImageResource(R.drawable.check_mark);
            else
                holder.item1.setImageResource(R.drawable.x_mark_red);

            // Load in "Question X"
            holder.item2.setText(question);
        }
        return v;
    }
}
