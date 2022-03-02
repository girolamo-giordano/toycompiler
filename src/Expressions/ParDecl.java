package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;


public class ParDecl {

    private Location sx;
    private Location dx;
    private Type t;
    private IdList il;

    public ParDecl(Location sx, Location dx, Type t, IdList il) {
        this.sx = sx;
        this.dx = dx;
        this.t = t;
        this.il = il;
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

    public IdList getIl() {
        return il;
    }

    public void setIl(IdList il) {
        this.il = il;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
