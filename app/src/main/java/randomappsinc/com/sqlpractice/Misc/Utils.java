package randomappsinc.com.sqlpractice.Misc;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import randomappsinc.com.sqlpractice.Database.QuestionServer;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 10/31/15.
 */
public class Utils {
    public static void showSnackbar(View parent, String content) {
        Context context = MyApplication.getAppContext();

        Snackbar snackbar = Snackbar.make(parent, content, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        view.setBackgroundColor(context.getResources().getColor(R.color.app_turquoise));
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(context.getResources().getColor(R.color.white));
        snackbar.show();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean validSELECT(String query) {
        return query.trim().toLowerCase().startsWith("select");
    }

    // For the choose multiple names at once case. We're just generating indices
    public static int getRandomQuestionIndex(int currentQuestionIndex) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < QuestionServer.getNumQuestions(); i++) {
            if (i != currentQuestionIndex) {
                list.add(i);
            }
        }
        Collections.shuffle(list);
        return list.get(0);
    }
}