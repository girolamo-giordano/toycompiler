package Expressions;

import Visitor.*;
import java_cup.runtime.ComplexSymbolFactory.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AssignStat {

    private Location sx;
    private Location dx;
    private IdList idl;
    private ExprList e;
    private LinkedHashMap<String,Expr> idle= new LinkedHashMap<>();

    public AssignStat(Location sx, Location dx, IdList idl, ExprList e) {
        this.sx = sx;
        this.dx = dx;
        this.idl = idl;
        this.e = e;
    }

    public LinkedHashMap<String,Expr> getIdle()
    {
        Collections.reverse(e.getElist());
        for(int i=0;i<idl.getElist().size();i++)
        {
            if(i < e.getElist().size())
            {
                idle.put(idl.getElist().get(i),e.getElist().get(i));
            }
            else
            {
                idle.put(idl.getElist().get(i),e.getElist().get(e.getElist().size()-1));
            }
        }
        return idle;
    }

    public LinkedHashMap<String,Expr> getListaidex(){
        return idle;
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

    public IdList getIdl() {
        return idl;
    }

    public void setIdl(IdList idl) {
        this.idl = idl;
    }

    public ExprList getE() {
        return e;
    }

    public void setE(ExprList e) {
        this.e = e;
    }

    public <T> T accept(VisitorInterface<T> visitor) {
        return visitor.visit(this);
    }
}
