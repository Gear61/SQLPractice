package randomappsinc.com.sqlpractice.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        ideaToUrl.put(MAX, "https://www.w3schools.com/sql/sql_min_max.asp");
        ideaToUrl.put(SUBQUERIES, "http://www.tutorialspoint.com/sql/sql-sub-queries.htm");
        ideaToUrl.put(ORDER_BY, "http://www.w3schools.com/sql/sql_orderby.asp");
        ideaToUrl.put(LIMIT, "https://www.tutorialspoint.com/sqlite/sqlite_limit_clause.htm");
        ideaToUrl.put(MIN, "https://www.w3schools.com/sql/sql_min_max.asp");
        ideaToUrl.put(LIKE, "http://www.w3schools.com/sql/sql_like.asp");
        ideaToUrl.put(ALIASES, "http://www.w3schools.com/sql/sql_alias.asp");
        ideaToUrl.put(INNER_JOIN, "http://www.w3schools.com/sql/sql_join_inner.asp");
        ideaToUrl.put(AVG, "https://www.w3schools.com/sql/sql_count_avg_sum.asp");
        ideaToUrl.put(SQLITE_METADATA, "https://www.sqlite.org/faq.html#q7");
        ideaToUrl.put(COUNT, "https://www.w3schools.com/sql/sql_count_avg_sum.asp");
        ideaToUrl.put(AND_OR, "http://www.w3schools.com/sql/sql_and_or.asp");
        ideaToUrl.put(COALESCE, "http://stackoverflow.com/a/6134463");
        ideaToUrl.put(SUM, "https://www.w3schools.com/sql/sql_count_avg_sum.asp");
    }

    public String getUrl(String idea) {
        return ideaToUrl.get(idea);
    }

    private List<String> getLessons() {
        return new ArrayList<>(ideaToUrl.keySet());
    }

    public String[] getLessonsArray() {
        return getLessons().toArray(new String[getLessons().size()]);
    }
}
