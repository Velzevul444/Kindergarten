package repositories;

import classes.Parents;
import utils.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcParentRepo implements ParentRepo {
    @Override
    public List<Parents> findAll() {
        String sql = "SELECT id, name, email, phone FROM parents ORDER BY id";
        List<Parents> parents = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                parents.add(toParent(resultSet));
            }
            return parents;
        } catch (SQLException error) {
            throw databaseError(error);
        }
    }

    @Override
    public boolean existsById(int id) {
        String sql = "SELECT 1 FROM parents WHERE id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException error) {
            throw databaseError(error);
        }
    }

    private Parents toParent(ResultSet resultSet) throws SQLException {
        return new Parents(
                resultSet.getString("name"),
                resultSet.getInt("id"),
                resultSet.getString("email"),
                resultSet.getString("phone")
        );
    }

    private RuntimeException databaseError(SQLException error) {
        return new IllegalStateException("Database operation failed: " + error.getMessage(), error);
    }
}