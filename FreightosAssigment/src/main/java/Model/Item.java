package Model;

import enums.Money.Currency;
import lombok.Data;

@Data
public class Item {

    private MoneyInfo money;
    private Integer amount;

    public Item(MoneyInfo money, Integer amount) {
        this.money = money;
        this.amount = amount;
    }

    public MoneyInfo getMoney() {
        return money;
    }

    public void setMoney(MoneyInfo money) {
        this.money = money;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
