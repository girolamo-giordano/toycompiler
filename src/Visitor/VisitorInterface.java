package Visitor;


import Expressions.*;

public interface VisitorInterface <T>{

    //Expressions
    T visit(AssignStat ex);
    T visit(CallProc ex);
    T visit(Elif ex);
    T visit(ElifList ex);
    T visit(Else ex);
    T visit(Expr ex);
    T visit(ExprAnd ex);
    T visit(ExprCall ex);
    T visit(ExprDiv ex);
    T visit(ExprEq ex);
    T visit(ExprFalse ex);
    T visit(ExprFloatConst ex);
    T visit(ExprGe ex);
    T visit(ExprGt ex);
    T visit(ExprId ex);
    T visit(ExprIntConst ex);
    T visit(ExprLe ex);
    T visit(ExprList ex);
    T visit(ExprLt ex);
    T visit(ExprMinus ex);
    T visit(ExprMinusUn ex);
    T visit(ExprNe ex);
    T visit(ExprNot ex);
    T visit(ExprNull ex);
    T visit(ExprOr ex);
    T visit(ExprPlus ex);
    T visit(ExprStringConst ex);
    T visit(ExprTimes ex);
    T visit(ExprTrue ex);
    T visit(IdList ex);
    T visit(IdListInit ex);
    T visit(IfStat ex);
    T visit(ParamDeclList ex);
    T visit(ParDecl ex);
    T visit(Proc ex);
    T visit(ProcList ex);
    T visit(Program ex);
    T visit(ReadlnStat ex);
    T visit(ResultType ex);
    T visit(ResultTypeList ex);
    T visit(ReturnExprs ex);
    T visit(Stat ex);
    T visit(StatAssign ex);
    T visit(StatCallProc ex);
    T visit(StatIf ex);
    T visit(StatList ex);
    T visit(StatReadln ex);
    T visit(StatWhile ex);
    T visit(StatWrite ex);
    T visit(Type ex);
    T visit(TypeBool ex);
    T visit(TypeFloat ex);
    T visit(TypeInt ex);
    T visit(TypeString ex);
    T visit(VarDecl ex);
    T visit(VarDeclList ex);
    T visit(WhileStat ex);
    T visit(WriteStat ex);




}
