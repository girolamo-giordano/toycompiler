package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class ExprMinusUn extends Expr {

    private Expr eleft;
    private String unop;

    public ExprMinusUn(Location sx, Location dx, Expr eleft) {
        super(sx, dx);
        this.eleft = eleft;
        unop="-";
    }

    public Expr getEleft() {
        return eleft;
    }

    public void setEleft(Expr eleft) {
        this.eleft = eleft;
    }

    public String getUnop() {
        return unop;
    }

    public void setUnop(String unop) {
        this.unop = unop;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
