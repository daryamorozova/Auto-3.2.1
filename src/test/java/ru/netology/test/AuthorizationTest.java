package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataHelper.*;
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

    @Test
    void shouldFailureAuthorizationIfInvalidLogin() {
        val loginPage = new LoginPage();
        val authInfo = getInvalidAuthIfInvalidLogin();
        loginPage.completedEntry(authInfo);
        loginPage.authorizationInvalid();
    }

    @Test
    void shouldFailureAuthorizationIfInvalidPassword() {
        val loginPage = new LoginPage();
        val authInfo = getInvalidAuthIfInvalidPassword();
        loginPage.completedEntry(authInfo);
        loginPage.authorizationInvalid();
    }

    @Test
    void shouldFailureVerificationIfInvalidCode() {
        val loginPage = new LoginPage();
        val authInfo = getAuthInfo();
        val verificationPage = loginPage.authorizationValid(authInfo);
        val verificationCode = getInvalidVerificationCode();
        verificationPage.verificationValid(verificationCode);
        verificationPage.verificationInvalid();
    }

    @Test
    void shouldBlockedIfTryEnterWithInvalidPasswordThreeTimes() {
        val loginPage = new LoginPage();
        val authInfo = getInvalidAuthIfInvalidPassword();
        loginPage.completedEntry(authInfo);
        loginPage.authorizationInvalid();
        loginPage.clearPassword();
        loginPage.authorizationInvalidIfInvalidPassword(authInfo.getPassword());
        loginPage.clearPassword();
        loginPage.authorizationInvalidIfInvalidPassword(authInfo.getPassword());
        loginPage.loginButtonShouldBeInactive();
    }

}
