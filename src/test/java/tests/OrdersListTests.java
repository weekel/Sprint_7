package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikumservices.qascooter.client.OrderCreationApiClient;
import ru.praktikumservices.qascooter.client.OrderListApiClient;
import ru.praktikumservices.qascooter.model.Order;
import ru.praktikumservices.qascooter.model.Track;
import ru.praktikumservices.qascooter.assertions.OrderListAssertions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

public class OrdersListTests {
    OrderListApiClient orderListClient = new OrderListApiClient();
    OrderCreationApiClient orderCreationClient = new OrderCreationApiClient();

    private Order order;
    private Track track;

    private String firstName = "Naruto";
    private String lastName = "Uchiha";
    private String address = "Konoha, 142 apt.";
    private int metroStation = 4;
    private String phone = "+7 800 355 35 35";
    private int rentTime = 5;
    private String deliveryDate = "2025-04-06";
    private String comment = "Saske, come back to Konoha";
    private List<String> color = Arrays.asList("BLACK");

    @Before
    public void setUp() {
        order = new Order(
                firstName,
                lastName,
                address,
                metroStation,
                phone,
                rentTime,
                deliveryDate,
                comment,
                color
        );
        Response response = orderCreationClient.createOrder(order);
        track = new Track(response.jsonPath().getInt("track"));
    }

    @Description("Успешная отправка запроса на получение списка заказов")
    @Test
    public void testGetOrdersReturns200() {
        Response response = orderListClient.getOrdersList();

        OrderListAssertions.assertOrderListReturns(response);
    }


    @After
    public void tearDown() {
        if (track != null) {
            orderCreationClient.cancelOrder(track);
        }
    }
}
