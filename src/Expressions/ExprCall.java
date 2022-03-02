package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class ExprCall extends Expr {

    private CallProc c;

    public ExprCall(Location sx, Location dx, CallProc c) {
        super(sx, dx,c);
        this.c = c;
    }

    public CallProc getC() {
        return c;
    }

    public void setC(CallProc c) {
        this.c = c;
    }

    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
