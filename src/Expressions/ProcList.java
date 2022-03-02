package Expressions;
import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

import java.util.ArrayList;

public class ProcList {
    private Location dx;
    private Location sx;
    private ArrayList<Proc> p= new ArrayList<Proc>();

    public ProcList(Location sx, Location dx, Proc p) {
        this.dx = dx;
        this.sx = sx;
        this.p.add(p);
    }

    public void add(Proc p){
        this.p.add(p);
    }

    public Location getDx() {
        return dx;
    }

    public void setDx(Location dx) {
        this.dx = dx;
    }

    public Location getSx() {
        return sx;
    }

    public void setSx(Location sx) {
        this.sx = sx;
    }

    public ArrayList<Proc> getP() {
        return p;
    }

    public void setP(ArrayList<Proc> p) {
        this.p = p;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
