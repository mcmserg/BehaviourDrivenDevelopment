package ru.netology.web.pade;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashBoardPage {
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";
    private final SelenideElement header = $("[data-test-id=dashboard]");

    public DashBoardPage() {
        header.should(Condition.visible).should(Condition.text("Личный кабинет"));
    }

    private SelenideElement getCard(DataHelper.CardInfo cardInfo){
        return cards.find(Condition.attribute("data-test-id", cardInfo.getTestId()));
    }
    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        String text = getCard(cardInfo).text();
        return extractBalance(text);
    }

    public TransferPage selectCard(DataHelper.CardInfo cardInfo) {
        getCard(cardInfo).$("button").click();
        return new TransferPage();
    }


    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        // Удаляем пробелы и заменяем запятую на точку (если есть)
            value = value.replaceAll("\\s+", "").replace(",", ".");
        // Если есть копейки, преобразуем их в целые рубли
        if (value.contains(".")) {
            value = value.substring(0, value.indexOf("."));
        }

        return Integer.parseInt(value);
    }
    public TransferPage selectCardForReplenishment(DataHelper.CardInfo cardInfo) {
        return selectCard(cardInfo);
    }

    public DashBoardPage refresh() {
        SelenideElement refreshButton = $("[data-test-id='action-reload']");
        refreshButton.click();
        return this;
    }



}