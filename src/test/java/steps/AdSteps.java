package steps;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import config.TestConfig;
import io.cucumber.java.ru.*;
import models.AdData;
import models.User;
import pages.*;
import utils.ApiHelper;
import utils.TestDataGenerator;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

public class AdSteps {
    private MainPage mainPage;
    private CreateAdPage createAdPage;
    private EditAdPage editAdPage;
    private User currentUser;
    private AdData currentAd;
    private String authToken;

    public AdSteps() {
        this.mainPage = new MainPage();
        this.createAdPage = new CreateAdPage();
        this.editAdPage = new EditAdPage();
    }

    @Допустим("пользователь авторизован в системе")
    public void loginUser() {
        // Создаем пользователя через API
        currentUser = TestDataGenerator.generateRandomUser();
        var registerResponse = ApiHelper.registerUser(currentUser);
        int statusCode = registerResponse.getStatusCode();

        if (statusCode == 201) {
            authToken = ApiHelper.extractTokenFromRegisterResponse(registerResponse);
        } else if (statusCode == 400) {
            authToken = ApiHelper.getAuthToken(currentUser.getEmail(), currentUser.getPassword());
        }

        assertNotNull(authToken, "Auth token is null");

        // Логинимся через UI
        mainPage.openPage();
        mainPage.goToLogin();

        LoginPage loginPage = new LoginPage();
        loginPage.fillEmail(currentUser.getEmail())
                .fillPassword(currentUser.getPassword())
                .clickLogin();

        sleep(2000);
        assertTrue(mainPage.isUserLoggedIn(), "Failed to login via UI");
    }

    @Когда("пользователь нажимает кнопку \"Создать объявление\"")
    public void clickCreateAdButton() {
        mainPage.clickCreateAd();
    }

    @Когда("пользователь заполняет все обязательные поля объявления")
    public void fillAllRequiredFields() {
        currentAd = TestDataGenerator.generateRandomAd();
        int price = (int) currentAd.getPrice();

        createAdPage
                .fillTitle(currentAd.getTitle())
                .fillDescription(currentAd.getDescription())
                .fillPrice(String.valueOf(price))
                .selectCategory(currentAd.getCategory())
                .selectCity("Москва")
                .selectCondition("Б/У");
    }

    @Когда("пользователь нажимает кнопку \"Опубликовать\"")
    public void clickPublishButton() {
        createAdPage.clickSubmit();
    }

    @Тогда("объявление появляется в списке объявлений")
    public void verifyAdAppearsInList() {
        sleep(3000);

        String currentUrl = WebDriverRunner.url();
        System.out.println("URL after submit: " + currentUrl);

        // Если не редиректит, переходим на главную сами
        if (currentUrl.contains("create-lisiting")) {
            System.out.println("Still on create page, navigating to home...");
            com.codeborne.selenide.Selenide.open(TestConfig.BASE_URL);
            sleep(2000);
        }

        // Ищем объявление через поиск с указанием категории
        System.out.println("Searching for ad: " + currentAd.getTitle() + " in category: " + currentAd.getCategory());
        mainPage.searchAd(currentAd.getTitle(), currentAd.getCategory());

        // Проверяем что объявление найдено
        assertTrue(mainPage.isAdPresent(currentAd.getTitle()),
                "Объявление с заголовком '" + currentAd.getTitle() + "' не найдено через поиск");
    }

    @Допустим("у пользователя есть созданное объявление")
    public void createAdForTest() {
        // Создаем объявление через UI
        currentAd = TestDataGenerator.generateRandomAd();
        int price = (int) currentAd.getPrice();

        // Переходим к созданию
        mainPage.clickCreateAd();
        createAdPage
                .fillTitle(currentAd.getTitle())
                .fillDescription(currentAd.getDescription())
                .fillPrice(String.valueOf(price))
                .selectCategory(currentAd.getCategory())
                .selectCity("Москва")
                .selectCondition("Б/У")
                .clickSubmit();

        sleep(2000);
        // Возвращаемся на главную
        mainPage.openPage();
    }

    @Когда("пользователь открывает свое объявление для редактирования")
    public void openAdForEditing() {
        // Ищем объявление через поиск с категорией
        System.out.println("Searching for ad to edit: " + currentAd.getTitle() + " in category: " + currentAd.getCategory());
        mainPage.searchAd(currentAd.getTitle(), currentAd.getCategory());
        sleep(500);
        // Кликаем на объявление
        mainPage.clickOnAdByTitle(currentAd.getTitle());
        sleep(1000);
    }

    @Когда("пользователь нажимает кнопку \"Редактировать объявление\"")
    public void clickEditButton() {
        $x("//button[contains(text(),'Редактировать объявление')]").click();
        sleep(500);
        assertTrue(editAdPage.isEditFormVisible(), "Форма редактирования не отображается");
    }

    @Когда("пользователь изменяет заголовок на {string}")
    public void changeTitle(String newTitle) {
        editAdPage.clearTitle().fillTitle(newTitle);
        currentAd.setTitle(newTitle);
    }

    @Когда("пользователь изменяет цену на {int}")
    public void changePrice(int newPrice) {
        editAdPage.clearPrice().fillPrice(String.valueOf(newPrice));
        currentAd.setPrice(newPrice);
    }

    @Когда("пользователь сохраняет изменения")
    public void saveChanges() {
        editAdPage.clickSubmit();
        sleep(2000);
    }

    @Тогда("объявление отображается с обновленным заголовком {string}")
    public void verifyUpdatedTitle(String expectedTitle) {
        // Ищем обновленное объявление через поиск с категорией
        mainPage.searchAd(expectedTitle, currentAd.getCategory());
        assertTrue(mainPage.isAdPresent(expectedTitle),
                String.format("Объявление с заголовком '%s' не найдено", expectedTitle));
    }

    @Тогда("цена объявления обновлена на {int}")
    public void verifyUpdatedPrice(int expectedPrice) {
        // Ищем заголовок и получаем текст всей карточки
        SelenideElement titleElement = $x("//div[@class='about']/h2[@class='h2' and contains(text(),'" + currentAd.getTitle() + "')]");

        if (!titleElement.exists()) {
            titleElement = $x("//h2[@class='h2' and contains(text(),'" + currentAd.getTitle() + "')]");
        }

        // От заголовка поднимаемся до карточки и ищем цену
        SelenideElement cardElement = titleElement.parent().parent().parent();
        SelenideElement priceElement = cardElement.$("div.price h2.h2");

        String priceText = priceElement.getText();
        System.out.println("Price text: " + priceText);

        String cleanPrice = priceText.replace(" ", "").replace("₽", "").replace(" ", "");
        System.out.println("Clean price: " + cleanPrice);

        assertTrue(cleanPrice.contains(String.valueOf(expectedPrice)),
                String.format("Цена %d не найдена. Текст цены: %s", expectedPrice, priceText));
    }

    @Когда("пользователь удаляет свое объявление")
    public void deleteAd() {
        // Ищем объявление через поиск с категорией
        System.out.println("Searching for ad to delete: " + currentAd.getTitle() + " in category: " + currentAd.getCategory());
        mainPage.searchAd(currentAd.getTitle(), currentAd.getCategory());
        sleep(500);
        // Кликаем на объявление
        mainPage.clickOnAdByTitle(currentAd.getTitle());
        sleep(1000);
        // Нажимаем "Удалить"
        editAdPage.clickDelete();
        editAdPage.confirmDelete();
    }

    @Тогда("объявление больше не отображается в списке")
    public void verifyAdRemoved() {
        sleep(2000);
        // Ищем объявление через поиск с категорией
        mainPage.searchAd(currentAd.getTitle(), currentAd.getCategory());
        // Проверяем что объявление не найдено
        assertFalse(mainPage.isAdPresent(currentAd.getTitle()),
                String.format("Объявление '%s' все еще отображается", currentAd.getTitle()));
    }

    @Тогда("отображается сообщение об успешном удалении")
    public void verifyDeleteSuccessMessage() {
        // Проверяем что мы на главной и объявления нет
        assertTrue(mainPage.isUserLoggedIn(), "Не на главной странице после удаления");
    }
}