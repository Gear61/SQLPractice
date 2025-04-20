package randomappsinc.com.sqlpractice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import randomappsinc.com.sqlpractice.R;
import randomappsinc.com.sqlpractice.database.QuestionServer;
import randomappsinc.com.sqlpractice.utils.PreferencesManager;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    public interface Listener {
        void onQuestionClicked(int position);
    }

    private final String[] questionList = new String[QuestionServer.getNumQuestions()];
    private final PreferencesManager preferencesManager;
    private final Listener listener;

    public QuestionsAdapter(Context context, String questionTemplate, Listener listener) {
        this.preferencesManager = new PreferencesManager(context);
        populateList(questionTemplate);
        this.listener = listener;
    }

    private void populateList(String questionTemplate) {
        for (int i = 1; i <= QuestionServer.getNumQuestions(); i++) {
            questionList[i - 1] = String.format(questionTemplate, i);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.question_list_item, parent, false);
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

        private final TextView questionNumber;
        private final TextView taggedLessons;
        private final TextView completionIcon;
        private final int green;
        private final int red;

        QuestionViewHolder(View view) {
            super(view);
            questionNumber = view.findViewById(R.id.question_number);
            taggedLessons = view.findViewById(R.id.tagged_lessons);
            completionIcon = view.findViewById(R.id.completion_icon);

            green = ContextCompat.getColor(view.getContext(), R.color.green);
            red = ContextCompat.getColor(view.getContext(), R.color.red);

            view.findViewById(R.id.parent).setOnClickListener(v -> listener.onQuestionClicked(getAdapterPosition()));
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
    }
}
