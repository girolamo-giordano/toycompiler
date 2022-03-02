package Expressions;
import Util.SymbolTable;
import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class Proc {
    private Location sx;
    private Location dx;
    private String id;
    private ParamDeclList p;
    private ResultTypeList r;
    private VarDeclList v;
    private StatList s;
    private ReturnExprs re;
    private SymbolTable st;

    public Proc(Location sx, Location dx, String id, ParamDeclList p, ResultTypeList r, VarDeclList v, StatList s, ReturnExprs re) {
        this.sx = sx;
        this.dx = dx;
        this.id = id;
        this.p = p;
        this.r = r;
        this.v = v;
        this.s = s;
        this.re = re;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ParamDeclList getP() {
        return p;
    }

    public void setP(ParamDeclList p) {
        this.p = p;
    }

    public ResultTypeList getR() {
        return r;
    }

    public void setR(ResultTypeList r) {
        this.r = r;
    }

    public VarDeclList getV() {
        return v;
    }

    public void setV(VarDeclList v) {
        this.v = v;
    }

    public StatList getS() {
        return s;
    }

    public void setS(StatList s) {
        this.s = s;
    }

    public ReturnExprs getRe() {
        return re;
    }

    public void setRe(ReturnExprs re) {
        this.re = re;
    }

    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }

    public SymbolTable getSt() {
        return st;
    }

    public void setSt(SymbolTable st) {
        this.st = st;
    }
}
