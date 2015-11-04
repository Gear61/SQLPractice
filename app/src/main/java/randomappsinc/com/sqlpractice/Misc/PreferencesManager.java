package randomappsinc.com.sqlpractice.Misc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by alexanderchiou on 11/2/15.
 */
public class PreferencesManager
{
    private SharedPreferences prefs;

    private static final String FIRST_TIME_KEY = "firstTime";
    private static final String COMPLETED_QUESTIONS_KEY = "completedQuestions";
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

    private Set<String> getCompletedQuestions() {
        return prefs.getStringSet(COMPLETED_QUESTIONS_KEY, new HashSet<String>());
    }

    private void setCompletedQuestion(Set<String> completedQuestions) {
        prefs.edit().remove(COMPLETED_QUESTIONS_KEY).apply();
        prefs.edit().putStringSet(COMPLETED_QUESTIONS_KEY, completedQuestions).apply();
    }

    public boolean hasCompletedQuestion(int questionNumber) {
        return getCompletedQuestions().contains(String.valueOf(questionNumber));
    }

    public void addCompletedQuestion(int questionNumber) {
        Set<String> completedQuestions = getCompletedQuestions();
        completedQuestions.add(String.valueOf(questionNumber));
        setCompletedQuestion(completedQuestions);
    }
}
