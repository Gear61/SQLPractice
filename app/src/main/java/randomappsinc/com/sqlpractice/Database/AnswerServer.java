package randomappsinc.com.sqlpractice.Database;

/**
 * Created by alexanderchiou on 10/31/15.
 */
public class AnswerServer
{
    // Answers stored here in this ghetto hard-coded array
    private static String[] answers =
            {"SELECT * FROM SALARIES;",
                    "SELECT Professor_Name FROM SALARIES;",
                    "SELECT DISTINCT Department FROM SALARIES;",
                    "SELECT COUNT(Professor_Name) FROM SALARIES WHERE Salary > 150000;",
                    "SELECT DEPARTMENT, SUM(Salary) FROM SALARIES GROUP BY DEPARTMENT;",
                    "SELECT Professor_Name, Salary FROM SALARIES WHERE Salary = (SELECT MAX(Salary) FROM SALARIES);",
                    "SELECT Professor_Name, Salary FROM SALARIES WHERE Department " +
                            "= \"Computer Science\" ORDER BY Salary DESC LIMIT 1;",
                    "SELECT Professor_Name, Salary FROM SALARIES ORDER BY Salary DESC LIMIT 5;",
                    "SELECT Professor_Name, Salary FROM SALARIES ORDER BY Salary ASC LIMIT 1;",
                    "SELECT Department FROM SALARIES WHERE Professor_Name = \"Zaniolo\";",
                    "SELECT Professor_Name FROM SALARIES WHERE Professor_Name LIKE 'C%';",
                    "SELECT DISTINCT Salary from SALARIES S1 where 2 = " +
                            "(SELECT COUNT (DISTINCT SALARY) from SALARIES S2 where S1.SALARY < S2.SALARY);",
                    "SELECT DISTINCT a.First_Name, a.Last_Name from CHECKED_OUT a, CHECKED_OUT b where a.Last_Name = b.Last_Name " +
                            "AND a.First_Name != b.First_Name",
                    "SELECT DISTINCT First_Name, Last_Name FROM CHECKED_OUT INNER JOIN BOOKS " +
                            "ON CHECKED_OUT.Book_ID = BOOKS.Book_ID WHERE Author = \"Terry Crews\";",
                    "SELECT DISTINCT First_Name, Last_Name FROM CHECKED_OUT INNER JOIN BOOKS " +
                            "ON CHECKED_OUT.Book_ID = BOOKS.Book_ID WHERE Author = \"Harper Lee\" " +
                            "AND BOOK_NAME = \"To Kill a Mockingbird\";",
                    "SELECT AVG(Salary) FROM SALARIES;"};

    public static String getAnswer(int position)
    {
        return answers[position];
    }
}
