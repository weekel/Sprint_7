package ru.praktikumservices.qascooter.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static ru.praktikumservices.qascooter.config.URL.ORDERS_LIST_ENDPOINT;

public class OrderListApiClient extends BaseApiClient {

    @Step("Отправка запроса на получение списка заказов")
    public Response getOrdersList() {
        return sendGetRequest(ORDERS_LIST_ENDPOINT);
    }
}
