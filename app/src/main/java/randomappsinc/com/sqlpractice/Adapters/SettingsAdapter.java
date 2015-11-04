package randomappsinc.com.sqlpractice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 11/3/15.
 */
public class SettingsAdapter extends BaseAdapter {
    private String[] optionNames;
    private Context context;

    @SuppressWarnings("unchecked")
    public SettingsAdapter(Context context)
    {
        this.context = context;
        this.optionNames = context.getResources().getStringArray(R.array.settings_options);
    }

    @Override
    public int getCount()
    {
        return optionNames.length;
    }

    @Override
    public String getItem(int position)
    {
        return optionNames[position];
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    public static class SettingsViewHolder
    {
        @Bind(R.id.option_icon) IconTextView settingsIcon;
        @Bind(R.id.settings_option) TextView settingsOption;

        public SettingsViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        SettingsViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.settings_list_item, parent, false);
            holder = new SettingsViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (SettingsViewHolder) view.getTag();
        }

        String tabName = optionNames[position];
        holder.settingsOption.setText(tabName);

        if (tabName.equals(context.getString(R.string.database_tables)))
        {
            holder.settingsIcon.setText(context.getString(R.string.database_icon));
        }
        else if (tabName.equals(context.getString(R.string.send_feedback)))
        {
            holder.settingsIcon.setText(context.getString(R.string.email_icon));
        }
        else if (tabName.equals(context.getString(R.string.rate_this_app)))
        {
            holder.settingsIcon.setText(context.getString(R.string.rate_icon));
        }
        else if (tabName.equals(context.getString(R.string.source_code)))
        {
            holder.settingsIcon.setText(context.getString(R.string.github_icon));
        }
        return view;
    }
}
