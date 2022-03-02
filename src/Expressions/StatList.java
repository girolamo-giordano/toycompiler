package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

import java.util.ArrayList;


public class StatList {

    private Location sx;
    private Location dx;
    private ArrayList<Stat> s=new ArrayList<Stat>();

    public StatList(Location sx, Location dx, Stat s) {
        this.sx = sx;
        this.dx = dx;
        this.s.add(s);
    }

    public void add(Stat s)
    {
        this.s.add(s);
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

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }

    public ArrayList<Stat> getS() {
        return s;
    }

    public void setS(ArrayList<Stat> s) {
        this.s = s;
    }
}
