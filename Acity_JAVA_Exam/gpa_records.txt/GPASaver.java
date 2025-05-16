import java.io.FileWriter;
import java.io.IOException;

public class GPASaver {

    public static void saveToFile(String name, String id, String[] courses, String[] grades, String[] credits, double gpa) {
        try {
            FileWriter writer = new FileWriter("gpa_records.txt", true);

            writer.write("Student Name: " + name + "\n");
            writer.write("Student ID: " + id + "\n");

            for (int i = 0; i < courses.length; i++) {
                String course = courses[i].trim();
                String grade = grades[i].trim().toUpperCase();
                String credit = credits[i].trim();

                writer.write("Course: " + course + " | Grade: " + grade + " | Credit Hours: " + credit + "\n");
            }

            writer.write("Calculated GPA: " + String.format("%.2f", gpa) + "\n");
            writer.write("----------------------------------------\n");

            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while saving the GPA to file.");
        }
    }
}
