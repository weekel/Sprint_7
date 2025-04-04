package ru.praktikumservices.qascooter.assertions;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.apache.http.HttpStatus.*;

public class OrderCreationAssertions {

    @Step("Проверка: успешное создание заказа c цветами: {color} (код 201 и track в теле ответа)")
    public static void assertOrderCreatesSuccessfully(Response response, List<String> color) {
        response.then()
                .statusCode(SC_CREATED)
                .body("track", notNullValue());
    }
}
