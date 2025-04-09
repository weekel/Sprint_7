package tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import ru.praktikumservices.qascooter.client.CourierCreationApiClient;
import ru.praktikumservices.qascooter.client.CourierLoginApiClient;
import ru.praktikumservices.qascooter.assertions.CourierCreationAssertions;
import ru.praktikumservices.qascooter.model.Courier;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.github.javafaker.Faker;

@DisplayName("Тесты на создание курьера")
public class CourierCreationTests {
    private final CourierCreationApiClient courierCreationClient = new CourierCreationApiClient();
    private final CourierLoginApiClient courierLoginClient = new CourierLoginApiClient();
    private Courier testCourier;
    private int createdCourierId;

    private final Faker faker = new Faker();

    private String login = faker.name().username();;
    private String password = faker.internet().password();
    private String firstname = faker.name().firstName();


    @Before
    public void setUp() {
        testCourier = new Courier(login, password, firstname);
    }

    @DisplayName("Успешный вход курьера в систему")
    @Description("Успешное создание курьера при передаче валидных login, password и firstname")
    @Test
    public void testCreateCourierSuccessfully() {
        Response response = courierCreationClient.createCourier(testCourier);

        CourierCreationAssertions.assertCourierCreated(response);
    }

    @DisplayName("Создание курьера с уже существующим логином возвращает 409")
    @Description("Получение ошибки и код 409 при попытке создать уже существующего курьера")
    @Test
    public void testCreateExistingCourierReturns409() {
        courierCreationClient.createCourier(testCourier);
        Response response = courierCreationClient.createCourier(testCourier);

        CourierCreationAssertions.assertDuplicateCourierError(response);
    }

    @DisplayName("Создание курьера без пароля возвращает ошибку 400")
    @Description("Получение ошибки и код 400 при попытке создать курьера без пароля")
    @Test
    public void testCreateCourierWithoutPasswordReturns400() {
        testCourier.setPassword(null);

        Response response = courierCreationClient.createCourier(testCourier);

        CourierCreationAssertions.assertMissingFieldError(response);
    }

    @DisplayName("Создание курьера без пароля возвращает ошибку 400")
    @Description("Получение ошибки и код 400 при попытке создать курьера без логина")
    @Test
    public void testCreateCourierWithoutLoginReturns400() {
        testCourier.setLogin(null);

        Response response = courierCreationClient.createCourier(testCourier);

        CourierCreationAssertions.assertMissingFieldError(response);
    }

    @DisplayName("Успешное создание курьера без имени")
    @Description("Успешное создание курьера без параметра firstname — код ответа 201")
    @Test
    public void testCreateCourierWithoutFirstnameReturns201() {
        testCourier.setFirstname(null);
        Response response = courierCreationClient.createCourier(testCourier);

        CourierCreationAssertions.assertCourierCreated(response);
    }


    @After
    public void tearDown() {
        if (testCourier.getLogin() != null && testCourier.getPassword() != null) {
            Courier courierLogIn = new Courier(testCourier.getLogin(), testCourier.getPassword());
            Response loginResponse = courierLoginClient.loginCourier(courierLogIn);
            if (loginResponse.statusCode() == 200) {
                createdCourierId = loginResponse.jsonPath().getInt("id");
            }
        }
        if (createdCourierId != 0) {
            courierCreationClient.deleteCourier(createdCourierId);
        }
    }
}

