package service;

import Model.*;
import enums.Money.Coin;
import enums.Money.Currency;
import enums.Money.MoneyTypes;
import enums.Money.Note;
import exception.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SnackMachine implements VendingMachine {

    private Map<Keypad, Item> store = new HashMap<>();

    private Printer printer;

    public SnackMachine(Map<Keypad, Item> store, Printer printer) {

        this.store = store;
        this.printer = printer;
    }

    private Item currentItem;
    private Double currentBalance;
    private List<Double> acceptedMoney =
            Stream
                    .concat(
                            Arrays.stream(Coin.values()),
                            Arrays.stream(Note.values()))
                    .map(money -> ((Money) money).getMoney().getValue())
                    .sorted(Comparator.reverseOrder())
                    .collect(Collectors.toList());


    @Override
    public void selectItemAndGetPrice(Keypad keypadLocation) {
        if (isItemExist(keypadLocation)) {
            currentItem = getItem(keypadLocation);
            printer.print("The item is available and its price is " + currentItem.getMoney().getValue());
            return;
        }
        throw new ItemDoesNotExist();
    }

    @Override
    public void insertMoney(MoneyInfo money) {
        validateMoney(money);
        currentBalance = currentBalance + money.getValue();
    }

    public boolean isItemExist(Keypad keypadLocation) {
        if (store.containsKey(keypadLocation) &&
                getItem(keypadLocation).getAmount() > 0) {
            return true;
        }
        return false;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    @Override
    public boolean validateMoney(MoneyInfo money) {
        if (money == null) {
            throw new NullMoneyException();
        }
        if (money.getCurrency() != Currency.USD) {
            throw new CurrencyNotSupportedException();
        }
        if (money.getType() != MoneyTypes.CARD) {
            if (acceptedMoney.indexOf(money.getValue()) == -1) {
                throw new InvalidMoneyException();
            }
        }
        return true;
    }

    @Override
    public void collectItemAndChange(Keypad keypadLocation, MoneyInfo money) {
        selectItemAndGetPrice(keypadLocation);
        insertMoney(money);
        List<Double> changes = null;
        if (isFullPaid(keypadLocation)) {
            if (money.getType() != MoneyTypes.CARD) {
                changes = collectChange(keypadLocation);
            } else {
                changes.add(deductFromCard(keypadLocation));
            }
            dispense(keypadLocation);
            reset();
            return;
        }
        Double remainingBalance = currentItem.getMoney().getValue() - currentBalance;
        printer.print("Price not full paid, remaining : " + remainingBalance);
    }


    public List<Double> collectChange(Keypad keypadLocation) {

        Double amount = currentBalance - store.get(keypadLocation).getMoney().getValue();

        List<Double> changes = Collections.EMPTY_LIST;
        if (amount > 0) {
            changes = new ArrayList<Double>();
            double balance = amount;
            int i = 0;
            while (balance > 0 ) {
                if (balance >= acceptedMoney.get(i)) {
                    changes.add(acceptedMoney.get(i));
                    balance = balance - acceptedMoney.get(i);
                    balance =Double.parseDouble(new DecimalFormat("##.#").format(balance));
                    continue;
                }
                i++;
            }
        }
        return changes;
    }


    public Double deductFromCard(Keypad keypadLocation) {
        return currentBalance - store.get(keypadLocation).getMoney().getValue();
    }

    public void dispense(Keypad keypadLocation) {
        store.get(keypadLocation).setAmount(store.get(keypadLocation).getAmount() - 1);
    }

    @Override
    public Item getItem(Keypad keypadLocation) {
        return store.get(keypadLocation);
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public boolean isFullPaid(Keypad keypadLocation) {
        if (currentBalance >= getItem(keypadLocation).getMoney().getValue()) {
            return true;
        }
        return false;
    }


    @Override
    public void reset() {
        currentItem = null;
        currentBalance = 0.0;
    }


}