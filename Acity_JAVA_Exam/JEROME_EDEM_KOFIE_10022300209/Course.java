public class Course {
    private final int creditHours;
    private final String grade;
    
    public Course(int creditHours, String grade) {
        this.creditHours = creditHours;
        this.grade = grade;
    }
    
    public int getCreditHours() {
        return creditHours;
    }
    
    public String getGrade() {
        return grade;
    }
    
    public double getGradePoint() {
        return switch (grade) {
            case "A" -> 4.0;
            case "B+" -> 3.5;
            case "B" -> 3.0;
            case "C+" -> 2.5;
            case "C" -> 2.0;
            case "D" -> 1.5;
            case "E" -> 1.0;
            case "F" -> 0.0;
            default -> 0.0;
        };
    }
} 