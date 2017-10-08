package randomappsinc.com.sqlpractice.database.models;

public class Column {

    private String rowName;
    private String dataType;

    public Column(String rowName, String dataType) {
        this.rowName = rowName;
        this.dataType = dataType;
    }

    public String getRowName() {
        return rowName;
    }

    public String getDataType() {
        return dataType;
    }
}
