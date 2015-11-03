package randomappsinc.com.sqlpractice.Misc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

/**
 * Created by alexanderchiou on 10/31/15.
 */
public class Util
{
    public static void showDialog(String message, Context context, String title)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // Set dialog message
        alertDialogBuilder.setMessage(Html.fromHtml(message)).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });

        // Create alert dialog and show it
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(title);
        alertDialog.show();
    }

    public static void showSnackbar(View parent, String content, int backgroundColor, int textColor) {
        Snackbar snackbar = Snackbar.make(parent, content, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        view.setBackgroundColor(backgroundColor);
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(textColor);
        snackbar.show();
    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @SuppressLint("DefaultLocale")
    public static boolean validSELECT(String query)
    {
        return query.trim().toLowerCase().startsWith("select");
    }
}