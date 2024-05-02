import java.util.List;

public class Main {
    public static void main(String[] args) {
        Verification verification = new Verification();
        System.out.println((verification.verify("((A/\\A)~(B\\/B))")));
    }
}

class Verification {

    final char constAtom = 'A';
    final List<String> operators = List.of("/\\", "\\/", "->", "(", ")", "~", "!");
    final List<Character> atomFormula = List.of('0', '1', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');


    public boolean verify(String formula) {
        return check_symbols(formula) && syntax(formula);
    }


    public boolean check_symbols(String formula) {
        for (int i = 0; i < formula.length(); i++) {
            List<Boolean> connective = check_connective(formula, i);
            if (!check_atom(formula, i) && !connective.getFirst()) {
                System.out.println("Incorrect");
                return false;
            }
            if (connective.getLast()) i++;
        }
        return true;
    }


    public boolean check_atom(String formula, int index) {
        for (Character character : atomFormula)
            if (formula.charAt(index) == character)
                return true;
        return false;
    }


    public List<Boolean> check_connective(String formula, int index) {
        if ((index + 1) < formula.length()) {
            if (formula.charAt(index) == '/' && formula.charAt(index + 1) == '\\') {
                return List.of(true, true);
            }
            if (formula.charAt(index) == '\\' && formula.charAt(index + 1) == '/') {
                return List.of(true, true);
            }
            if (formula.charAt(index) == '-' && formula.charAt(index + 1) == '>') {
                return List.of(true, true);
            }
            if (formula.charAt(index) == '~' || formula.charAt(index) == '(' || formula.charAt(index) == '!') {
                return List.of(true, false);
            }
        }
        if (formula.charAt(index) == ')') return List.of(true, false);
        return List.of(false, false);
    }


    public int get_number_of_brackets(String formula) {
        int result = 0;
        for (int i = 0; i < formula.length(); i++)
            if (formula.charAt(i) == '(' || formula.charAt(i) == ')') result++;
        return result;
    }


    public boolean syntax(String formula) {
        int number = get_number_of_brackets(formula);
        while (number != 0) {
            for (int i = 0; i < formula.length(); i++) {
                if (formula.charAt(i) == '(') {
                    for (int j = i + 1; j < formula.length(); j++) {
                        if (formula.charAt(j) == '(') break;
                        if (formula.charAt(j) == ')') {
                            if ((check_atom(formula, i + 1) && check_atom(formula, j - 1) && check_connective(formula, i + 2).getFirst()) ||
                                    check_atom(formula, j - 1) && check_connective(formula, i + 1).getFirst()) {
                                if ((check_connective(formula, i + 2).getLast() && j == 1 + 5) ||
                                        (!check_connective(formula, i + 2).getLast() && j == 1 + 4) ||
                                        (!check_connective(formula, i + 1).getLast() && j == i + 3)) {
                                    while (i != j) {
                                        StringBuilder stringBuilder = new StringBuilder(formula);
                                        stringBuilder.setCharAt(i, ' ');
                                        formula = stringBuilder.toString();
                                        i++;
                                    }
                                }
                                StringBuilder stringBuilder = new StringBuilder(formula);
                                stringBuilder.setCharAt(j, constAtom);
                                formula = stringBuilder.toString();
                                formula = formula.replace(" ", "");
                            }
                            break;
                        }
                    }
                }
            }
            number--;
        }
        if (formula.length() == 1) return true;
        else {
            System.out.println("Syntax Error");
            return false;
        }
    }
}