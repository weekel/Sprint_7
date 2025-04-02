package ru.praktikumservices.qascooter.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.praktikumservices.qascooter.model.Order;
import ru.praktikumservices.qascooter.model.Track;

import static ru.praktikumservices.qascooter.config.URL.ORDER_CANCELLATION_ENDPOINT;
import static ru.praktikumservices.qascooter.config.URL.ORDER_CREATION_ENDPOINT;

public class OrderCreationApiClient extends BaseApiClient{

    @Step("Создание закза: цвет = {order.color}")
    public Response createOrder(Order order) {
        return sendPostRequest(ORDER_CREATION_ENDPOINT, order);
    }

    public Response cancelOrder(Track track) {
        return sendPutRequest(ORDER_CANCELLATION_ENDPOINT, track);
    }
}
