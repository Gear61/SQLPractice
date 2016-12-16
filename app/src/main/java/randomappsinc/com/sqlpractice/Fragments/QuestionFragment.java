package randomappsinc.com.sqlpractice.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import randomappsinc.com.sqlpractice.Activities.AnswerCheckerActivity;
import randomappsinc.com.sqlpractice.Adapters.QueryACAdapter;
import randomappsinc.com.sqlpractice.Database.AnswerServer;
import randomappsinc.com.sqlpractice.Database.QuestionServer;
import randomappsinc.com.sqlpractice.Database.SchemaServer;
import randomappsinc.com.sqlpractice.Misc.Constants;
import randomappsinc.com.sqlpractice.Misc.Utils;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 6/19/16.
 */
public class QuestionFragment extends Fragment {
    public static QuestionFragment create(int position) {
        QuestionFragment questionFragment = new QuestionFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.QUESTION_NUMBER_KEY, position);
        questionFragment.setArguments(bundle);

        return questionFragment;
    }

    // Question form views
    @Bind(R.id.table_design) TextView tableDesign;
    @Bind(R.id.problem) TextView questionPrompt;
    @Bind(R.id.query_entry) AutoCompleteTextView queryHelper;
    @Bind(R.id.parent) View parent;

    @BindString(R.string.invalid_select) String invalidSelect;

    private SchemaServer schemaServer;
    private QuestionServer questionServer;
    private int currentQuestion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.question_form, container, false);
        ButterKnife.bind(this, rootView);

        schemaServer = SchemaServer.getSchemaServer();
        questionServer = QuestionServer.getQuestionServer();

        currentQuestion = getArguments().getInt(Constants.QUESTION_NUMBER_KEY, 0);
        setUpQuestion();

        return rootView;
    }

    // Sets up a question given the number
    private void setUpQuestion() {
        // Get descriptions of the tables we're supposed to use.
        String tableDescriptions = "";
        int[] relevantTables = questionServer.getQuestion(currentQuestion).giveNeededTables();
        for (int i = 0; i < relevantTables.length; i++) {
            if (i != 0) {
                tableDescriptions += "\n\n";
            }
            tableDescriptions += schemaServer.serveTable(relevantTables[i]).getDescription();
        }
        tableDesign.setText(tableDescriptions);

        // Load the problem
        questionPrompt.setText(questionServer.getQuestion(currentQuestion).giveQuestionText());

        // Set up Auto Complete
        QueryACAdapter adapter = new QueryACAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line,
                schemaServer.serveSomeTables(relevantTables), queryHelper);
        queryHelper.setAdapter(adapter);
        queryHelper.setText("");
    }

    @OnClick(R.id.submit_query)
    public void checkAnswer() {
        if (Utils.isValidSelect(queryHelper.getText().toString())) {
            Intent intent = new Intent(getActivity(), AnswerCheckerActivity.class);
            intent.putExtra(Constants.QUESTION_NUMBER_KEY, currentQuestion);
            intent.putExtra(Constants.USER_QUERY_KEY, queryHelper.getText().toString());
            getActivity().startActivityForResult(intent, 1);
        } else {
            Utils.showSnackbar(parent, invalidSelect);
        }
    }

    @OnClick(R.id.view_answer)
    public void giveUp() {
        final String answer = AnswerServer.getAnswer(currentQuestion);
        new MaterialDialog.Builder(getActivity())
                .title(R.string.our_answer_query)
                .content(answer)
                .positiveText(android.R.string.yes)
                .neutralText(R.string.copy_answer)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Utils.copyTextToClipboard(answer);
                        Utils.showSnackbar(parent, getString(R.string.copy_confirmation));
                    }
                })
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
