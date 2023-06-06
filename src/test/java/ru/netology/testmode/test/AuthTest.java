package ru.netology.testmode.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.testmode.data.UserDataGenerator;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;


class AuthTest {

    @BeforeEach
    void setup() {
        Configuration.baseUrl = "http://localhost:9999";
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        UserDataGenerator.UserData registeredUser = UserDataGenerator.generateUser("active");

        open("/");
        $(byName("login")).setValue(registeredUser.getLogin());
        $(byName("password")).setValue(registeredUser.getPassword());
        $(".button").click();
        $(".heading_size_l").shouldHave(text("Личный кабинет"));
        $(".icon_name_bank-2449").shouldBe(visible);
    }
    @Test
    @DisplayName("Should fail login with incorrect password")
    void shouldFailLoginWithIncorrectPassword() {
        UserDataGenerator.UserData user = UserDataGenerator.generateUser("active");
        String incorrectPassword = UserDataGenerator.generateRandomPassword();

        open("/");
        $(byName("login")).setValue(user.getLogin());
        $(byName("password")).setValue(incorrectPassword);
        $(".button").click();
        $(".notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should fail login with incorrect login")
    void shouldFailLoginWithIncorrectLogin() {
        UserDataGenerator.UserData user = UserDataGenerator.generateUser("active");
        String incorrectLogin = UserDataGenerator.generateRandomLogin();

        open("/");
        $(byName("login")).setValue(incorrectLogin);
        $(byName("password")).setValue(user.getPassword());
        $(".button").click();
        $(".notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should fail login with unregistered user")
    void shouldFailLoginWithUnregisteredUser() {
        String unregisteredUser = UserDataGenerator.generateRandomLogin();
        String password = UserDataGenerator.generateRandomPassword();

        open("/");
        $(byName("login")).setValue(unregisteredUser);
        $(byName("password")).setValue(password);
        $(".button").click();
        $(".notification__content").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}