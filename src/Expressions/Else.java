package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class Else {

    private Location sx;
    private Location dx;
    private StatList sl;

    public Else(){

    }

    public Else(Location sx, Location dx, StatList sl) {
        this.sx = sx;
        this.dx = dx;
        this.sl = sl;
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

    public StatList getSl() {
        return sl;
    }

    public void setSl(StatList sl) {
        this.sl = sl;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
