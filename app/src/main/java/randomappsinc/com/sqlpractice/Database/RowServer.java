package randomappsinc.com.sqlpractice.Database;

/**
 * Created by alexanderchiou on 10/31/15.
 */
public class RowServer
{
    // Database rows stored here in this ghetto hard-coded array
    private static String[][][] rows = {
            // COMPLETION_STATUS (Question_Number (TEXT))
            {},

            // SALARIES (Professor_Name (TEXT), Department (TEXT), Salary (INT))
            {{"Zaniolo", "Computer Science", "130000"},
                    {"Eggert", "Computer Science", "170000"},
                    {"Cho", "Computer Science", "150000"},
                    {"Fowler", "Anthropology", "80000"},
                    {"Wertheim", "Anthropology", "95000"},
                    {"Yang", "Anthropology", "120000"},
                    {"Kreger", "Political Science", "190000"},
                    {"Saverin", "Political Science", "90000"},
                    {"Chen", "Electrical Engineering", "125000"},
                    {"White", "Management", "150000"},
                    {"Calderon", "Journalism", "200000"},
                    {"Lee", "Computer Science", "250000"},
                    {"Jacob", "Biology", "175000"},
                    {"Ng", "Sociology", "50000"},
                    {"Hsieh", "Chemical Engineering", "400000"},
                    {"Muniain", "Spanish", "160000"},
                    {"Guerin", "Management", "500000"},
                    {"John", "Economics", "250000"}},

            // CHECKED_OUT (First_Name (TEXT), Last_Name (TEXT), Book_ID (INT))
            {{"Alex", "Chiou", "9024"},
                    {"Justin", "Lee", "3492"},
                    {"Justin", "Lee", "2122"},
                    {"Justin", "Lee", "9001"},
                    {"Tyrell", "Lee", "0022"},
                    {"Raymond", "Chen", "3999"},
                    {"Phil", "Chen", "8743"},
                    {"Nate", "Bailey", "1349"},
                    {"Nate", "Bailey", "3422"},
                    {"Derek", "Ng", "3242"},
                    {"John", "Smith", "2394"},
                    {"Soap", "MacTavish", "1234"}},

            // BOOKS (Book_ID (INT), Book_Name (TEXT), Author (TEXT))
            {{"9024", "Acting for Dummies", "Nicolas Cage"},
                    {"3492", "Euro Training for Dummies", "Terry Crews"},
                    {"2122", "To Kill a Mockingbird", "Harper Lee"},
                    {"9001", "Achieving Super Saiyan", "Terry Crews"},
                    {"0022", "How to be a Charger", "Terry Crews"},
                    {"3999", "The Art of Learning", "Josh Waitzkin"},
                    {"8743", "How to be a Sweet Cop", "Terry Crews"},
                    {"3422", "IEOR - Master Level", "John Buckingham"},
                    {"3242", "42 Wonderful Spaghetti Recipes", "Merm"},
                    {"2394", "Game of Thrones: Book 1", "George R.R. Martin"},
                    {"1243", "Pro Sniping 3", "Tony Kampy"},
                    {"9240", "War and Peace", "Some Dude"},
                    {"9211", "1984", "George Orwell"},
                    {"4923", "Animal Farm", "George Orwell"},
                    {"3923", "Jane Eyre", "Jane Smith"},
                    {"5929", "Invisible Man", "Barack Obama"},
                    {"2302", "Memories from the Navy Feel Team", "Sergeant McFeelz"},
                    {"3824", "Going HAM for Derps", "A Cool Dude"},
                    {"8141", "I Don't Read Much", "Developer of this App"}
            }

    };

    public static String[][] getRows(int tableNum)
    {
        return rows[tableNum];
    }
}