package randomappsinc.com.sqlpractice.Misc;

import android.app.Application;
import android.content.Context;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;

public class MyApplication extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        Iconify.with(new IoniconsModule())
               .with(new FontAwesomeModule());
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }
}