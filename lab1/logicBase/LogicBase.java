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
    private HashMap<Character,Character> elementValue = new HashMap<>();
    private Stack<String> stack = new Stack<>();
    private List<Character> result = new ArrayList<>();

    public void checkImpracticabilityForm(String formula) { //проверка является ли формула невыполнимой
        this.logicFunction = formula;
        findUniqElements();
        createReversPolishWrite();
        createNewInterpretation();
        reverseElementsInStack();
        System.out.println(stack);
    }
    private void findUniqElements() {   //Находит все уникальные элементы
        for (char token: this.logicFunction.toCharArray()) {
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
        for (int i=0;i<this.elements.size();i++) {
            for (int j=i+1;j<this.elements.size();j++) {
                if (this.elements.get(i) == this.elements.get(j)) {
                    this.elements.remove(j--);
                }
            }
        }
    }
    private void createNewInterpretation() {  //создание всех интерпритаций
        int number = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<Math.pow(this.elements.size(),2);i++) {
            String binaryNumber = Integer.toBinaryString(number++);
            for (int j=0;j<this.elements.size() - binaryNumber.length();j++) {
                stringBuilder.append('0');
            }
            stringBuilder.append(binaryNumber);
            setElementValue(stringBuilder.toString());
            System.out.println(this.elementValue);
            //вычисления
            stringBuilder.setLength(0);
        }
    }
    private void setElementValue(String interpretation) {  //установка бита соответствующему элементу
        for (int i=0;i<this.elements.size();i++) {
            this.elementValue.put(this.elements.get(i),interpretation.charAt(i));
        }
        this.elementValue.put('1','1');
        this.elementValue.put('0','0');
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
    private void reverseElementsInStack() {  //инвертируем элементы стека
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
