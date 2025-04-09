package tests;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import ru.praktikumservices.qascooter.client.CourierCreationApiClient;
import ru.praktikumservices.qascooter.client.CourierLoginApiClient;
import ru.praktikumservices.qascooter.model.Courier;
import ru.praktikumservices.qascooter.assertions.CourierLoginAssertions;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@DisplayName("Тесты на логин курьера")
public class CourierLoginTests {
    private final CourierLoginApiClient courierLoginClient = new CourierLoginApiClient();
    private final CourierCreationApiClient courierCreationClient = new CourierCreationApiClient();
    private Courier testCourier;
    private Courier nonExistingCourier = new Courier("Non_existing", "user");
    private int createdCourierId;

    private final Faker faker = new Faker();

    private String login = faker.name().username();
    private String password = faker.internet().password();


    @Before
    public void setUp(){
        testCourier = new Courier(login, password);
        courierCreationClient.createCourier(testCourier);
    }

    @DisplayName("Успешный вход курьера при валидных данных")
    @Description("Проверка, что система авторизует курьера и возвращает id при правильном логине и пароле")
    @Test
    public void testCourierLoginSuccessfully() {

        Response response = courierLoginClient.loginCourier(testCourier);

        CourierLoginAssertions.assertCourierLoginSuccessfully(response);
    }

    @DisplayName("Вход без логина возвращает ошибку 400")
    @Description("Получение ошибки и код 400 при попытке входа без логина")
    @Test
    public void testCourierLoginWithoutLoginReturns400() {
        testCourier.setLogin(null);

        Response response = courierLoginClient.loginCourier(testCourier);

        CourierLoginAssertions.assertMissingFieldError(response);
    }

    @DisplayName("Вход без пароля возвращает ошибку 400")
    @Description("Получение ошибки и код 400 при попытке входа без пароля")
    @Test
    public void testCourierLoginWithoutPasswordReturns400() {
        testCourier.setPassword(null);

        Response response = courierLoginClient.loginCourier(testCourier);

        CourierLoginAssertions.assertMissingFieldError(response);
    }

    @DisplayName("Вход с несуществующими логином и паролем возвращает 404")
    @Description("Получение ошибки и код 404 при попытке входа с несуществующими учетными данными")
    @Test
    public void testCourierLoginWithNonExistingParamsReturns404() {
        Response response = courierLoginClient.loginCourier(nonExistingCourier);

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
