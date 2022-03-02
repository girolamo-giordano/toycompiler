package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory;

public class ExprId extends Expr {
    private String id;
    public ExprId(ComplexSymbolFactory.Location sx, ComplexSymbolFactory.Location dx, String s){
        super(sx,dx,s);
        this.id=s;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
