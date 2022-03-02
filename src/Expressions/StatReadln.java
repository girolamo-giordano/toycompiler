package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class StatReadln extends Stat{

    private Location sx;
    private Location dx;
    private ReadlnStat rln;

    public StatReadln(Location sx, Location dx, ReadlnStat rln) {
        super(sx, dx);
        this.rln = rln;
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

    public ReadlnStat getRln() {
        return rln;
    }

    public void setRln(ReadlnStat rln) {
        this.rln = rln;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
