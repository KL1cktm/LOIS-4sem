package logicBase;

public class main {
    public static void main(String[] args)
    {
        LogicBase logicBase = new LogicBase();
//        logicBase.checkImpracticabilityForm("(A->(B/\\C))");
        System.out.println("(((A->B)/\\(C\\/D))");
        logicBase.checkImpracticabilityForm("(((A->B)/\\(C\\/D))");
    }
}
