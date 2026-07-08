package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class MainPage extends BasePage {
    private final ElementsCollection adCards = $$x("//h2[@class='h2']");
    // Кнопка пагинации "вправо" - точный XPath
    private final SelenideElement nextPageButton = $x("/html/body/div/div/div[2]/div[3]/button[2]");
    // Модальное окно
    private final SelenideElement modalOverlay = $("div.homePage_modal__zSdUB");
    private final SelenideElement modalCloseButton = $x("//div[contains(@class,'modal')]//button | //div[contains(@class,'modal')]//*[contains(@class,'close')]");

    public MainPage openPage() {
        com.codeborne.selenide.Selenide.open("/");
        waitForPageLoad();
        closeModalIfPresent();
        return this;
    }

    private void closeModalIfPresent() {
        sleep(500);
        if (modalOverlay.exists()) {
            System.out.println("Modal detected, closing...");
            if (modalCloseButton.exists()) {
                modalCloseButton.click();
            } else {
                modalOverlay.click();
            }
            sleep(500);
        }
    }

    /**
     * Перейти на последнюю страницу с объявлениями
     */
    public MainPage goToLastPage() {
        closeModalIfPresent();
        int attempts = 0;
        // Проверяем что кнопка существует И не disabled
        while (nextPageButton.exists() && !nextPageButton.has(attribute("disabled")) && attempts < 20) {
            nextPageButton.scrollIntoView(true);
            nextPageButton.click();
            sleep(500);
            attempts++;
        }
        System.out.println("Went to last page after " + attempts + " clicks");
        return this;
    }

    public void clickOnAdByTitle(String title) {
        closeModalIfPresent();
        // Ищем точный заголовок в карточке
        SelenideElement ad = $x("//div[@class='about']/h2[@class='h2' and contains(text(),'" + title + "')]");
        if (!ad.exists()) {
            // Запасной вариант - ищем просто h2
            ad = $x("//h2[@class='h2' and contains(text(),'" + title + "')]");
        }
        ad.shouldBe(visible).click();
    }

    public boolean isAdPresent(String title) {
        SelenideElement ad = $x("//div[@class='about']/h2[@class='h2' and contains(text(),'" + title + "')]");
        if (ad.exists()) return true;
        // Запасной вариант
        return $x("//h2[@class='h2' and contains(text(),'" + title + "')]").exists();
    }

    public SelenideElement getAdByTitle(String title) {
        // Ищем заголовок в div.about и возвращаем всю карточку
        SelenideElement titleElement = $x("//div[@class='about']/h2[@class='h2' and contains(text(),'" + title + "')]");
        if (titleElement.exists()) {
            // Возвращаем карточку (3 уровня вверх: about -> description -> card)
            return titleElement.parent().parent().parent();
        }
        // Запасной вариант
        return $x("//h2[@class='h2' and contains(text(),'" + title + "')]");
    }

    public int getAdsCount() {
        return adCards.size();
    }

    public MainPage clickCreateAd() {
        closeModalIfPresent();
        createAdButton.shouldBe(visible).click();
        return this;
    }

    public MainPage waitForPageLoad() {
        $("body").shouldBe(visible);
        return this;
    }
}