package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory;

public class ExprFalse extends Expr {
    public ExprFalse(ComplexSymbolFactory.Location sx, ComplexSymbolFactory.Location dx)
    {
        super(sx,dx,false);
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
