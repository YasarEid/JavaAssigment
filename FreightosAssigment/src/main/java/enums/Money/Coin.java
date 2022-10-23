package enums.Money;

import Model.Money;
import Model.MoneyInfo;

public enum Coin implements Money {
    TEN_CENT(0.10, Currency.USD),
    TWENTY_CENT(0.20, Currency.USD),
    FIFTY_CENT(0.50, Currency.USD),
    ONE_DOLLAR(1.0, Currency.USD);

    MoneyInfo money;

    Coin(Double value, Currency currency) {
        this.money = new MoneyInfo(MoneyTypes.COIN, value, currency);
    }

    public MoneyInfo getMoney() {
        return money;
    }

}
