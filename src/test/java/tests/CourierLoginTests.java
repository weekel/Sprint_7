package tests;

import ru.praktikumservices.qascooter.client.CourierCreationApiClient;
import ru.praktikumservices.qascooter.client.CourierLoginApiClient;
import ru.praktikumservices.qascooter.model.Courier;
import ru.praktikumservices.qascooter.assertions.CourierLoginAssertions;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CourierLoginTests {
    private final CourierLoginApiClient courierLoginClient = new CourierLoginApiClient();
    private final CourierCreationApiClient courierCreationClient = new CourierCreationApiClient();
    private Courier testCourier;
    private int createdCourierId;

    private String login = "moonlight";
    private String password = "1234";

    @Before
    public void setUp(){
        testCourier = new Courier(login, password);
    }

    @Description("Успешный вход курьера в систему при передаче валидных login и password")
    @Test
    public void testCourierLoginSuccessfully() {
        courierCreationClient.createCourier(testCourier);

        Response response = courierLoginClient.loginCourier(testCourier);

        CourierLoginAssertions.assertCourierLoginSuccessfully(response);
    }

    @Description("Получение ошибки при попытке входа курьера без логина")
    @Test
    public void testCourierLoginWithoutLoginReturns400() {
        testCourier.setLogin(null);

        Response response = courierLoginClient.loginCourier(testCourier);

        CourierLoginAssertions.assertMissingFieldError(response);
    }

    @Description("Получение ошибки при попытке входа курьера без пароля")
    @Test
    public void testCourierLoginWithoutPasswordReturns400() {
        testCourier.setPassword(null);

        Response response = courierLoginClient.loginCourier(testCourier);

        CourierLoginAssertions.assertMissingFieldError(response);
    }

    @Description("Получение ошибки при попытке входа несуществующего курьера")
    @Test
    public void testCourierLoginWithNonExistingParamsReturns404() {
        Response response = courierLoginClient.loginCourier(testCourier);

        CourierLoginAssertions.assertNonExistingCourierError(response);
    }


    @After
    public void tearDown() {
        if (testCourier.getLogin() != null && testCourier.getPassword() != null) {
            Response loginResponse = courierLoginClient.loginCourier(testCourier);
            if (loginResponse.statusCode() == 200) {
                createdCourierId = loginResponse.jsonPath().getInt("id");
                courierCreationClient.deleteCourier(createdCourierId);
            }
        }
    }
}
