package service;

import Model.Item;
import Model.Keypad;
import enums.VendingMachineTypes;

import java.util.Map;

public class VendingMachineFactory {
    public static VendingMachine createVendingMachine(VendingMachineTypes type, Map<Keypad, Item> store, Printer printer) {

        if (type == null)
            return null;
        switch (type) {
            case SNACK:
                return new SnackMachine(store, printer);
            default:
                throw new IllegalArgumentException("Unknown type "+type);
        }
    }
}
