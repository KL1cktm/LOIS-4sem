package logicBase;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;


public class LogicBase {
    private String logicFunction;
    private final List<Character> alphabet = List.of('0', '1', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
    private final List<String> operators = Arrays.asList("/\\", "\\/", "->", "!", "~", ")", "(");
    private Stack<String> stack = new Stack<>();

    public void checkImpracticabilityForm(String formula) { //проверка является ли формула невыполнимой
        this.logicFunction = formula;
        createReversPolishWrite();
        reverseElementsInStack();
        System.out.println(stack);
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
                        }
                    }
                }
            }
        }

    }
    private void reverseElementsInStack() {
        Stack<String> reverseStack = new Stack<>();
        int size = this.stack.size();
        for (int i=0;i<size;i++) {
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
