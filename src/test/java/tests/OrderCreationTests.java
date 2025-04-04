package tests;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@DisplayName("Тесты на создание заказа")
@RunWith(Parameterized.class)
public class OrderCreationTests {
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

    @DisplayName("Успешное создание заказа при разных вариантах цвета")
    @Description("Проверка, что заказ успешно создается при передаче одного, нескольких или отсутствующих значений параметра color")
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
