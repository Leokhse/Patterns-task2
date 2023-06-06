package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;

import static io.restassured.RestAssured.given;

public class UserDataGenerator {
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost:9999")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Gson gson = new Gson();
    private static final Faker faker = new Faker();

    private UserDataGenerator() {
    }

    public static UserData generateUser(String status) {
        String login = generateRandomLogin();
        String password = generateRandomPassword();
        UserData user = new UserData(login, password, status);
        saveUser(user);
        return user;
    }

    public static String generateRandomLogin() {
        return faker.name().username();
    }

    public static String generateRandomPassword() {
        return faker.internet().password();
    }

    private static void saveUser(UserData user) {
        given()
                .spec(requestSpec)
                .body(gson.toJson(user))
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    @Value
    public static class UserData {
        String login;
        String password;
        String status;
    }
}