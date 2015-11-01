package randomappsinc.com.sqlpractice.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import randomappsinc.com.sqlpractice.Database.Models.Schema;

/**
 * Created by alexanderchiou on 10/31/15.
 */
public class MySQLiteHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "sqltester.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase database;

    SchemaServer schemaServer;
    Schema[] allSchemas;

    public MySQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        schemaServer = SchemaServer.getSchemaServer();
        allSchemas = schemaServer.serveAllTables();
    }

    // Create and populate table(s)
    @Override
    public void onCreate(SQLiteDatabase database)
    {
        MySQLiteHelper.database = database;
        // For each table that the SchemaServer gives us
        for (int i = 0; i < allSchemas.length; i++)
        {
            database.execSQL(allSchemas[i].creationStatement());
            String[] allInserts = allSchemas[i].insertStatements();

            if (allInserts != null)
            {
                // Run each of its corresponding inserts
                for (int j = 0; j < allInserts.length; j++)
                {
                    database.execSQL(allInserts[j]);
                }
            }
        }
    }

    public static SQLiteDatabase getConnection() {
        return database;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
                + ", which will destroy all old data");

        for (int i = 0; i < allSchemas.length; i++)
        {
            db.execSQL("DROP TABLE IF EXISTS " + allSchemas[i].getName());
        }
        onCreate(db);
    }
}
