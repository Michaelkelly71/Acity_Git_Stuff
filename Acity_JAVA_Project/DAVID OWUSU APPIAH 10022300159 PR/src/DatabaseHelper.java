import java.sql.*;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:contacts.db";

    public static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS contacts (" +
                         "name TEXT, phone TEXT PRIMARY KEY, email TEXT)";
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean saveContact(Contact contact) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "INSERT INTO contacts(name, phone, email) VALUES(?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getPhone());
            stmt.setString(3, contact.getEmail());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false; // likely duplicate
        }
    }

    public static boolean deleteContact(String phone) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String query = "DELETE FROM contacts WHERE phone=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, phone);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public static ResultSet getAllContactsSorted(String byColumn) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        String query = "SELECT * FROM contacts ORDER BY " + byColumn;
        return conn.createStatement().executeQuery(query);
    }
}
