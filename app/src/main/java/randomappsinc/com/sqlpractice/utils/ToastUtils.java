package randomappsinc.com.sqlpractice.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

public class ToastUtils {

    public static void showLongToast(Context context, @StringRes int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }
}
