package enums.Money;

import Model.Money;
import Model.MoneyInfo;

public enum Note implements Money {
    TWENTY_DOLLAR(20.0, Currency.USD),
    FIFTY_DOLLAR(50.0,Currency.USD);

    MoneyInfo money;

    Note(Double value, Currency currency) {

        this.money = new MoneyInfo(MoneyTypes.NOTE, value, currency);
    }
    public MoneyInfo getMoney() {
        return money;
    }

}
