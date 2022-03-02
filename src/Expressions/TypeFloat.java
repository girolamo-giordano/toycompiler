package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;


public class TypeFloat extends Type{

    private String s;

    public TypeFloat(Location sx, Location dx, String s) {
        super(sx, dx, s,Typet.FLOAT);
        this.s = s;
    }

    @Override
    public String getS() {
        return s;
    }

    @Override
    public void setS(String s) {
        this.s = s;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
