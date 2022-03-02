package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class WriteStat {
    private Location sx;
    private Location dx;
    private ExprList el;

    public WriteStat(Location sx, Location dx, ExprList el) {
        this.sx = sx;
        this.dx = dx;
        this.el = el;
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

    public ExprList getEl() {
        return el;
    }

    public void setEl(ExprList el) {
        this.el = el;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
