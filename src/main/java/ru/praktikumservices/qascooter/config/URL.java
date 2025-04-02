package ru.praktikumservices.qascooter.config;

public class URL {
    public static final String BASE_HOST = "https://qa-scooter.praktikum-services.ru/";

    // Эндпоинты для курьера
    public static final String COURIER_CREATION_ENDPOINT = "/api/v1/courier";
    public static final String COURIER_DELETE_ENDPOINT = "/api/v1/courier/";
    public static final String COURIER_LOGIN_ENDPOINT = "/api/v1/courier/login";

    //Эндпойнты для заказа
    public static final String ORDER_CREATION_ENDPOINT = "/api/v1/orders";
    public static final String ORDER_CANCELLATION_ENDPOINT = "/api/v1/orders/cancel";
    public static final String ORDERS_LIST_ENDPOINT = "/api/v1/orders";
}
