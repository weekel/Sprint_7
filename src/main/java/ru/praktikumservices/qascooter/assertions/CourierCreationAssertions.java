package ru.praktikumservices.qascooter.assertions;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;

public class CourierCreationAssertions {

    @Step("Проверка: курьер успешно создан (код 201 и ok: true)")
    public static void assertCourierCreated(Response response) {
        response.then()
                .statusCode(201)
                .body("ok", equalTo(true));
    }

    @Step("Проверка: создание существующего курьера возвращает 409")
    public static void assertDuplicateCourierError(Response response) {
        response.then()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Step("Проверка: создание курьера без логина или пароля возвращает 400")
    public static void assertMissingFieldError(Response response) {
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

}
