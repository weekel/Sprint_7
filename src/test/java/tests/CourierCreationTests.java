package tests;

import io.qameta.allure.Description;
import ru.praktikumservices.qascooter.client.CourierCreationApiClient;
import ru.praktikumservices.qascooter.client.CourierLoginApiClient;
import ru.praktikumservices.qascooter.assertions.CourierCreationAssertions;
import ru.praktikumservices.qascooter.model.Courier;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CourierCreationTests {
    private final CourierCreationApiClient courierCreationClient = new CourierCreationApiClient();
    private final CourierLoginApiClient courierLoginClient = new CourierLoginApiClient();
    private Courier testCourier;
    private int createdCourierId;

    private String login = "moonlight";
    private String password = "1234";
    private String firstname = "Маша";


    @Before
    public void setUp() {
        testCourier = new Courier(login, password, firstname);
    }

    @Description("Успешное создание курьера при передаче валидных login, password и firstname")
    @Test
    public void testCreateCourierSuccessfully() {
        Response response = courierCreationClient.createCourier(testCourier);

        CourierCreationAssertions.assertCourierCreated(response);
    }

    @Description("Получение ошибки при попытке создать уже существующего курьера")
    @Test
    public void testCreateExistingCourierReturns409() {
        courierCreationClient.createCourier(testCourier);
        Response response = courierCreationClient.createCourier(testCourier);

        CourierCreationAssertions.assertDuplicateCourierError(response);
    }

    @Description("Получение ошибки при попытке создать курьера без пароля")
    @Test
    public void testCreateCourierWithoutPasswordReturns400() {
        testCourier.setPassword(null);

        Response response = courierCreationClient.createCourier(testCourier);

        CourierCreationAssertions.assertMissingFieldError(response);
    }

    @Description("Получение ошибки при попытке создать курьера без логина")
    @Test
    public void testCreateCourierWithoutLoginReturns400() {
        testCourier.setLogin(null);

        Response response = courierCreationClient.createCourier(testCourier);

        CourierCreationAssertions.assertMissingFieldError(response);
    }

    @Description("Успешное создание курьера без параметра firstname")
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

