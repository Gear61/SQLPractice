package randomappsinc.com.sqlpractice.Database.Models;

/**
 * Created by alexanderchiou on 10/31/15.
 */
public class Column
{
    private String rowName;
    private String dataType;

    public Column(String rowName, String dataType)
    {
        this.rowName = rowName;
        this.dataType = dataType;
    }

    public String getRowName()
    {
        return rowName;
    }

    public String getDataType()
    {
        return dataType;
    }
}
