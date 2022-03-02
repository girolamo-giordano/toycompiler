package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class Type {

    private Location sx;
    private Location dx;
    private Object s;
    private Typet t;
    public enum Typet{
        INT,
        BOOL,
        FLOAT,
        STRING,
        NULL,
        VOID;
    }

    public Type(Location sx, Location dx, Object s,Typet t) {
        this.sx = sx;
        this.dx = dx;
        this.s = s;
        this.t=t;
    }

    public Type(Location sx, Location dx){
        this.sx=sx;
        this.dx=dx;
        this.t=Typet.VOID;
    }

    public Typet getType()
    {
        return t;
    }

    public Type(Object s)
    {
        this.s=s;
    }

    public Location getSx() {
        return sx;
    }

    public void setSx(Location sx) {
        this.sx = sx;
    }

    public Location getDx() {
        return dx;
    }

    public void setDx(Location dx) {
        this.dx = dx;
    }

    public Object getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
