package repositories;

import Enumes.Status;
import classes.Enrollment;
import utils.Database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcEnrollRepo implements EnrollmentRepo {
    @Override
    public void save(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (id, child_name, parent_id, created_at, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, enrollment.getId());
            statement.setString(2, enrollment.getChildName());
            statement.setInt(3, enrollment.getParentId());
            statement.setDate(4, Date.valueOf(enrollment.getCreatedAt()));
            statement.setString(5, enrollment.getStatus().name());
            statement.executeUpdate();
        } catch (SQLException error) {
            throw databaseError(error);
        }
    }

    @Override
    public List<Enrollment> findAll() {
        String sql = "SELECT id, child_name, parent_id, created_at, status FROM enrollments ORDER BY id";
        List<Enrollment> enrollments = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                enrollments.add(toEnrollment(resultSet));
            }
            return enrollments;
        } catch (SQLException error) {
            throw databaseError(error);
        }
    }

    @Override
    public Enrollment findById(int id) {
        String sql = "SELECT id, child_name, parent_id, created_at, status FROM enrollments WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? toEnrollment(resultSet) : null;
            }
        } catch (SQLException error) {
            throw databaseError(error);
        }
    }

    @Override
    public void update(Enrollment enrollment) {
        String sql = "UPDATE enrollments SET child_name = ?, parent_id = ?, created_at = ?, status = ? WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, enrollment.getChildName());
            statement.setInt(2, enrollment.getParentId());
            statement.setDate(3, Date.valueOf(enrollment.getCreatedAt()));
            statement.setString(4, enrollment.getStatus().name());
            statement.setInt(5, enrollment.getId());
            statement.executeUpdate();
        } catch (SQLException error) {
            throw databaseError(error);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM enrollments WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException error) {
            throw databaseError(error);
        }
    }

    private Enrollment toEnrollment(ResultSet resultSet) throws SQLException {
        return new Enrollment(
                resultSet.getInt("id"),
                resultSet.getString("child_name"),
                resultSet.getInt("parent_id"),
                resultSet.getDate("created_at").toLocalDate(),
                Status.valueOf(resultSet.getString("status"))
        );
    }

    private RuntimeException databaseError(SQLException error) {
        return new IllegalStateException("Database operation failed: " + error.getMessage(), error);
    }
}
