package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;


public class ReadlnStat {

    private Location sx;
    private Location dx;
    private IdList idlist;

    public ReadlnStat(Location sx, Location dx, IdList idlist) {
        this.sx = sx;
        this.dx = dx;
        this.idlist = idlist;
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

    public IdList getIdlist() {
        return idlist;
    }

    public void setIdlist(IdList idlist) {
        this.idlist = idlist;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
