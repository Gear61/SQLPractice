package randomappsinc.com.sqlpractice.Database;

import randomappsinc.com.sqlpractice.Database.Models.Column;
import randomappsinc.com.sqlpractice.Database.Models.Schema;

// Class for serving the tables to be created to the MySQLiteHelper
public class SchemaServer {

    // Our singleton
    private static SchemaServer instance = null;

    // Hardcoded table names + columns here
    // TABLE NAMES
    private String[] tableNames = {"SALARIES", "CHECKED_OUT", "BOOKS"};

    // COLUMNS FOR THE TABLES
    private Column[][] tableColumns =
            {
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

    public static SchemaServer getSchemaServer() {
        if (instance == null) {
            instance = new SchemaServer();
        }
        return instance;
    }

    private SchemaServer() {
        for (int i = 0; i < tableNames.length; i++) {
            allSchemas[i] = new Schema(tableNames[i], tableColumns[i], RowServer.getRows(i));
        }
    }

    // Return a table based on position
    public Schema serveTable(int position) {
        return allSchemas[position];
    }

    // Return a table based on name
    public Schema serveTable(String tableName) {
        for (int i = 0; i < tableNames.length; i++) {
            if (allSchemas[i].getName().equals(tableName)) {
                return allSchemas[i];
            }
        }
        return null;
    }

    // Return a subset of tables
    public Schema[] serveSomeTables(int[] targetTables) {
        Schema[] targetSubset = new Schema[targetTables.length];
        for (int i = 0; i < targetTables.length; i++) {
            targetSubset[i] = allSchemas[targetTables[i]];
        }
        return targetSubset;
    }

    // Creates and serves all tables
    public Schema[] serveAllTables() {
        return allSchemas;
    }

    public String[] serveAllTableNames()
    {
        return tableNames;
    }

    // Serve all non-persistent table names (presumably so they can be updated)
    public String[] serveTableNames()
    {
        return tableNames;
    }
}
