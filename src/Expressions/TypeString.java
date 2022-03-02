package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;


public class TypeString extends Type {

    private String s;

    public TypeString(Location sx, Location dx, String s) {
        super(sx, dx, s,Typet.STRING);
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
