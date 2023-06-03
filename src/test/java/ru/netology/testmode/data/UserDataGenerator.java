package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import java.util.UUID;


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

    private UserDataGenerator() {
    }

    public static UserData generateUser(String status) {
        String login = generateRandomLogin();
        String password = generateRandomPassword();
        UserData user = new UserData(login, password, status);
        saveUser(user);
        return user;
    }

    private static String generateRandomLogin() {
        Faker faker = new Faker();
        String login = faker.name().username();
        return login;
    }

    private static String generateRandomPassword() {
        Faker faker = new Faker();
        String password = faker.internet().password();
        return password;
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
