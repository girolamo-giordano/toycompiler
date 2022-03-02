package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

import java.util.ArrayList;

public class ResultTypeList
{
    private ArrayList<ResultType> r= new ArrayList<ResultType>();
    private Location dx;
    private Location sx;

    public ResultTypeList( Location sx, Location dx, ResultType r) {
        this.r.add(r);
        this.dx = dx;
        this.sx = sx;
    }

    public void add(ResultType r){
        this.r.add(r);
    }

    public ArrayList<ResultType> getR() {
        return r;
    }

    public void setR(ArrayList<ResultType> r) {
        this.r = r;
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

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
