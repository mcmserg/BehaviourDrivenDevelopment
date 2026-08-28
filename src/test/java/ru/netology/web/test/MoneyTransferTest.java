package ru.netology.web.test;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.pade.*;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.getAuthInfo;

class MoneyTransferTest {

    private DashBoardPage dashBoardPage;
    private DataHelper.CardInfo firstCard;
    private DataHelper.CardInfo secondCard;
    private int initialFirstCardBalance;
    private int initialSecondCardBalance;

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        var info = getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(info);
        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();
        var loginPage = Selenide.open("http://localhost:9999", LoginPageV1.class);
        var verificationPage = loginPage.validLogin(info);
        var dashBoardPage = verificationPage.validVerification(verificationCode);
        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();
        $("h1").shouldHave(Condition.exactText("Ваши карты")).shouldBe(Condition.visible);

        initialFirstCardBalance = dashBoardPage.getCardBalance(firstCard);
        initialSecondCardBalance = dashBoardPage.getCardBalance(secondCard);


    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsV1() throws InterruptedException {
        // 1. Получение данных для авторизации
        var info = getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(info);
        var firstCard = DataHelper.getFirstCard();
        var secondCard = DataHelper.getSecondCard();
        var loginPage = Selenide.open("http://localhost:9999", LoginPageV1.class);
        var verificationPage = loginPage.validLogin(info);
        var dashBoardPage = verificationPage.validVerification(verificationCode);

        // 3. Проверка, что мы на странице "Личный кабинет"
        $("h1").shouldHave(Condition.exactText("Ваши карты")).shouldBe(Condition.visible);

        // 4. Получение балансов до перевода
        int balanceBeforeFirst = dashBoardPage.getCardBalance(firstCard);
        int balanceBeforeSecond = dashBoardPage.getCardBalance(secondCard);

        System.out.println("💰 Баланс карты 0001 до перевода: " + balanceBeforeFirst + " RUB");
        System.out.println("💰 Баланс карты 0002 до перевода: " + balanceBeforeSecond + " RUB");

        // 5. Нажатие на кнопку "Пополнить" для второй карты (куда будем переводить)
        TransferPage transferPage = dashBoardPage.selectCardForReplenishment(secondCard);

        // 6. Ожидание появления формы "Пополнение карты"
        $("h1").shouldHave(Condition.exactText("Пополнение карты")).shouldBe(Condition.visible);

        // 7. Задержка 4 секунды (по вашему требованию)
        System.out.println("⏳ Ожидание 4 секунды перед переводом...");
        Thread.sleep(4000);

        // 8. Перевод 1000 рублей с первой карты на вторую
        int amount = 1000;
        dashBoardPage = transferPage.transferMoney(amount, firstCard);

        // 9. Получение балансов после перевода
        int balanceAfterFirst = dashBoardPage.getCardBalance(firstCard);
        int balanceAfterSecond = dashBoardPage.getCardBalance(secondCard);

        System.out.println("💰 Баланс карты 0001 после перевода: " + balanceAfterFirst + " RUB");
        System.out.println("💰 Баланс карты 0002 после перевода: " + balanceAfterSecond + " RUB");

        // 10. Проверки
        assertEquals(balanceBeforeFirst - amount, balanceAfterFirst,
                "Баланс первой карты должен уменьшиться на " + amount + " RUB");
        assertEquals(balanceBeforeSecond + amount, balanceAfterSecond,
                "Баланс второй карты должен увеличиться на " + amount + " RUB");

        System.out.println("✅ Тест пройден! Перевод " + amount + " RUB выполнен успешно.");
    }


}
