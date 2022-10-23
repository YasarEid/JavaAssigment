package Model;

import enums.Money.Currency;
import enums.Money.MoneyTypes;
import lombok.Data;

@Data
public class Card implements Money{
    private MoneyInfo money;

    Card(Double value, Currency currency) {

        this.money = new MoneyInfo(MoneyTypes.CARD, value, currency);
    }

    public MoneyInfo getMoney() {
        return money;
    }

}
