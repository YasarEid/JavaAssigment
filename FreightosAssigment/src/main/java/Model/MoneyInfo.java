package Model;

import enums.Money.Currency;
import enums.Money.MoneyTypes;
import lombok.Data;

@Data
public class MoneyInfo {
    private final MoneyTypes type;
    private final Double value;
    private final Currency currency;


    public MoneyInfo(MoneyTypes type, Double value, Currency currency) {
        this.type = type;
        this.value = value;
        this.currency = currency;
    }

}
