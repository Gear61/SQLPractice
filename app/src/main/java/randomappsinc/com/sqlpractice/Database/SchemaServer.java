package randomappsinc.com.sqlpractice.Database;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import randomappsinc.com.sqlpractice.Database.Models.Column;
import randomappsinc.com.sqlpractice.Database.Models.Schema;

/**
 * Created by alexanderchiou on 10/31/15.
 */
// Class for serving the tables to be created to the MySQLiteHelper
public class SchemaServer {
    // Our singleton
    public static SchemaServer instance = null;

    // Hardcoded table names + columns here
    // TABLE NAMES
    private String[] tableNames = {"COMPLETION_STATUS", "SALARIES", "CHECKED_OUT", "BOOKS"};

    // PERSISTENT TABLE NAMES (THOSE THAT ARE NEVER "RENEWED")
    private String[] persistentNames = {"COMPLETION_STATUS"};

    // COLUMNS FOR THE TABLES
    private Column[][] tableColumns =
            {
                    {
                            new Column("Question_Number", "TEXT"),
                    },
                    {
                            new Column("Professor_Name", "TEXT"),
                            new Column("Department", "TEXT"),
                            new Column("Salary", "INT")
                    },
                    {
                            new Column("First_Name", "TEXT"),
                            new Column("Last_Name", "TEXT"),
                            new Column("Book_ID", "INT")
                    },
                    {
                            new Column("Book_ID", "INT"),
                            new Column("Book_Name", "TEXT"),
                            new Column("Author", "TEXT")
                    }
            };

    // Array of all our schemas
    private Schema[] allSchemas = new Schema[tableNames.length];

    public static SchemaServer getSchemaServer()
    {
        if (instance == null) {
            instance = new SchemaServer();
        }
        return instance;
    }

    private SchemaServer()
    {
        for (int i = 0; i < tableNames.length; i++)
        {
            allSchemas[i] = new Schema(tableNames[i], tableColumns[i], RowServer.getRows(i));
        }
    }

    // Return a table based on position
    public Schema serveTable(int position) {
        return allSchemas[position];
    }

    // Return a table based on name
    public Schema serveTable(String tableName)
    {
        for (int i = 0; i < tableNames.length; i++)
        {
            if (allSchemas[i].getName().equals(tableName))
            {
                return allSchemas[i];
            }
        }
        return null;
    }

    // Return a subset of tables
    public Schema[] serveSomeTables(int[] targetTables)
    {
        Schema[] targetSubset = new Schema[targetTables.length];
        for (int i = 0; i < targetTables.length; i++)
        {
            targetSubset[i] = allSchemas[targetTables[i]];
        }
        return targetSubset;
    }

    // Creates and serves all tables
    public Schema[] serveAllTables()
    {
        return allSchemas;
    }

    public String[] serveAllTableNames()
    {
        return tableNames;
    }

    // Serve all non-persistent table names (presumably so they can be updated)
    public String[] serveNPTableNames()
    {
        Set<String> TableNamesSet = new HashSet<>(Arrays.asList(tableNames));
        Set<String> persistentNamesSet = new HashSet(Arrays.asList(persistentNames));

        String[] NPTables = {};
        TableNamesSet.removeAll(persistentNamesSet);
        NPTables = TableNamesSet.toArray(NPTables);
        return NPTables;
    }
}
