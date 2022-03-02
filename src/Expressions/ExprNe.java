package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class ExprNe extends Expr {

    private Expr eleft,eright;

    public ExprNe(Location sx, Location dx, Expr eleft, Expr eright) {
        super(sx, dx,eleft,eright,"!=");
        this.eleft = eleft;
        this.eright = eright;
    }

    public Expr getEleft() {
        return eleft;
    }

    public void setEleft(Expr eleft) {
        this.eleft = eleft;
    }

    public Expr getEright() {
        return eright;
    }

    public void setEright(Expr eright) {
        this.eright = eright;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}

