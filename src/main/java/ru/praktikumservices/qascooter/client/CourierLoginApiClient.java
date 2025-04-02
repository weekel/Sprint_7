package ru.praktikumservices.qascooter.client;


import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikumservices.qascooter.model.Courier;

import static ru.praktikumservices.qascooter.config.URL.COURIER_LOGIN_ENDPOINT;

public class CourierLoginApiClient extends BaseApiClient{

    @Step("Вход курьера в систему: логин = {courier.login}")
    public Response loginCourier(Courier courier) {
        return sendPostRequest(COURIER_LOGIN_ENDPOINT, courier);
    }
}
