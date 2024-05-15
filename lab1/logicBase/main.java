package logicBase;

public class main {
    public static void main(String[] args)
    {
        LogicBase logicBase = new LogicBase();
//        Verification verification = new Verification();
//        verification.verify("(A/\\(!A))");
//        logicBase.checkImpracticabilityForm("(A->(B/\\C))");
//        System.out.println("(((A->B)/\\(C\\/D))");
//        logicBase.checkImpracticabilityForm("((A->B)/\\(C\\/D))");
//        System.out.println("(((!P)->(Q/\\R))~((!(!(P\\/Q)))->S))");
//        logicBase.checkImpracticabilityForm("(((!P)->(Q/\\R))~((!(!(P\\/Q)))->S))");
        //Result:  0000100101010101
        //WebSite: 0000100101010101
        logicBase.checkImpracticabilityForm("(A/\\(!A))");
    }
}
