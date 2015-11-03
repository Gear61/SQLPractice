package randomappsinc.com.sqlpractice.Misc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by alexanderchiou on 11/2/15.
 */
public class PreferencesManager
{
    private SharedPreferences prefs;

    private static final String FIRST_TIME_KEY = "firstTime";
    private static PreferencesManager instance;

    public static PreferencesManager get()
    {
        if (instance == null)
        {
            instance = getSync();
        }
        return instance;
    }

    private static synchronized PreferencesManager getSync()
    {
        if (instance == null)
        {
            instance = new PreferencesManager();
        }
        return instance;
    }

    private PreferencesManager()
    {
        Context context = MyApplication.getAppContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean getFirstTimeUser()
    {
        return prefs.getBoolean(FIRST_TIME_KEY, true);
    }

    public void setFirstTimeUser(boolean firstTimeUser)
    {
        prefs.edit().putBoolean(FIRST_TIME_KEY, firstTimeUser).apply();
    }
}
