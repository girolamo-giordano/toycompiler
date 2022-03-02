package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;


public class Stat {
    Location sx;
    Location dx;
    Object o;

    public Stat(Location sx, Location dx, Object o) {
        this.sx = sx;
        this.dx = dx;
        this.o = o;
    }

    public Stat(Location sx, Location dx) {
        this.sx = sx;
        this.dx = dx;
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

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
