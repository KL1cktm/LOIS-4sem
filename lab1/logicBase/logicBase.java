package logicBase;

import java.util.ArrayList;
import java.util.List;

public class logicBase {
    private String logicFunction;
    private List<Character> alphabet = List.of('0', '1', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');

    public void checkImpracticabilityForm(String formula) {
        this.logicFunction = formula;

    }
}
