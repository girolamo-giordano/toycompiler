package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

import java.util.ArrayList;


public class ElifList {

    private Location sx;
    private Location dx;
    private ArrayList<Elif> elif=new ArrayList<Elif>();

    public ElifList() {

    }

    public ElifList(Location sx, Location dx, Elif el) {
        this.sx = sx;
        this.dx = dx;
        this.elif.add(el);
    }

    public void add(Elif el)
    {
        elif.add(el);
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

    public ArrayList<Elif> getElif() {
        return elif;
    }

    public void setElif(ArrayList<Elif> elif) {
        this.elif = elif;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
