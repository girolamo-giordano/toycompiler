package Expressions;
import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class IdListInit {
    private Location dx;
    private Location sx;
    private String id;
    private LinkedHashMap<String,Expr> idl = new LinkedHashMap<>(); //associazione id ad un expr
    private Expr e;
    private ArrayList<String> idlist=new ArrayList<>();


    public IdListInit(Location sx, Location dx, String id) {
        this.dx = dx;
        this.sx = sx;
        this.idl.put(id,null);
        this.id=id;

    }

    public IdListInit(Location sx, Location dx, String id,Expr e) {
        this.dx = dx;
        this.sx = sx;
        this.idl.put(id,e);
        this.id=id;
        this.e=e;
    }

    public void add(String i,Expr e) {

            if (idl.containsKey(i)) {
                idlist.add(i);

            } else {
                this.idl.put(i, e);
            }

    }

    public void insert(String id,Expr e)
    {
        /*idl.forEach((key, tab) -> {
                    idl.put(key,e);
                }
        );*/
        idl.put(id,e);
        this.e=e;
    }

    public ArrayList<String> getIdlist() {
        return idlist;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Expr getE() {
        return e;
    }

    public void setE(Expr e) {
        this.e = e;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }

    public LinkedHashMap<String, Expr> getIdl() {
        return idl;
    }

    public void setIdl(LinkedHashMap<String, Expr> idl) {
        this.idl = idl;
    }
}