package Expressions;
import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class VarDecl {
    private Location dx;
    private Location sx;
    private Type t;
    private IdListInit idl;

    public VarDecl(Location sx, Location dx, Type t, IdListInit idl) {
        this.dx = dx;
        this.sx = sx;
        this.t = t;
        this.idl = idl;
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

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }

    public void setSx(Location sx) {
        this.sx = sx;
    }

    public Type getT() {
        return t;
    }

    public void setT(Type t) {
        this.t = t;
    }

    public IdListInit getIdl() {
        return idl;
    }

    public void setIdl(IdListInit idl) {
        this.idl = idl;
    }
}
