package ru.praktikumservices.qascooter.assertions;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierLoginAssertions {

    @Step("Провека: успешный вход в систему (код 200 и id в теле ответа)")
    public static void assertCourierLoginSuccessfully(Response response) {
        response.then()
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Step("Проверка: вход в систему курьера без логина или пароля возвращает 400")
    public static void assertMissingFieldError(Response response) {
        response.then()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Step("Проверка: вход в систему несуществующего возвращает 404")
    public static void assertNonExistingCourierError(Response response) {
        response.then()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }
}
