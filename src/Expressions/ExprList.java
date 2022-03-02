package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

import java.util.ArrayList;

public class ExprList {

    private ArrayList<Expr> elist= new ArrayList<Expr>();
    private Location sx;
    private Location dx;

    public ExprList(Location sx,Location dx,Expr e)
    {
        this.sx=sx;
        this.dx=dx;
        elist.add(e);
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
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

    public void add(Expr e)
    {
        elist.add(e);
    }

    public ArrayList<Expr> getElist() {
        return elist;
    }

    public void setElist(ArrayList<Expr> elist) {
        this.elist = elist;
    }
}
