package Expressions;

import Visitor.*;
import java_cup.runtime.ComplexSymbolFactory.Location;

import java.util.ArrayList;

public class VarDeclList {
    private Location sx;
    private Location dx;
    private ArrayList<VarDecl> v= new ArrayList <VarDecl>();

    public VarDeclList() {

    }

    public void add(VarDecl v){
        this.v.add(v);
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

    public ArrayList<VarDecl> getV() {
        return v;
    }



}
