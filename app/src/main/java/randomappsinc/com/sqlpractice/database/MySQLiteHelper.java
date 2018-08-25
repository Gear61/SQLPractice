package randomappsinc.com.sqlpractice.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import randomappsinc.com.sqlpractice.database.models.Schema;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sqltester.db";
    private static final int DATABASE_VERSION = 1;

    private Schema[] allSchemas;

    MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SchemaServer schemaServer = SchemaServer.getSchemaServer();
        allSchemas = schemaServer.serveAllTables();
    }

    // Create and populate table(s)
    @Override
    public void onCreate(SQLiteDatabase database) {
        // For each table that the SchemaServer gives us
        for (Schema schema : allSchemas) {
            database.execSQL(schema.creationStatement());
            String[] allInserts = schema.insertStatements();

            if (allInserts != null) {
                // Run each of its corresponding inserts
                for (String insert : allInserts) {
                    database.execSQL(insert);
                }
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (Schema schema : allSchemas) {
            db.execSQL("DROP TABLE IF EXISTS " + schema.getName());
        }
        onCreate(db);
    }
}
