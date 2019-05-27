package randomappsinc.com.sqlpractice.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

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
