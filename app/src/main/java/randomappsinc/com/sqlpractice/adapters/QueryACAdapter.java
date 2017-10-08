package randomappsinc.com.sqlpractice.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
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
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import randomappsinc.com.sqlpractice.database.models.Column;
import randomappsinc.com.sqlpractice.database.models.Schema;
import randomappsinc.com.sqlpractice.R;

// Auto-Complete adapter for the query to make users' lives easier
public class QueryACAdapter extends ArrayAdapter<String> {

    private static final ArrayList<String> CONSTANTS = new ArrayList<>
            (Arrays.asList("SELECT", "FROM", "WHERE", "COUNT", "ORDER BY", "GROUP BY", "MAX",
                    "MIN", "DISTINCT", "DESC", "ASC", "LIMIT", "AND", "OR", "AS", "SUM", "LIKE",
                    "INNER JOIN", "LEFT JOIN", "RIGHT JOIN", "OUTER JOIN", "AVG", "sql", "sqlite_master",
                    "tbl_name", "type", "table", "BETWEEN", "COALESCE", "OFFSET"));

    private Context context;
    private Set<String> itemsAll;
    private AutoCompleteTextView userQuery;
    private String currentInput;
    private Schema[] currentSchemas;

    public QueryACAdapter(Context context, int viewResourceId, Schema[] currentSchemas, AutoCompleteTextView userQuery) {
        super(context, viewResourceId, new ArrayList<String>());
        this.context = context;
        this.userQuery = userQuery;
        this.currentSchemas = currentSchemas;
        this.itemsAll = new HashSet<>();
        this.itemsAll.addAll(CONSTANTS);

        setUpAC();
        setProgressTracker();
        addTableInformation();
        addRowInformation();
    }

    // Add table terms (table name, column names) to suggestions
    private void addTableInformation() {
        for (Schema schema : currentSchemas) {
            itemsAll.add(schema.getName());
            Column[] allCols = schema.getColumns();
            for (Column column : allCols) {
                itemsAll.add(column.getRowName());
            }
        }
    }

    // Adds suggestions from contents of each table.
    // Ain't nobody got time to type out Computer Science or Zaniolo
    private void addRowInformation() {
        for (Schema schema : currentSchemas) {
            itemsAll.addAll(schema.createSuggestions());
        }
    }

    private void setProgressTracker() {
        userQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable arg0) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                currentInput = s.toString();
            }
        });
    }

    // Set it up so that selecting an item doesn't erase everything
    private void setUpAC() {
        userQuery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) throws IllegalArgumentException, IllegalStateException {
                String[] pieces = currentInput.split(" ");
                String autoFill = "";
                if (pieces[pieces.length - 1].contains("(")) {
                    autoFill += pieces[pieces.length - 1].split("\\(")[0] + "(";
                } else if (pieces[pieces.length - 1].contains("\"")) {
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

    public class SuggestionViewHolder {
        @BindView(R.id.suggestion) TextView suggestion;

        public SuggestionViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void loadSuggestion(int position) {
            suggestion.setText(getItem(position));
        }
    }

    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        SuggestionViewHolder holder;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.acquery_item, parent, false);
            holder = new SuggestionViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (SuggestionViewHolder) view.getTag();
        }
        holder.loadSuggestion(position);
        return view;
    }

    @Override
    @NonNull
    public Filter getFilter() {
        return nameFilter;
    }

    @SuppressLint("DefaultLocale")
    private Filter nameFilter = new Filter() {
        public String convertResultToString(Object resultValue) {
            return (resultValue).toString();
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Set<String> newSuggestions = new HashSet<>();
            if (constraint != null) {
                String pieces[] = constraint.toString().split(" ");
                String target = pieces[pieces.length - 1];
                pieces = target.split("\\(");
                target = pieces[pieces.length - 1];
                pieces = target.split("\"");
                target = pieces[pieces.length - 1];
                if (!target.trim().isEmpty()) {
                    for (String potential : itemsAll) {
                        if (potential.toLowerCase().startsWith(target.toLowerCase())
                                && !potential.toLowerCase().equals(target.toLowerCase())) {
                            newSuggestions.add(potential);
                            if (newSuggestions.size() >= 10) {
                                break;
                            }
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = newSuggestions;
                filterResults.count = newSuggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            @SuppressWarnings("unchecked")
            Set<String> filteredList = (Set<String>) results.values;
            if (results.count > 0) {
                clear();
                addAll(filteredList);
                notifyDataSetChanged();
            }
        }
    };
}
