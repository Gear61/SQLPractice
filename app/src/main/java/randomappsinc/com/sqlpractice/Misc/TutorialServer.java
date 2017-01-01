package randomappsinc.com.sqlpractice.Misc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexanderchiou on 6/29/16.
 */
public class TutorialServer {
    public static final String INTRO = "SELECT Introduction";
    public static final String DISTINCT = "DISTINCT";
    public static final String WHERE = "WHERE";
    public static final String GROUP_BY = "GROUP BY";
    public static final String MAX = "MAX";
    public static final String SUBQUERIES = "Subqueries";
    public static final String ORDER_BY = "ORDER BY";
    public static final String LIMIT = "LIMIT";
    public static final String MIN = "MIN";
    public static final String LIKE = "LIKE";
    public static final String ALIASES = "Aliases";
    public static final String INNER_JOIN = "INNER JOIN";
    public static final String AVG = "AVG";
    public static final String SQLITE_METADATA = "SQLite Metadata";
    public static final String COUNT = "COUNT";
    public static final String AND_OR = "AND/OR";
    public static final String COALESCE = "COALESCE";
    public static final String SUM = "SUM";

    private static TutorialServer instance;

    private Map<String, String> ideaToUrl;

    public static TutorialServer get() {
        if (instance == null) {
            instance = new TutorialServer();
        }
        return instance;
    }

    private TutorialServer() {
        ideaToUrl = new LinkedHashMap<>();
        ideaToUrl.put(INTRO, "http://www.w3schools.com/sql/sql_select.asp");
        ideaToUrl.put(DISTINCT, "http://www.w3schools.com/sql/sql_distinct.asp");
        ideaToUrl.put(WHERE, "http://www.w3schools.com/sql/sql_where.asp");
        ideaToUrl.put(GROUP_BY, "http://www.w3schools.com/sql/sql_groupby.asp");
        ideaToUrl.put(MAX, "http://www.w3schools.com/sql/sql_func_max.asp");
        ideaToUrl.put(SUBQUERIES, "http://www.tutorialspoint.com/sql/sql-sub-queries.htm");
        ideaToUrl.put(ORDER_BY, "http://www.w3schools.com/sql/sql_orderby.asp");
        ideaToUrl.put(LIMIT, "http://www.techonthenet.com/sql/select_limit.php");
        ideaToUrl.put(MIN, "http://www.w3schools.com/sql/sql_func_min.asp");
        ideaToUrl.put(LIKE, "http://www.w3schools.com/sql/sql_like.asp");
        ideaToUrl.put(ALIASES, "http://www.w3schools.com/sql/sql_alias.asp");
        ideaToUrl.put(INNER_JOIN, "http://www.w3schools.com/sql/sql_join_inner.asp");
        ideaToUrl.put(AVG, "http://www.w3schools.com/sql/sql_func_avg.asp");
        ideaToUrl.put(SQLITE_METADATA, "https://www.sqlite.org/faq.html#q7");
        ideaToUrl.put(COUNT, "http://www.w3schools.com/sql/sql_func_count.asp");
        ideaToUrl.put(AND_OR, "http://www.w3schools.com/sql/sql_and_or.asp");
        ideaToUrl.put(COALESCE, "http://stackoverflow.com/a/6134463");
        ideaToUrl.put(SUM, "http://www.w3schools.com/sql/sql_func_sum.asp");
    }

    public String getUrl(String idea) {
        return ideaToUrl.get(idea);
    }

    public List<String> getLessons() {
        List<String> lessons = new ArrayList<>();
        for (String lesson : ideaToUrl.keySet()) {
            lessons.add(lesson);
        }
        return lessons;
    }
}
