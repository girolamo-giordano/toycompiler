package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;


public class IfStat {

    private Location sx;
    private Location dx;
    private Expr e;
    private StatList sl;
    private ElifList el;
    private Else els;

    public IfStat(Location sx, Location dx, Expr e, StatList sl, ElifList el, Else els) {
        this.sx = sx;
        this.dx = dx;
        this.e = e;
        this.sl = sl;
        this.el = el;
        this.els = els;
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

    public StatList getSl() {
        return sl;
    }

    public void setSl(StatList sl) {
        this.sl = sl;
    }

    public ElifList getEl() {
        return el;
    }

    public void setEl(ElifList el) {
        this.el = el;
    }

    public Else getEls() {
        return els;
    }

    public void setEls(Else els) {
        this.els = els;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
