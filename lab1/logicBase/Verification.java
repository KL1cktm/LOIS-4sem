package logicBase;

import java.util.ArrayList;
import java.util.List;

class Verification {
    public static void main(String[] args) {
        Verification verification = new Verification();
        System.out.println((verification.verify("(((A\\/B~(!C))/\\((C/\\D)~(A/\\B)))/\\((F/\\A)~(!C)))")));
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
            if (c == '(' || c == ')') { // Считаем количество всех скобок
                result++;
            }
        }
        return result;
    }

    private static boolean syntax(String formula) {
        int bracketCount = getNumberOfBrackets(formula);
        while (bracketCount > 0) {
            for (int i = 0; i < formula.length(); i++) {
                if (formula.charAt(i) == '(') { // Ищем структуру ( ... ), чтобы внутри не было скобок лишних
                    for (int j = i + 1; j < formula.length(); j++) {
                        if (formula.charAt(j) == '(') break;
                        if (formula.charAt(j) == ')') {
                            if ((checkAtom(formula, i + 1) && checkAtom(formula, j - 1) && checkConnective(formula, i + 2).first()) || // Если бинарная формула
                                    (checkAtom(formula, j - 1) && checkConnective(formula, i + 1).first())) { // Если унарная формула
                                if ((checkConnective(formula, i + 2).second() == 1 && j == i + 5) || // Если бинарная и связка занимает два символа
                                        (checkConnective(formula, i + 2).second() == 0 && j == i + 4) || // Если бинарная и связка занимает два символа
                                        (checkConnective(formula, i + 1).second() == 0 && j == i + 3)) { // Если унарная
                                    while (i != j) {
                                        formula = formula.substring(0, i) + " " + formula.substring(i + 1); // Замена подформулы на пробелы
                                        i++;
                                    }
                                }
                                formula = formula.substring(0, j) + "X" + formula.substring(j + 1); // Заменяем подформулу на атомарную
                                formula = formula.replaceAll("\\s+", ""); // Удаляем проблемы
                            }
                            break;
                        }
                    }
                }
            }
            bracketCount--;
        }
        if (formula.length() == 1) { // Если смогли видоизменить формулу, то она корректна
            return true;
        } else { // Иначе есть ошибки
            System.out.println("Syntax error");
            return false;
        }
    }

    public static boolean verify(String formula) { // Если синтаксис и атомы корректные
        return checkSymbols(formula) && syntax(formula);
    }

    private static boolean checkAtom(String formula, int index) {
        for (char c : atomFormula) {
            if (formula.charAt(index) == c) { // Если совпадает
                return true;
            }
        }
        return false;
    }

    private static boolean checkSymbols(String formula) {
        for (int i = 0; i < formula.length(); i++) {
            Pair<Boolean, Integer> connective = checkConnective(formula, i); // Сохраняем данные о связке (если она существует, 1 - занимает 2 символа ; 0 - один символ
            if (!checkAtom(formula, i) && !connective.first) { // Если символ не атом и не связка
                System.out.println("Incorrect symbols");
                return false;
            }
            if (connective.second > 0) {
                // i += connective.second
                i++; // Если связка занимает два символа, инкремент
            }
        }
        return true;
    }

    private static Pair<Boolean, Integer> checkConnective(String formula, int index) {
        if (index + 1 < formula.length()) { // Следующие символы не могут стоять в самом конце строки
            if (formula.charAt(index) == '/' && formula.charAt(index + 1) == '\\') { // Для конъюкции, занимает два символа
                return new Pair<>(true, 1);
            }
            if (formula.charAt(index) == '\\' && formula.charAt(index + 1) == '/') { // Для дизъюнкции, занимает два символа
                return new Pair<>(true, 1);
            }
            if (formula.charAt(index) == '-' && formula.charAt(index + 1) == '>') { // Для импликации, занимает два символа
                return new Pair<>(true, 1);
            }
            if (formula.charAt(index) == '~' || formula.charAt(index) == '(' || formula.charAt(index) == '!') { // Для связок ии (, занимающих один символ
                return new Pair<>(true, 0);
            }
        }
        if (formula.charAt(index) == ')') // Символ, которые может быть в конце строки
            return new Pair<>(true, 0); // Занимает один символ
        return new Pair<>(false, 0); // Неккоректная связка
    }

    record Pair<T, U>(T first, U second) {
    }
}
