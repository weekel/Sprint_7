package ru.praktikumservices.qascooter.assertions;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

public class OrderListAssertions {

    @Step("Проверка: список заказов возвращается (код 200 и orders в теле ответа")
    public static void assertOrderListReturns(Response response) {
        response.then()
                .statusCode(200)
                .body("orders", notNullValue())
                .body("orders", instanceOf(java.util.List.class))
                .body("orders.size()", greaterThan(0));
    }
}
