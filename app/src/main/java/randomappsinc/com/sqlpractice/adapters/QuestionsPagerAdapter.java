package randomappsinc.com.sqlpractice.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import randomappsinc.com.sqlpractice.database.QuestionServer;
import randomappsinc.com.sqlpractice.fragments.QuestionFragment;

public class QuestionsPagerAdapter extends FragmentStatePagerAdapter {

    public QuestionsPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return QuestionFragment.create(position);
    }

    @Override
    public int getCount() {
        return QuestionServer.getNumQuestions();
    }
}
