package randomappsinc.com.sqlpractice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import randomappsinc.com.sqlpractice.R;

public class SettingsAdapter extends BaseAdapter {

    private final String[] optionNames;
    private final String[] optionIcons;

    public SettingsAdapter(Context context) {
        this.optionNames = context.getResources().getStringArray(R.array.settings_options);
        this.optionIcons = context.getResources().getStringArray(R.array.settings_icons);
    }

    @Override
    public int getCount() {
        return optionNames.length;
    }

    @Override
    public String getItem(int position) {
        return optionNames[position];
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    static class SettingsViewHolder {
        IconTextView settingsIcon;
        TextView settingsOption;

        SettingsViewHolder(View view) {
            settingsIcon = view.findViewById(R.id.option_icon);
            settingsOption = view.findViewById(R.id.settings_option);
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        SettingsViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) parent
                    .getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.settings_list_item, parent, false);
            holder = new SettingsViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (SettingsViewHolder) view.getTag();
        }

        String tabName = optionNames[position];
        holder.settingsOption.setText(tabName);
        holder.settingsIcon.setText(optionIcons[position]);

        return view;
    }
}
