package randomappsinc.com.sqlpractice.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import randomappsinc.com.sqlpractice.database.models.Schema;
import randomappsinc.com.sqlpractice.misc.MyApplication;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sqltester.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase database;

    private SchemaServer schemaServer;
    private Schema[] allSchemas;

    public MySQLiteHelper() {
        super(MyApplication.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
        schemaServer = SchemaServer.getSchemaServer();
        allSchemas = schemaServer.serveAllTables();
    }

    // Create and populate table(s)
    @Override
    public void onCreate(SQLiteDatabase database) {
        MySQLiteHelper.database = database;
        // For each table that the SchemaServer gives us
        for (int i = 0; i < allSchemas.length; i++) {
            database.execSQL(allSchemas[i].creationStatement());
            String[] allInserts = allSchemas[i].insertStatements();

            if (allInserts != null) {
                // Run each of its corresponding inserts
                for (int j = 0; j < allInserts.length; j++) {
                    database.execSQL(allInserts[j]);
                }
            }
        }
    }

    public static SQLiteDatabase getConnection() {
        return database;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = 0; i < allSchemas.length; i++) {
            db.execSQL("DROP TABLE IF EXISTS " + allSchemas[i].getName());
        }
        onCreate(db);
    }
}
