package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;


public class ResultType {

    private Location sx;
    private Location dx;
    private Type t;

    public ResultType(Location sx, Location dx, Type t) {
        this.sx = sx;
        this.dx = dx;
        this.t = t;
    }

    public ResultType(Location sx,Location dx)
    {
            this.t= new Type(sx,dx);
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

    public Type getT() {
        return t;
    }

    public void setT(Type t) {
        this.t = t;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
