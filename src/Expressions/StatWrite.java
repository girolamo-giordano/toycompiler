package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class StatWrite extends Stat{

    private Location sx;
    private Location dx;
    private WriteStat ws;

    public StatWrite(Location sx, Location dx, WriteStat ws) {
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

    public WriteStat getWs() {
        return ws;
    }

    public void setWs(WriteStat ws) {
        this.ws = ws;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
