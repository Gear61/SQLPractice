package randomappsinc.com.sqlpractice.Database.Models;

/**
 * Created by alexanderchiou on 10/31/15.
 */
public class ResultSet {
    private String[] columns;
    private String[][] data;
    private String exception;

    public ResultSet (String[] columns, String[][] data, String exception) {
        this.columns = columns;
        this.data = data;
        this.exception = exception;
    }

    public String[] getColumns() {
        return columns;
    }

    public String[][] getData() {
        return data;
    }

    public String getException() {
        return exception;
    }
}
