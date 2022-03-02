package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;


public class StatCallProc extends Stat {

    private Location sx;
    private Location dx;
    private CallProc cp;

    public StatCallProc(Location sx, Location dx, CallProc cp) {
        super(sx, dx);
        this.cp = cp;
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

    public CallProc getCp() {
        return cp;
    }

    public void setCp(CallProc cp) {
        this.cp = cp;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
