package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;


public class ExprNull extends Expr{

    private String nul="NULL";

    public ExprNull(Location dx, Location sx) {

        super(dx, sx,"NULL");
    }

    public String getValue(){
        return nul;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
