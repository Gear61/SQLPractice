package randomappsinc.com.sqlpractice.Database;

public class AnswerServer {

    // Answers stored here in this ghetto hard-coded array
    private static String[] answers =
            {"SELECT * FROM SALARIES;",
                    "SELECT Professor_Name FROM SALARIES;",
                    "SELECT DISTINCT Department FROM SALARIES;",
                    "SELECT COUNT(Professor_Name) FROM SALARIES WHERE Salary > 150000;",
                    "SELECT DEPARTMENT, SUM(Salary) FROM SALARIES GROUP BY DEPARTMENT;",
                    "SELECT Professor_Name, MAX(Salary) FROM SALARIES;",
                    "SELECT Professor_Name, MAX(Salary) FROM SALARIES WHERE Department = \"Computer Science\";",
                    "SELECT Professor_Name, Salary FROM SALARIES ORDER BY Salary DESC LIMIT 5;",
                    "SELECT Professor_Name, MIN(Salary) FROM SALARIES;",
                    "SELECT Department FROM SALARIES WHERE Professor_Name = \"Zaniolo\";",
                    "SELECT Professor_Name FROM SALARIES WHERE Professor_Name LIKE 'C%';",
                    "SELECT DISTINCT Salary FROM SALARIES ORDER BY Salary DESC LIMIT 1 OFFSET 2",
                    "SELECT DISTINCT a.First_Name, a.Last_Name FROM CHECKED_OUT AS a, " +
                            "CHECKED_OUT AS b where a.Last_Name = b.Last_Name " +
                            "AND a.First_Name != b.First_Name",
                    "SELECT DISTINCT First_Name, Last_Name FROM CHECKED_OUT INNER JOIN BOOKS " +
                            "ON CHECKED_OUT.Book_ID = BOOKS.Book_ID WHERE Author = \"Terry Crews\";",
                    "SELECT DISTINCT First_Name, Last_Name FROM CHECKED_OUT INNER JOIN BOOKS " +
                            "ON CHECKED_OUT.Book_ID = BOOKS.Book_ID WHERE Author = \"Harper Lee\" " +
                            "AND BOOK_NAME = \"To Kill a Mockingbird\";",
                    "SELECT AVG(Salary) FROM SALARIES;",
                    "SELECT Professor_Name, Salary + 10000 FROM SALARIES WHERE Department = \"Computer Science\";",
                    "SELECT sql FROM sqlite_master WHERE type = \"table\" AND tbl_name = \"CHECKED_OUT\";",
                    "SELECT Professor_Name, Salary FROM SALARIES WHERE Salary > " +
                            "(SELECT MIN(Salary) FROM SALARIES) * 4;",
                    "SELECT COUNT(DISTINCT(AUTHOR)) FROM BOOKS;",
                    "SELECT Professor_Name, Salary FROM SALARIES WHERE Salary BETWEEN 120000 AND 250000;",
                    "SELECT * FROM SALARIES WHERE Department = \"Anthropology\" OR Salary > 150000;",
                    "SELECT MAX(Salary) FROM SALARIES WHERE Department = \"Computer Science\";",
                    "SELECT COUNT(*) FROM SALARIES WHERE Salary > 2 * " +
                            "(SELECT MIN(Salary) FROM SALARIES WHERE Department = \"Political Science\");",
                    "SELECT Book_Name FROM CHECKED_OUT INNER JOIN BOOKS ON CHECKED_OUT.Book_ID = BOOKS.Book_ID " +
                            "WHERE First_Name = \"Justin\" AND Last_Name = \"Lee\";",
                    "SELECT DISTINCT(COALESCE(First_Name, '') || ' ' || COALESCE(Last_Name, '')) " +
                            "AS Name FROM CHECKED_OUT;",
                    "SELECT Department, SUM(Salary) FROM SALARIES GROUP BY DEPARTMENT " +
                            "ORDER BY SUM(Salary) DESC LIMIT 1;",
                    "SELECT Department, AVG(Salary) FROM SALARIES GROUP BY DEPARTMENT " +
                            "ORDER BY AVG(Salary) DESC LIMIT 1;",
                    "SELECT Professor_Name, Department, Max(Salary) FROM SALARIES GROUP BY Department;",
                    "SELECT Professor_Name, Salary FROM SALARIES WHERE Salary " +
                            ">= (SELECT DISTINCT Salary FROM SALARIES ORDER BY Salary DESC LIMIT 1 OFFSET 2) " +
                            "OR Salary <= (SELECT DISTINCT Salary FROM SALARIES ORDER BY Salary ASC LIMIT 1 OFFSET 2);",
                    "SELECT Department, Count() FROM SALARIES GROUP BY Department;",
                    "SELECT Department, Count() FROM SALARIES GROUP BY Department ORDER BY COUNT() DESC LIMIT 1;"};

    public static String getAnswer(int position)
    {
        return answers[position];
    }
}
