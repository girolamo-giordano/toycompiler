package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class ExprNot extends Expr {

    private Expr enode;
    private String not;

    public ExprNot(Location sx, Location dx, Expr enode) {
        super(sx, dx);
        this.enode = enode;
        not="NOT";
    }

    public Expr getEnode() {
        return enode;
    }

    public void setEnode(Expr enode) {
        this.enode = enode;
    }

    public String getNot() {
        return not;
    }

    public void setNot(String not) {
        this.not = not;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
