package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;


public class StatWhile extends Stat {

    private Location sx;
    private Location dx;
    private WhileStat ws;

    public StatWhile(Location sx, Location dx, WhileStat ws) {
        super(sx, dx);
        this.ws = ws;
    }

    @Override
    public Location getSx() {
        return sx;
    }

    @Override
    public void setSx(Location sx) {
        this.sx = sx;
    }

    @Override
    public Location getDx() {
        return dx;
    }

    @Override
    public void setDx(Location dx) {
        this.dx = dx;
    }

    public WhileStat getWs() {
        return ws;
    }

    public void setWs(WhileStat ws) {
        this.ws = ws;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
