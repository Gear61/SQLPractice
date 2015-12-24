package randomappsinc.com.sqlpractice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 12/24/15.
 */
public class OtherAppsAdapter extends BaseAdapter {
    public static final int[] OTHER_APP_IMAGE_IDS = {R.drawable.math_race, R.drawable.flashcards,
            R.drawable.bernie, R.drawable.name_picker};

    private Context context;
    private String[] appNames;
    private String[] appDescriptions;

    public OtherAppsAdapter(Context context) {
        this.context = context;
        this.appNames = context.getResources().getStringArray(R.array.other_app_names);
        this.appDescriptions = context.getResources().getStringArray(R.array.other_app_descriptions);
    }

    @Override
    public int getCount() {
        return appNames.length;
    }

    @Override
    public String getItem(int position) {
        return appNames[position];
    }

    @Override
    public long getItemId(int position) {
        return appNames[position].hashCode();
    }

    public class OtherAppViewHolder {
        @Bind(R.id.app_icon) ImageView icon;
        @Bind(R.id.app_title) TextView title;
        @Bind(R.id.app_description) TextView description;

        public OtherAppViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        OtherAppViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.other_app_cell, parent, false);
            holder = new OtherAppViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (OtherAppViewHolder) view.getTag();
        }
        holder.icon.setImageResource(OTHER_APP_IMAGE_IDS[position]);
        holder.title.setText(appNames[position]);
        holder.description.setText(appDescriptions[position]);
        return view;
    }
}
