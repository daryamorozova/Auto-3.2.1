package ru.netology.data;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.*;

public class SQLData {

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://192.168.99.100:3306/app", "app", "pass");
    }

    public static void dropDataBase() {
        val runner = new QueryRunner();
        val users = "DELETE FROM users";
        val verificationCodes = "DELETE FROM auth_codes";
        val cards = "DELETE FROM cards";

        try (Connection connection = getConnection()) {
            runner.update(connection, users);
            runner.update(connection, verificationCodes);
            runner.update(connection, cards);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static String getVerificationCode(DataHelper.AuthInfo authInfo) {
        val runner = new QueryRunner();
        String login = authInfo.getLogin();
        String userId = null;
        val findId = "SELECT id FROM users WHERE login = ?;";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatementId = connection.prepareStatement(findId)) {
            preparedStatementId.setString(1, login);
            try (ResultSet resultSet = preparedStatementId.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getString("id");
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        String verifCode = "";
        val verificationCode = "SELECT code FROM auth_codes WHERE user_id = ? ORDER BY created DESC LIMIT 1;";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatementCode = connection.prepareStatement(verificationCode)) {
            preparedStatementCode.setString(1, userId);
            try (ResultSet resultSet = preparedStatementCode.executeQuery()) {
                if (resultSet.next()) {
                    verifCode = resultSet.getString("code");
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return verifCode;
    }
}
