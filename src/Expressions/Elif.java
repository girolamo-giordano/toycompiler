package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;


public class Elif {

    private Location sx;
    private Location dx;
    private Expr e;
    private StatList s;

    public Elif(Location sx, Location dx, Expr e, StatList s) {
        this.sx = sx;
        this.dx = dx;
        this.e = e;
        this.s = s;
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

    public Expr getE() {
        return e;
    }

    public void setE(Expr e) {
        this.e = e;
    }

    public StatList getS() {
        return s;
    }

    public void setS(StatList s) {
        this.s = s;
    }


    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
