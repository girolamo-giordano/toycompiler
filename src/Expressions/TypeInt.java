package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class TypeInt extends Type {

    private String s;

    public TypeInt(Location sx, Location dx,String s) {
        super(sx, dx,s,Typet.INT);
        this.s=s;
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
