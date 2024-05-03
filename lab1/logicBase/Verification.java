package logicBase;

import java.util.ArrayList;
import java.util.List;

class Verification {
    public static void main(String[] args) {
        Verification verification = new Verification();
        System.out.println((verification.verify("((A/\\A)~(B\\/B))")));
    }

    private static final List<String> connectives = new ArrayList<>();
    private static final List<Character> atomFormula = new ArrayList<>();

    static {
        connectives.add("/\\");
        connectives.add("\\/");
        connectives.add("->");
        connectives.add("(");
        connectives.add(")");
        connectives.add("~");
        connectives.add("!");

        atomFormula.add('0');
        atomFormula.add('1');
        for (char c = 'A'; c <= 'Z'; c++) {
            atomFormula.add(c);
        }
    }

    private static int getNumberOfBrackets(String formula) {
        int result = 0;
        for (char c : formula.toCharArray()) {
            if (c == '(' || c == ')') {
                result++;
            }
        }
        return result;
    }

    private static boolean syntax(String formula) {
        int bracketCount = getNumberOfBrackets(formula);
        while (bracketCount > 0) {
            for (int i = 0; i < formula.length(); i++) {
                if (formula.charAt(i) == '(') {
                    for (int j = i + 1; j < formula.length(); j++) {
                        if (formula.charAt(j) == '(') break;
                        if (formula.charAt(j) == ')') {
                            if ((checkAtom(formula, i + 1) && checkAtom(formula, j - 1) && checkConnective(formula, i + 2).first()) ||
                                    (checkAtom(formula, j - 1) && checkConnective(formula, i + 1).first())) {
                                if ((checkConnective(formula, i + 2).second() == 1 && j == i + 5) ||
                                        (checkConnective(formula, i + 2).second() == 0 && j == i + 4) ||
                                        (checkConnective(formula, i + 1).second() == 0 && j == i + 3)) {
                                    while (i != j) {
                                        formula = formula.substring(0, i) + " " + formula.substring(i + 1);
                                        i++;
                                    }
                                }
                                formula = formula.substring(0, j) + "X" + formula.substring(j + 1);
                                formula = formula.replaceAll("\\s+", "");
                            }
                            break;
                        }
                    }
                }
            }
            bracketCount--;
        }
        if (formula.length() == 1) {
            return true;
        } else {
            System.out.println("Syntax error");
            return false;
        }
    }

    public static boolean verify(String formula) {
        return checkSymbols(formula) && syntax(formula);
    }

    private static boolean checkAtom(String formula, int index) {
        for (char c : atomFormula) {
            if (formula.charAt(index) == c) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkSymbols(String formula) {
        for (int i = 0; i < formula.length(); i++) {
            Pair<Boolean, Integer> connective = checkConnective(formula, i);
            if (!checkAtom(formula, i) && !connective.first) {
                System.out.println("Incorrect symbols");
                return false;
            }
            if (connective.second > 0) {
                i += connective.second;
            }
        }
        return true;
    }

    private static Pair<Boolean, Integer> checkConnective(String formula, int index) {
        if (index + 1 < formula.length()) {
            if (formula.charAt(index) == '/' && formula.charAt(index + 1) == '\\') {
                return new Pair<>(true, 1);
            }
            if (formula.charAt(index) == '\\' && formula.charAt(index + 1) == '/') {
                return new Pair<>(true, 1);
            }
            if (formula.charAt(index) == '-' && formula.charAt(index + 1) == '>') {
                return new Pair<>(true, 1);
            }
            if (formula.charAt(index) == '~' || formula.charAt(index) == '(' || formula.charAt(index) == '!') {
                return new Pair<>(true, 0);
            }
        }
        if (formula.charAt(index) == ')') {
            return new Pair<>(true, 0);
        }
        return new Pair<>(false, 0);
    }

    private record Pair<T, U>(T first, U second) {
    }
}