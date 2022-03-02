package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class ExprTrue extends Expr{

    public ExprTrue(Location sx,Location dx)
    {
        super(sx,dx,true);
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
