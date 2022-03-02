package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class WhileStat {

    private Location sx;
    private Location dx;
    private StatList sl,sl2;
    private Expr e;

    public WhileStat(Location sx, Location dx, StatList sl, Expr e,StatList sl2) {
        this.sx = sx;
        this.dx = dx;
        this.sl = sl;
        this.sl2 = sl2;
        this.e = e;
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

    public StatList getSl2() {
        return sl2;
    }

    public void setSl2(StatList sl2) {
        this.sl2 = sl2;
    }

    public Expr getE() {
        return e;
    }

    public void setE(Expr e) {
        this.e = e;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
