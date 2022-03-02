package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

import java.util.ArrayList;


public class ParamDeclList {

    private Location sx;
    private Location dx;
    private ArrayList<ParDecl> pl= new ArrayList<ParDecl>();

    public ParamDeclList(Location sx, Location dx, ParDecl pl) {
        this.sx = sx;
        this.dx = dx;
        this.pl.add(pl);
    }

    public void add(ParDecl pl)
    {
        this.pl.add(pl);
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

    public ArrayList<ParDecl> getPl() {
        return pl;
    }

    public void setPl(ArrayList<ParDecl> pl) {
        this.pl = pl;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
