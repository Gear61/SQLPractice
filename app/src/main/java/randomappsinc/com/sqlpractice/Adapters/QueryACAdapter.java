package randomappsinc.com.sqlpractice.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import randomappsinc.com.sqlpractice.Database.Models.Column;
import randomappsinc.com.sqlpractice.Database.Models.Schema;
import randomappsinc.com.sqlpractice.R;

/**
 * Created by alexanderchiou on 10/31/15.
 */
// Auto-Complete adapter for the query to make users' lives easier
public class QueryACAdapter extends ArrayAdapter<String>
{
    private Context context;

    // SQLite constructs
    private static ArrayList<String> items = new ArrayList<String>();

    private ArrayList<String> constants = new ArrayList<String>
            (Arrays.asList("SELECT", "FROM", "WHERE", "COUNT", "ORDER BY", "GROUP BY", "MAX",
                    "MIN", "DISTINCT", "DESC", "ASC", "LIMIT", "AND", "OR", "AS", "SUM", "LIKE",
                    "INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "OUTER JOIN"));

    private ArrayList<String> itemsAll;
    private ArrayList<String> suggestions;
    private AutoCompleteTextView userQuery;
    private String currentInput;
    private Schema[] currentSchemas;

    public QueryACAdapter(Context context, int viewResourceId, Schema[] currentSchemas, AutoCompleteTextView userQuery)
    {
        super(context, viewResourceId, items);
        this.context = context;
        this.userQuery = userQuery;
        this.currentSchemas = currentSchemas;

        setUpAC();
        setProgressTracker();
        addConstants();
        addTableInformation();
        addRowInformation();

        this.itemsAll = (ArrayList<String>) QueryACAdapter.items.clone();
        this.suggestions = new ArrayList<>();
    }

    public String getSuggestion(int position) {
        return suggestions.get(position);
    }

    // Add SQLite keywords (SELECT, FROM, WHERE, etc) to suggestions
    private void addConstants() {
        for (int i = 0; i < constants.size(); i++) {
            items.add(constants.get(i));
        }
    }

    // Add table terms (table name, column names) to suggestions
    private void addTableInformation() {
        for (Schema schema : currentSchemas) {
            items.add(schema.getName());
            Column[] allCols = schema.getColumns();
            for (Column column : allCols) {
                items.add(column.getRowName());
            }
        }
    }

    // Adds suggestions from contents of each table.
    // Ain't nobody got time to type out Computer Science or Zaniolo
    private void addRowInformation()
    {
        for (Schema schema : currentSchemas)
        {
            items.addAll(schema.createSuggestions());
        }
    }

    private void setProgressTracker()
    {
        this.userQuery.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable arg0) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                currentInput = s.toString();
            }
        });
    }

    // Set it up so that selecting an item doesn't erase everything
    private void setUpAC()
    {
        this.userQuery.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
                    throws IllegalArgumentException, IllegalStateException
            {
                String[] pieces = currentInput.split(" ");
                String autoFill = "";
                if (pieces[pieces.length - 1].contains("("))
                {
                    autoFill += pieces[pieces.length - 1].split("\\(")[0] + "(";
                }
                else if (pieces[pieces.length - 1].contains("\""))
                {
                    autoFill += pieces[pieces.length - 1].split("\"")[0] + "\"";
                }
                autoFill += parent.getItemAtPosition(position).toString();
                pieces[pieces.length - 1] = autoFill;
                String longerQuery = "";
                for (String piece : pieces) {
                    longerQuery += piece + " ";
                }
                userQuery.setText(longerQuery);
                userQuery.setSelection(longerQuery.length());
            }
        });
    }

    public static class ViewHolder
    {
        public TextView item1;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        ViewHolder holder;
        if (v == null)
        {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.acquery_item, null);
            holder = new ViewHolder();
            holder.item1 = (TextView) v.findViewById(R.id.suggestion);
            v.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)v.getTag();
        }

        final String item = (items.get(position)).toString();
        if (item != null)
        {
            holder.item1.setText(item);
        }
        return v;
    }

    @Override
    public android.widget.Filter getFilter()
    {
        return nameFilter;
    }

    @SuppressLint("DefaultLocale")
    Filter nameFilter = new Filter()
    {
        public String convertResultToString(Object resultValue)
        {
            String str = (resultValue).toString();
            return str;
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            suggestions.clear();
            if (constraint != null)
            {
                String pieces[] = constraint.toString().split(" ");
                String target = pieces[pieces.length - 1];
                pieces = target.split("\\(");
                target = pieces[pieces.length - 1];
                pieces = target.split("\"");
                target = pieces[pieces.length - 1];
                if (!target.equals(""))
                {
                    // Linear search to populate suggestions
                    for (int i = 0, j = 0; i < itemsAll.size() && j <= 10; i++)
                    {
                        if (itemsAll.get(i).toLowerCase().startsWith((target.toLowerCase()))
                                && !itemsAll.get(i).toLowerCase().equals(target.toLowerCase())
                                && !suggestions.contains(itemsAll.get(i)))
                        {
                            j++;
                            suggestions.add(itemsAll.get(i));
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }
            else
            {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            @SuppressWarnings("unchecked")
            ArrayList<String> filteredList = (ArrayList<String>) results.values;
            if (results != null && results.count > 0)
            {
                clear();
                for (String c : filteredList)
                {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
