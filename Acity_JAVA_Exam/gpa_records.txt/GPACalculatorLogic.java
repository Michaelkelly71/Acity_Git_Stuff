public class GPACalculatorLogic {

    public static double calculateGPA(String[] credits, String[] grades) throws Exception {
        int totalCredits = 0;
        double totalPoints = 0;

        for (int i = 0; i < credits.length; i++) {
            int credit = 0;
            try {
                credit = Integer.parseInt(credits[i].trim());
            } catch (NumberFormatException e) {
                throw new Exception("Invalid number format for credit at index " + (i + 1));
            }

            double point = convertGradeToPoint(grades[i].trim().toUpperCase());
            totalPoints += point * credit;
            totalCredits += credit;
        }

        if (totalCredits == 0) {
            throw new Exception("Total credit hours cannot be zero.");
        }

        return totalPoints / totalCredits;
    }

    private static double convertGradeToPoint(String grade) throws Exception {
        if (grade.equals( "A") )return 3.5;
        else if (grade.equals("B+") )return 3.5;
        else if (grade.equals("B")) return 3.0;
        else if (grade.equals("C+")) return 2.5;
        else if (grade.equals("C")) return 2.0;
        else if (grade.equals("D")) return 1.5;
        else if (grade.equals("E")) return 1.0;
        else if (grade.equals("F")) return 0.0;
        else throw new Exception("Unknown grade: " + grade);
    }
}
