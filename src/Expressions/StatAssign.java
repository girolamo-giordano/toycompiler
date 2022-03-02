package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;


public class StatAssign extends Stat{

    private Location sx;
    private Location dx;
    private AssignStat as;

    public StatAssign(Location sx, Location dx, AssignStat as) {
        super(sx, dx);
        this.as = as;
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

    public AssignStat getAs() {
        return as;
    }

    public void setAs(AssignStat as) {
        this.as = as;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
