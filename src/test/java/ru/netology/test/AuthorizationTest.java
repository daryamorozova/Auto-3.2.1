package ru.netology.test;

import com.github.javafaker.Faker;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.page.LoginPage;

import java.sql.DriverManager;
import java.sql.SQLData;
import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataHelper.getAuthInfo;
import static ru.netology.data.SQLData.dropDataBase;
import static ru.netology.data.SQLData.getVerificationCode;

public class AuthorizationTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @AfterAll
    public static void clearDataBase() {
        dropDataBase();
    }

    @Test
    void shouldSuccessAuthorization() {
        val loginPage = new LoginPage();
        val authInfo = getAuthInfo();
        val verificationPage = loginPage.authorizationValid(authInfo);
        val verificationCode = getVerificationCode(authInfo);
        val dashboardPage = verificationPage.verificationValid(verificationCode);
        dashboardPage.dashboardPageIsVisible();
    }


}
