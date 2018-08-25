package randomappsinc.com.sqlpractice.utils;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Iconify.with(new IoniconsModule())
               .with(new FontAwesomeModule());
    }
}
