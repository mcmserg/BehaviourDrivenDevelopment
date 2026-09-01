package ru.netology.web.test;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        // Авторизация перед каждым тестом
        var info = getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(info);
        firstCard = DataHelper.getFirstCard();
        secondCard = DataHelper.getSecondCard();

        var loginPage = Selenide.open("http://localhost:9999", LoginPageV1.class);
        var verificationPage = loginPage.validLogin(info);
        dashBoardPage = verificationPage.validVerification(verificationCode);


        dashBoardPage.waitForDashboardPage();
    }

    @Test
    void shouldTransferMoneyBetweenOwnCards() {
        int initialFirstCardBalance = dashBoardPage.getCardBalance(firstCard);
        int initialSecondCardBalance = dashBoardPage.getCardBalance(secondCard);

        int amount = 1000;
        TransferPage transferPage = dashBoardPage.selectCardForReplenishment(secondCard);
        transferPage.waitForTransferPage();
        dashBoardPage = transferPage.transferMoney(amount, firstCard);

        int finalFirstCardBalance = dashBoardPage.getCardBalance(firstCard);
        int finalSecondCardBalance = dashBoardPage.getCardBalance(secondCard);

        assertEquals(initialFirstCardBalance - amount, finalFirstCardBalance,
                "Баланс первой карты должен уменьшиться на " + amount + " RUB");
        assertEquals(initialSecondCardBalance + amount, finalSecondCardBalance,
                "Баланс второй карты должен увеличиться на " + amount + " RUB");
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsWithDifferentAmount() {

        int initialFirstCardBalance = dashBoardPage.getCardBalance(firstCard);
        int initialSecondCardBalance = dashBoardPage.getCardBalance(secondCard);

        int amount = 500;
        TransferPage transferPage = dashBoardPage.selectCardForReplenishment(secondCard);
        transferPage.waitForTransferPage();
        dashBoardPage = transferPage.transferMoney(amount, firstCard);

        int finalFirstCardBalance = dashBoardPage.getCardBalance(firstCard);
        int finalSecondCardBalance = dashBoardPage.getCardBalance(secondCard);

        assertEquals(initialFirstCardBalance - amount, finalFirstCardBalance,
                "Баланс первой карты должен уменьшиться на " + amount + " RUB");
        assertEquals(initialSecondCardBalance + amount, finalSecondCardBalance,
                "Баланс второй карты должен увеличиться на " + amount + " RUB");
    }
}
