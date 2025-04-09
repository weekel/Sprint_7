package tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikumservices.qascooter.client.OrderCreationApiClient;
import ru.praktikumservices.qascooter.client.OrderListApiClient;
import ru.praktikumservices.qascooter.model.Order;
import ru.praktikumservices.qascooter.model.Track;
import ru.praktikumservices.qascooter.assertions.OrderListAssertions;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

@DisplayName("Тесты на получение списка заказов")
public class OrdersListTests {
    OrderListApiClient orderListClient = new OrderListApiClient();
    OrderCreationApiClient orderCreationClient = new OrderCreationApiClient();

    private final Faker faker = new Faker();

    private Order order;
    private Track track;

    private String firstName = faker.name().firstName();
    private String lastName = faker.name().lastName();
    private String address = faker.address().streetAddress();
    private int metroStation = faker.number().numberBetween(1, 10); // допустим, станции от 1 до 10
    private String phone = faker.phoneNumber().phoneNumber(); // например: +7 (921) 123-45-67
    private int rentTime = faker.number().numberBetween(1, 10);
    private String deliveryDate = LocalDate.now().plusDays(faker.number().numberBetween(1, 7)).toString(); // дата в пределах недели
    private String comment = faker.lorem().sentence();
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

    @DisplayName("Успешный запрос списка заказов возвращает 200")
    @Description("Проверка, что при запросе списка заказов возвращается статус 200 OK и в теле ответа на запрос содержится массив заказов")
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
