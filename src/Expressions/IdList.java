package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

import java.util.ArrayList;

public class IdList {

    private Location sx;
    private Location dx;
    private ArrayList<String> elist= new ArrayList<String>(); //tiene traccia del nome degli id
    private ArrayList<String> idlist=new ArrayList<>(); //contiene eventuali doppioni

    public IdList(Location sx, Location dx, String e) {
        this.sx = sx;
        this.dx = dx;
        elist.add(e);
    }

    public void add(String e)
    {
        if(elist.contains(e))
        {
            idlist.add(e);
        }
        else {
            elist.add(e);
        }
    }

    public ArrayList<String> getIdlist() {
        return idlist;
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

    public ArrayList<String> getElist() {
        return elist;
    }

    public void setElist(ArrayList<String> elist) {
        this.elist = elist;
    }
}
