package ru.netology.web.pade;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private final SelenideElement verificationField = $("[data-test-id='code'] input");
    private final SelenideElement verificationButton = $("[data-test-id=action-verify]");

    public VerificationPage() {
        verificationField.should(Condition.visible);
    }


    public DashBoardPage validVerification(DataHelper.VerificationCode verificationCode) {
        verificationField.setValue(verificationCode.getCode());
        verificationButton.click();
        return new DashBoardPage();

    }


}
