package ru.praktikumservices.qascooter.client;

import io.restassured.response.Response;
import ru.praktikumservices.qascooter.model.Courier;
import io.qameta.allure.Step;

import static ru.praktikumservices.qascooter.config.URL.COURIER_CREATION_ENDPOINT;
import static ru.praktikumservices.qascooter.config.URL.COURIER_DELETE_ENDPOINT;


public class CourierCreationApiClient extends BaseApiClient{

    @Step("Создание курьера: логин = {courier.login}, имя = {courier.firstname}")
    public Response createCourier(Courier courier) {
        return sendPostRequest(COURIER_CREATION_ENDPOINT, courier);
    }


    @Step("Удаление курьера с ID = {courierId}")
    public Response deleteCourier(int courierId) {
        return sendDeleteRequest(COURIER_DELETE_ENDPOINT + courierId);
    }

}
