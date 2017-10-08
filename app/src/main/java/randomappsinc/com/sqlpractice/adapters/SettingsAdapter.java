package randomappsinc.com.sqlpractice.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.joanzapata.iconify.widget.IconTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.R;

public class SettingsAdapter extends BaseAdapter {

    private String[] optionNames;
    private String[] optionIcons;
    private Context context;

    public SettingsAdapter(Context context) {
        this.context = context;
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

    public static class SettingsViewHolder {
        @BindView(R.id.option_icon) IconTextView settingsIcon;
        @BindView(R.id.settings_option) TextView settingsOption;

        public SettingsViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public View getView(int position, View view, ViewGroup parent) {
        SettingsViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.settings_list_item, parent, false);
            holder = new SettingsViewHolder(view);
            view.setTag(holder);
        }
        else {
            holder = (SettingsViewHolder) view.getTag();
        }

        String tabName = optionNames[position];
        holder.settingsOption.setText(tabName);
        holder.settingsIcon.setText(optionIcons[position]);

        return view;
    }
}
