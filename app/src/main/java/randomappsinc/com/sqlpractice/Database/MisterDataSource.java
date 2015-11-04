package randomappsinc.com.sqlpractice.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import randomappsinc.com.sqlpractice.Database.Models.ResultSet;
import randomappsinc.com.sqlpractice.Database.Models.Schema;

/**
 * Created by alexanderchiou on 10/31/15.
 */
public class MisterDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    SchemaServer schemaServer;

    // Constructor
    public MisterDataSource(Context context)
    {
        dbHelper = new MySQLiteHelper(context);
        schemaServer = SchemaServer.getSchemaServer();
    }

    // Open connection to database
    private void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // Terminate connection to database
    private void close() {
        dbHelper.close();
    }

    // 1. Create any tables that haven't been created yet
    // 2. Check all non-persistent tables to see if they need an update. If so, rebuild them
    // NOTE: This doesn't need an open()/close() pair since it's a collection of other commands
    public void refreshTables()
    {
        // Create any missing tables
        String[] allTables = schemaServer.serveAllTableNames();
        for (int i = 0; i < allTables.length; i++)
        {
            if (!tableExists(allTables[i]))
            {
                createTable(allTables[i]);
            }
        }

        // Get list of tables that can renewed from schema server
        String[] targetTables = schemaServer.serveTableNames();

        // For each of those tables, check its current row count with what it should be
        // Then destroy/rebuild it if those numbers don't match
        for (int i = 0; i < targetTables.length; i++)
        {
            if (getNumRowsInTable(targetTables[i]) !=
                    schemaServer.serveTable(targetTables[i]).numRows())
            {
                clearTable(targetTables[i]);
                repopulateTable(targetTables[i]);
            }
        }
    }

    // Clear a table's contents based on name
    public void clearTable(String tableName)
    {
        open();
        database.delete(tableName, null, null);
        close();
    }

    // Repopulate a table based on name
    public void repopulateTable(String tableName)
    {
        open();
        Schema mrTable = schemaServer.serveTable(tableName);
        String[] Inserts = mrTable.insertStatements();

        if (Inserts != null)
        {
            // Run each of the table's inserts
            for (int j = 0; j < Inserts.length; j++)
            {
                database.execSQL(Inserts[j]);
            }
        }
        close();
    }

    // Creates a table
    public void createTable(String tableName)
    {
        open();

        // Create the table
        database.execSQL(schemaServer.serveTable(tableName).creationStatement());

        // Fill it with data
        repopulateTable(tableName);

        close();
    }

    // Checks to see if the table with the given name exists
    public boolean tableExists(String tableName)
    {
        open();
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if (cursor != null)
        {
            if (cursor.getCount() > 0)
            {
                cursor.close();
                close();
                return true;
            }
        }
        cursor.close();
        close();
        return false;
    }

    // Return the number of tuples in a certain table. This number may be outdated
    public int getNumRowsInTable(String tableName)
    {
        open();
        int numRows = 0;
        String query = "SELECT COUNT(*) FROM " + tableName + ";";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0)
        {
            System.out.println("Table not found.");
        }
        else
        {
            cursor.moveToNext();
            numRows = cursor.getInt(0);
        }
        // This way, the connection to the DB is closed for sure while also freeing the cursor
        cursor.close();
        close();
        return numRows;
    }

    public ResultSet getData(String queryString)
    {
        open();
        try
        {
            Cursor cursor = database.rawQuery(queryString, null);
            int row = cursor.getCount(), col = cursor.getColumnCount();
            String columns[] = new String[col];
            for (int i = 0; i < col; i++)
            {
                columns[i] = cursor.getColumnName(i);
            }

            // If no data was gotten, return null
            if (row == 0)
            {
                String[][] empty = {};
                return new ResultSet(columns, empty);
            }

            String[][] ourData = new String[row][col];
            int[] typeDict = new int[col];

            cursor.moveToNext();
            for (int i = 0; i < col; i++)
            {
                typeDict[i] = cursor.getType(i);
            }
            cursor.moveToPrevious();

            int eye = 0;
            while (cursor.moveToNext())
            {
                for (int i = 0; i < col; i++)
                {
                    switch (typeDict[i])
                    {
                        case Cursor.FIELD_TYPE_STRING:
                            ourData[eye][i] = cursor.getString(i);
                            break;

                        case Cursor.FIELD_TYPE_INTEGER:
                            ourData[eye][i] = String.valueOf(cursor.getInt(i));
                            break;

                        case Cursor.FIELD_TYPE_FLOAT:
                            ourData[eye][i] = String.valueOf(cursor.getFloat(i));
                            break;

                        case Cursor.FIELD_TYPE_NULL:
                            ourData[eye][i] = null;
                            break;

                        case Cursor.FIELD_TYPE_BLOB:
                            break;
                    }
                }
                eye++;
            }
            cursor.close();
            close();
            return new ResultSet(columns, ourData);
        }
        catch (SQLiteException e)
        {
            return new ResultSet(null, null);
        }
    }
}
