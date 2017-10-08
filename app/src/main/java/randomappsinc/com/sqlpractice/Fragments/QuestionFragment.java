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

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import randomappsinc.com.sqlpractice.Activities.AnswerCheckerActivity;
import randomappsinc.com.sqlpractice.Adapters.QueryACAdapter;
import randomappsinc.com.sqlpractice.Database.AnswerServer;
import randomappsinc.com.sqlpractice.Database.MisterDataSource;
import randomappsinc.com.sqlpractice.Database.Models.ResultSet;
import randomappsinc.com.sqlpractice.Database.QuestionServer;
import randomappsinc.com.sqlpractice.Database.SchemaServer;
import randomappsinc.com.sqlpractice.Misc.Constants;
import randomappsinc.com.sqlpractice.Misc.Utils;
import randomappsinc.com.sqlpractice.R;

public class QuestionFragment extends Fragment {

    public static QuestionFragment create(int position) {
        QuestionFragment questionFragment = new QuestionFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(Constants.QUESTION_NUMBER_KEY, position);
        questionFragment.setArguments(bundle);

        return questionFragment;
    }

    // Question form views
    @BindView(R.id.table_design) TextView tableDesign;
    @BindView(R.id.problem) TextView questionPrompt;
    @BindView(R.id.query_entry) AutoCompleteTextView queryHelper;
    @BindView(R.id.parent) View parent;

    @BindString(R.string.invalid_select) String invalidSelect;

    private SchemaServer mSchemaServer;
    private QuestionServer mQuestionServer;
    private int mCurrentQuestion;
    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.question_form, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        mSchemaServer = SchemaServer.getSchemaServer();
        mQuestionServer = QuestionServer.getQuestionServer();

        mCurrentQuestion = getArguments().getInt(Constants.QUESTION_NUMBER_KEY, 0);
        setUpQuestion();

        return rootView;
    }

    // Sets up a question given the number
    private void setUpQuestion() {
        // Get descriptions of the tables we're supposed to use.
        String tableDescriptions = "";
        int[] relevantTables = mQuestionServer.getQuestion(mCurrentQuestion).giveNeededTables();
        for (int i = 0; i < relevantTables.length; i++) {
            if (i != 0) {
                tableDescriptions += "\n\n";
            }
            tableDescriptions += mSchemaServer.serveTable(relevantTables[i]).getDescription();
        }
        tableDesign.setText(tableDescriptions);

        // Load the problem
        questionPrompt.setText(mQuestionServer.getQuestion(mCurrentQuestion).giveQuestionText());

        // Set up autocomplete
        QueryACAdapter adapter = new QueryACAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line,
                mSchemaServer.serveSomeTables(relevantTables), queryHelper);
        queryHelper.setAdapter(adapter);
        queryHelper.setText("");
    }

    @OnClick(R.id.submit_query)
    public void checkAnswer() {
        Utils.hideKeyboard(getActivity());
        String userQuery = queryHelper.getText().toString().trim();
        ResultSet results = (new MisterDataSource()).getResultsOfQuery(userQuery);
        if (results.getData() != null) {
            Intent intent = new Intent(getActivity(), AnswerCheckerActivity.class);
            intent.putExtra(Constants.QUESTION_NUMBER_KEY, mCurrentQuestion);
            intent.putExtra(Constants.USER_QUERY_KEY, userQuery);
            getActivity().startActivityForResult(intent, 1);
        } else {
            Utils.showLongSnackbar(parent, String.format(invalidSelect, results.getException()));
        }
    }

    @OnClick(R.id.view_answer)
    public void giveUp() {
        final String answer = AnswerServer.getAnswer(mCurrentQuestion);
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
        mUnbinder.unbind();
    }
}
