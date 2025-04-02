package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikumservices.qascooter.client.OrderCreationApiClient;
import ru.praktikumservices.qascooter.model.Order;
import ru.praktikumservices.qascooter.model.Track;
import ru.praktikumservices.qascooter.assertions.OrderCreationAssertions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTests {
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
    private List<String> color;

    public OrderCreationTests(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет: {0}")
    public static Object[][] getColorData() {
        return new Object[][] {
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {Arrays.asList()}
        };
    }

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
    }

    @Description("Проверка успешного создания заказа при разных значениях поля color")
    @Test
    public void shouldCreateOrderWithVariousColorOptions() {
        Response response = orderCreationClient.createOrder(order);

        OrderCreationAssertions.assertOrderCreatesSuccessfully(response, color);

        //Получаем track для отмены заказа
        track = new Track(response.then()
                .extract()
                .path("track"));
    }

    @After
    public void tearDown() {
        if (track != null) {
            orderCreationClient.cancelOrder(track);
        }
    }
}
