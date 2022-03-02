package Expressions;

import Util.SymbolTable;
import Visitor.*;
import java_cup.runtime.ComplexSymbolFactory.Location;

public class Program {
    private Location sx;
    private Location dx;
    private VarDeclList v;
    private ProcList p;
    private SymbolTable globaltable;

    public Program(Location sx, Location dx, VarDeclList v, ProcList p) {
        this.sx = sx;
        this.dx = dx;
        this.v = v;
        this.p = p;
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

    public VarDeclList getV() {
        return v;
    }

    public void setV(VarDeclList v) {
        this.v = v;
    }

    public ProcList getP() {
        return p;
    }

    public void setP(ProcList p) {
        this.p = p;
    }

    public SymbolTable getGlobaltable() {
        return globaltable;
    }

    public void setGlobaltable(SymbolTable globaltable) {
        this.globaltable = globaltable;
    }

    public <T> T accept(VisitorInterface<T> visitor) {
        return visitor.visit(this);
    }


}
