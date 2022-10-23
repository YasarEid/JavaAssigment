package service;

import Model.Item;
import Model.Keypad;
import Model.MoneyInfo;
import enums.Money.Currency;
import enums.Money.MoneyTypes;
import enums.VendingMachineTypes;
import exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SnackMachineTest {

    @InjectMocks
    SnackMachine snackMachine;
    @Spy
    Printer printer;

    Map<Keypad, Item> store;

    @BeforeEach
    void setUp() {
        store = new HashMap<>();
        store.put(new Keypad('1', '1'),
                new Item(new MoneyInfo(MoneyTypes.PRICE, 9.0, Currency.USD), 10));
        store.put(new Keypad('1', '2'),
                new Item(new MoneyInfo(MoneyTypes.PRICE, 12.0, Currency.USD), 5));
        store.put(new Keypad('1', '3'),
                new Item(new MoneyInfo(MoneyTypes.PRICE, 1.0, Currency.USD), 5));
        store.put(new Keypad('1', '4'),
                new Item(new MoneyInfo(MoneyTypes.PRICE, 0.2, Currency.USD), 5));

        VendingMachine vendingMachine = VendingMachineFactory.createVendingMachine(VendingMachineTypes.SNACK, store, printer);
        if (vendingMachine instanceof SnackMachine){
            snackMachine = (SnackMachine) VendingMachineFactory.createVendingMachine(VendingMachineTypes.SNACK, store, printer);
            snackMachine.reset();
        }else {
            throw new NullMoneyException();
        }
    }

    @Test
    public void reset_Test1() {
        snackMachine.reset();
        assertEquals(snackMachine.getCurrentBalance(), 0.0);
        assertEquals(snackMachine.getCurrentItem(), null);
    }


    @Test
    public void isItemExist_Test1() {
        assertFalse(snackMachine.isItemExist(new Keypad('2', '1')));
    }

    @Test
    public void isItemExist_Test2() {
        assertTrue(snackMachine.isItemExist(new Keypad('1', '1')));
    }

    @Test
    public void isFullPaid_Test1() {
        MoneyInfo money = new MoneyInfo(MoneyTypes.COIN, 1.0, Currency.USD);
        snackMachine.insertMoney(money);
        assertTrue(snackMachine.isFullPaid(new Keypad('1', '3')));
    }

    @Test
    void dispense_Test1() {
        Keypad keypadLocation = new Keypad('1', '2');
        Item item = snackMachine.getItem(keypadLocation);
        Integer previousValue = item.getAmount();
        snackMachine.dispense(keypadLocation);
        assertEquals(previousValue-1,item.getAmount());
    }

    @Test
    public void selectItemAndGetPrice_Test1() {
        Keypad keypadLocation = new Keypad('5', '5');
        assertThrows(ItemDoesNotExist.class, () -> snackMachine.selectItemAndGetPrice(keypadLocation));
    }

    @Test
    public void selectItemAndGetPrice_Test2() {
        Keypad keypadLocation = new Keypad('1', '1');
        snackMachine.selectItemAndGetPrice(keypadLocation);
        verify(printer).print("The item is available and its price is 9.0");
    }

    @Test
    public void validateMoney_Test1() {
        MoneyInfo money = null;
        assertThrows(NullMoneyException.class, () -> snackMachine.validateMoney(money));
    }
    @Test
    public void validateMoney_Test2() {
        MoneyInfo money = new MoneyInfo(MoneyTypes.COIN, 5.0, Currency.NIS);
        assertThrows(CurrencyNotSupportedException.class, () -> snackMachine.validateMoney(money));
    }
    @Test
    public void validateMoney_Test3() {
        MoneyInfo money = new MoneyInfo(MoneyTypes.COIN, 5.0, Currency.USD);
        assertThrows(InvalidMoneyException.class, () -> snackMachine.validateMoney(money));
    }

    @Test
    public void insertMoney_Test1() {
        MoneyInfo money = new MoneyInfo(MoneyTypes.COIN, 1.0, Currency.USD);
        snackMachine.insertMoney(money);
        assertEquals(snackMachine.getCurrentBalance(), money.getValue());
    }

    @Test
    public void insertMoney_Test2() {
        MoneyInfo money = new MoneyInfo(MoneyTypes.NOTE, 20.0, Currency.USD);
        snackMachine.insertMoney(money);
        assertEquals(snackMachine.getCurrentBalance(), money.getValue());
    }

    @Test
    public void insertMoney_Test3() {
        MoneyInfo money = new MoneyInfo(MoneyTypes.CARD, 37.0, Currency.USD);
        snackMachine.insertMoney(money);
        assertEquals(snackMachine.getCurrentBalance(), money.getValue());
    }


    @Test
    public void collectChange_Test1() {
        MoneyInfo money = new MoneyInfo(MoneyTypes.NOTE, 50.0, Currency.USD);
        snackMachine.insertMoney(money);
        assertEquals(snackMachine.getCurrentBalance(), money.getValue());
        List<Double> expectedResult = Arrays.asList(20.0, 20.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5, 0.2, 0.1);
        assertIterableEquals(snackMachine.collectChange(new Keypad('1', '4')),expectedResult);
    }



    @Test
    public void collectItemAndChange_Test1() {
        MoneyInfo money = new MoneyInfo(MoneyTypes.NOTE, 50.0, Currency.USD);
        Keypad keypadLocation = new Keypad('1', '1');
        Item item = snackMachine.getItem(keypadLocation);
        Integer previousValue = item.getAmount();
        snackMachine.collectItemAndChange(keypadLocation, money);

        assertEquals(previousValue-1,item.getAmount());
        assertEquals(snackMachine.getCurrentBalance(), 0.0);
        assertEquals(snackMachine.getCurrentItem(), null);
    }

    @Test
    public void collectItemAndChange_Test2() {
        MoneyInfo money = new MoneyInfo(MoneyTypes.CARD, 1.0, Currency.USD);
        Keypad keypadLocation = new Keypad('1', '1');
        snackMachine.collectItemAndChange(keypadLocation, money);
        Double remainingBalance = snackMachine.getCurrentItem().getMoney().getValue() - snackMachine.getCurrentBalance();

        verify(printer).print("Price not full paid, remaining : " + remainingBalance);
    }
}