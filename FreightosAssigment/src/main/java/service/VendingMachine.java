package service;

import Model.Item;
import Model.Keypad;
import Model.MoneyInfo;

import java.util.List;

public interface VendingMachine {
    public void selectItemAndGetPrice(Keypad keypadLocation);
    public void insertMoney(MoneyInfo money);
    public boolean validateMoney(MoneyInfo money);
    public Item getItem(Keypad keypadLocation);
    public void collectItemAndChange(Keypad keypadLocation, MoneyInfo money);
    public void reset();
}
