package Model;

import lombok.Data;

@Data
public class Keypad {
    Character row;
    Character column;

    public Keypad(Character row, Character column) {
        this.row = row;
        this.column = column;
    }

}
