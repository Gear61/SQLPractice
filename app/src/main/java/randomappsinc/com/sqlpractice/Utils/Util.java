package randomappsinc.com.sqlpractice.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;

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

    @SuppressLint("DefaultLocale")
    public static boolean validSELECT(String query)
    {
        if (!(query.split(" ")[0].toLowerCase().equals("select")))
        {
            return false;
        }
        return true;
    }
}