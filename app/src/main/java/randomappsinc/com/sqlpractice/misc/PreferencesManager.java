package randomappsinc.com.sqlpractice.misc;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

public class PreferencesManager {

    private SharedPreferences prefs;

    private static final String FIRST_TIME_KEY = "firstTime";
    private static final String COMPLETED_QUESTIONS_KEY = "completedQuestions";
    private static final String NUM_APP_OPENS_KEY = "numAppOpens";
    private static PreferencesManager instance;

    public static PreferencesManager get() {
        if (instance == null) {
            instance = getSync();
        }
        return instance;
    }

    private static synchronized PreferencesManager getSync() {
        if (instance == null) {
            instance = new PreferencesManager();
        }
        return instance;
    }

    private PreferencesManager() {
        prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
    }

    public boolean getFirstTimeUser() {
        return prefs.getBoolean(FIRST_TIME_KEY, true);
    }

    public void setFirstTimeUser(boolean firstTimeUser) {
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

    public boolean shouldAskToRate() {
        int numAppOpens = prefs.getInt(NUM_APP_OPENS_KEY, 0);
        numAppOpens++;
        prefs.edit().putInt(NUM_APP_OPENS_KEY, numAppOpens).apply();
        return numAppOpens == 5;
    }
}
