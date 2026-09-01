package ru.netology.web.pade;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement amountField = $("[data-test-id='amount'] input");
    private final SelenideElement fromField = $("[data-test-id='from'] input");
    private final SelenideElement toField = $("[data-test-id='to'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement errorMessage = $("[data-test-id='error-notification']");

    public TransferPage() {
        amountField.should(Condition.visible);
    }

    public  DashBoardPage makeValidTransfer(String amountToTransfer, DataHelper.CardInfo cardInfo) {
        makeValidTransfer(amountToTransfer, cardInfo);
        return  new DashBoardPage();
    }

    public DashBoardPage transferMoney(int amount, DataHelper.CardInfo cardInfo) {
        amountField.setValue(String.valueOf(amount));
        fromField.setValue(cardInfo.getCardNumber());
        transferButton.click();
        return new DashBoardPage();
    }
    public void waitForTransferPage() {
        $("h1").shouldHave(Condition.exactText("Пополнение карты"))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
    }



}