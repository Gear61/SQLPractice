package randomappsinc.com.sqlpractice.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import randomappsinc.com.sqlpractice.Database.QuestionServer;
import randomappsinc.com.sqlpractice.Fragments.QuestionFragment;

/**
 * Created by alexanderchiou on 6/19/16.
 */
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
