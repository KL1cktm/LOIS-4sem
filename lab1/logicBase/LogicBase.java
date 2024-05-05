package logicBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;


public class LogicBase {
    private String logicFunction;
    private final List<Character> alphabet = List.of('0', '1', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
    private final List<String> operators = List.of("/\\", "\\/", "->", "!", "~", ")", "(");
    private List<Character> elements = new ArrayList<>();
    private HashMap<Character, Character> elementValue = new HashMap<>();
    private Stack<String> stack = new Stack<>();
    private List<Character> result = new ArrayList<>();

    public void checkImpracticabilityForm(String formula) { //проверка является ли формула невыполнимой
        this.logicFunction = formula;
        System.out.println(this.logicFunction);
        findUniqElements();
        createReversPolishWrite();
        reverseElementsInStack();
        createNewInterpretation();
        for (char token : this.result) {
            if (token == '1') {
                System.out.println("Формула является выполнимой");
                return;
            }
        }
        System.out.println("Формула является невыполнимой");

    }

    private void findUniqElements() {   //Находит все уникальные элементы
        for (char token : this.logicFunction.toCharArray()) {
            for (char literal : this.alphabet) {
                if (token == literal) {
                    if (token == '0' || token == '1') {
                        break;
                    }
                    this.elements.add(token);
                    break;
                }
            }
        }
        for (int i = 0; i < this.elements.size(); i++) {
            for (int j = i + 1; j < this.elements.size(); j++) {
                if (this.elements.get(i) == this.elements.get(j)) {
                    this.elements.remove(j--);
                }
            }
        }
    }

    private boolean checkOperator(String item) {  //проверка элемента стека на принадлежность оператором
        for (String operator : this.operators) {
            if (operator.equals(item)) {
                return true;
            }
        }
        return false;
    }

    private String conjunction(char item1, char item2) {  //Операция конъюнкции
        if (item1 == item2 && item1 == '1') {
            return "1";
        } else {
            return "0";
        }
    }

    private String disjunction(char item1, char item2) { //Операция дизъюнкции
        if (item1 == item2 && item1 == '0') {
            return "0";
        } else {
            return "1";
        }
    }

    private String implication(char item1, char item2) { //Операция импликации
        if (item2 == '0' || (item2 == '1' && item1 == '1')) {
            return "1";
        } else {
            return "0";
        }
    }

    private String equivalent(char item1, char item2) { //Операция эквиваленция
        if (item2 == item1) {
            return "1";
        } else {
            return "0";
        }
    }

    private String denial(char item) { //Операция отрицания
        if (item == '1') {
            return "0";
        } else {
            return "1";
        }
    }

    private char returnElementLogicValue(String item) { //проверка элемента на логическое значение
        Character element;
        if (!item.equals("0") && !item.equals("1")) {
            element = this.elementValue.get(item.charAt(0));
        } else {
            element = item.charAt(0);
        }
        return element;
    }

    private void chooseOperation(Stack<String> memoryOfIteration, String operator) {  //функция вызова операции
        switch (operator) {
            case "\\/":
                memoryOfIteration.push(disjunction(returnElementLogicValue(memoryOfIteration.pop()), returnElementLogicValue(memoryOfIteration.pop())));
                // вычисление операции "дизъюнкции" и запись результата в стек
                break;
            case "/\\":
                memoryOfIteration.push(conjunction(returnElementLogicValue(memoryOfIteration.pop()), returnElementLogicValue(memoryOfIteration.pop())));
                // вычисление операции "конъюнкции" и запись результата в стек
                break;
            case "->":
                memoryOfIteration.push(implication(returnElementLogicValue(memoryOfIteration.pop()), returnElementLogicValue(memoryOfIteration.pop())));
                // вычисление операции "импликации" и запись результата в стек
                break;
            case "~":
                memoryOfIteration.push(equivalent(returnElementLogicValue(memoryOfIteration.pop()), returnElementLogicValue(memoryOfIteration.pop())));
                // вычисление операции "эквиваленции" и запись результата в стек
                break;
            case "!":
                memoryOfIteration.push(denial(returnElementLogicValue(memoryOfIteration.pop())));
                // вычисление операции "отрицания" и запись результата в стек
                break;
        }
    }

    private void calculateInterpretation() {  //считывание элементов стека для начала подсчётов
        Stack<String> stack = new Stack<>();
        stack.addAll(this.stack);
        Stack<String> memoryOfIteration = new Stack<>();
        while (!stack.isEmpty()) {
            if (checkOperator(stack.peek())) {
                chooseOperation(memoryOfIteration, stack.pop());
            } else {
                memoryOfIteration.push(stack.pop());
            }
        }
        this.result.add(memoryOfIteration.pop().charAt(0));
    }

    private void createNewInterpretation() {  //создание всех интерпритаций
        int number = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < Math.pow(this.elements.size(), 2); i++) {
            String binaryNumber = Integer.toBinaryString(number++);
            for (int j = 0; j < this.elements.size() - binaryNumber.length(); j++) {
                stringBuilder.append('0');
            }
            stringBuilder.append(binaryNumber);
            setElementValue(stringBuilder.toString());
            //вычисления
            calculateInterpretation();
            stringBuilder.setLength(0);
        }
    }

    private void setElementValue(String interpretation) {  //установка бита соответствующему элементу
        for (int i = 0; i < this.elements.size(); i++) {
            this.elementValue.put(this.elements.get(i), interpretation.charAt(i));
        }
        this.elementValue.put('1', '1');
        this.elementValue.put('0', '0');
    }

    private void createReversPolishWrite() { //создание таблицы истинности
        Stack<String> operators = new Stack<>();
        for (int i = 0; i < this.logicFunction.length(); i++) {   //перебор посимвольно логической функции
            if (this.logicFunction.charAt(i) == ')') {  //проверка оператора закрытой скобки для записи ОПЗ
                addOperatorsOnMainStack(operators);
            } else {    //заполнение стека операндом
                for (char token : this.alphabet) {
                    if (token == this.logicFunction.charAt(i)) {
                        this.stack.push(String.valueOf(token));
                    }
                }
            }
            if (this.logicFunction.charAt(i) == '(') { //проверка строки на символ (, для записи в стек операторов
                operators.push("(");
            } else {       //запись в стек операторов
                for (String operator : this.operators) {
                    if (operator.charAt(0) == this.logicFunction.charAt(i)) {
                        if (operator.charAt(0) == '/') {     //проверка токена на конъюнкцию
                            operators.push("/\\");
                            i++;
                            break;
                        } else if (operator.charAt(0) == '\\') {  //проверка токена на дизъюнкцию
                            operators.push("\\/");
                            i++;
                            break;
                        } else if (operator.charAt(0) == '-') {  //проверка токена на импликацию
                            operators.push(("->"));
                            i++;
                            break;
                        } else {
                            if (!operator.equals(")")) {
                                operators.push(operator);
                            }
                            break;
                        }
                    }
                }
            }
        }

    }

    private void reverseElementsInStack() {  //инвертируем элементы стека
        Stack<String> reverseStack = new Stack<>();
        int size = this.stack.size();
        for (int i = 0; i < size; i++) {
            reverseStack.push(this.stack.pop());
        }
        this.stack = reverseStack;
    }

    private void addOperatorsOnMainStack(Stack<String> operators) { //записываем в стек операндов операторы для создания ОПЗ
        while (true) {
            if (operators.peek().equals("(")) {
                operators.pop();
                return;
            }
            this.stack.push(operators.pop());
        }
    }
}
