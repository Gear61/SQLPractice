package randomappsinc.com.sqlpractice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.database.QuestionServer;
import randomappsinc.com.sqlpractice.utils.PreferencesManager;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    public interface Listener {
        void onQuestionClicked(int position);
    }

    private String[] questionList = new String[QuestionServer.getNumQuestions()];
    private PreferencesManager preferencesManager;
    private Listener listener;

    public QuestionsAdapter(Context context, String questionTemplate, Listener listener) {
        preferencesManager = new PreferencesManager(context);
        populateList(questionTemplate);
        this.listener = listener;
    }

    // Fills in "Question 1, Question 2, etc..." list
    private void populateList(String questionTemplate) {
        for (int i = 1; i <= QuestionServer.getNumQuestions(); i++) {
            this.questionList[i - 1] = String.format(questionTemplate, i);
        }
    }

    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.question_list_item,
                parent,
                false);
        return new QuestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.loadQuestion(position);
    }

    @Override
    public int getItemCount() {
        return questionList.length;
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.question_number) TextView questionNumber;
        @BindView(R.id.tagged_lessons) TextView taggedLessons;
        @BindView(R.id.completion_icon) TextView completionIcon;

        @BindColor(R.color.green) int green;
        @BindColor(R.color.red) int red;

        QuestionViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void loadQuestion(int position) {
            if (preferencesManager.hasCompletedQuestion(position)) {
                completionIcon.setText(R.string.check_icon);
                completionIcon.setTextColor(green);
            } else {
                completionIcon.setText(R.string.x_icon);
                completionIcon.setTextColor(red);
            }
            questionNumber.setText(questionList[position]);
            taggedLessons.setText(QuestionServer.getQuestionServer().getQuestion(position).getIdeasList());
        }

        @OnClick(R.id.parent)
        void onQuestionClicked() {
            listener.onQuestionClicked(getAdapterPosition());
        }
    }
}
