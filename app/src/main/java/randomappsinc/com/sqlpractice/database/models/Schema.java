package randomappsinc.com.sqlpractice.database.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

// Represents a schema (collection of columns + names)
// Knows how to create itself, populate itself, and give basic information about itself
public class Schema {

    private Column[] columns;
    private String tableName;
    private String[][] rows;
    private StringBuilder insertionTemplate;

    public Schema(String tableName, Column[] columns, String[][] rows) {
        this.columns = columns;
        this.tableName = tableName;
        this.rows = rows;

        // Initialize insertionTemplate base of "INSERT INTO TABLE_NAME (COLUMNS) VALUES )"
        insertionTemplate = new StringBuilder("INSERT INTO " + tableName + " (");
        for (int i = 0; i < columns.length; i++) {
            insertionTemplate.append(columns[i].getRowName());
            if (i != columns.length - 1) {
                insertionTemplate.append(", ");
            }
        }
        insertionTemplate.append(") VALUES (");
    }

    // Tells you its name
    public String getName() {
        return tableName;
    }

    // Returns columns that comprise the table
    public Column[] getColumns() {
        return columns;
    }

    // Returns how many rows this schema contains. This is what the number should always be
    public int numRows()
    {
        return rows.length;
    }

    public ArrayList<String> createSuggestions() {
        HashSet<String> noDupes = new HashSet<>();
        for (String[] row : rows) {
            Collections.addAll(noDupes, row);
        }
        return new ArrayList<>(noDupes);
    }

    // Returns a string describing itself to the app user
    // Example: SALARIES (Professor_Name (TEXT), Department (TEXT), SALARY (INT))
    public String getDescription() {
        StringBuilder description = new StringBuilder(tableName + " (");
        for (int i = 0; i < columns.length; i++) {
            description.append(columns[i].getRowName());
            description.append(" (");
            description.append(columns[i].getDataType());
            description.append(")");
            if (i != columns.length - 1) {
                description.append(", ");
            }
        }
        description.append(")");
        return description.toString();
    }

    // Returns statement to create this schema in DB
    public String creationStatement() {
        // Database creation sql statements
        StringBuilder creationStatement = new StringBuilder("CREATE TABLE " + tableName + "(");
        for (int i = 0; i < columns.length; i++) {
            if (i == columns.length - 1) {
                creationStatement.append(columns[i].getRowName());
                creationStatement.append(" ");
                creationStatement.append(columns[i].getDataType());
                creationStatement.append(");");
            } else {
                creationStatement.append(columns[i].getRowName());
                creationStatement.append(" ");
                creationStatement.append(columns[i].getDataType());
                creationStatement.append(", ");
            }
        }
        return creationStatement.toString();
    }

    // Returns an array of insertion statements to populate itself
    public String[] insertStatements() {
        if (rows.length == 0) {
            return null;
        }

        // Create array equal to # of rows we have
        String[] inserts = new String[rows.length];

        // For each row...
        for (int i = 0; i < rows.length; i++) {
            // Start with the template "INSERT INTO TABLE_NAME (COLUMNS) VALUES ("
            inserts[i] = insertionTemplate.toString();
            for (int j = 0; j < columns.length; j++) {
                if (columns[j].getDataType().equals("TEXT")) {
                    inserts[i] += "\"";
                }
                inserts[i] += rows[i][j];
                if (columns[j].getDataType().equals("TEXT")) {
                    inserts[i] += "\"";
                }

                if (j != columns.length - 1) {
                    inserts[i] += ", ";
                }
            }
            inserts[i] += ");";
        }
        return inserts;
    }
}
