package Expressions;

import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;


public class StatIf extends Stat {

    private IfStat s;

    public StatIf(Location sx, Location dx,IfStat s) {
        super(sx, dx);
        this.s = s;
    }

    public IfStat getS() {
        return s;
    }

    public void setS(IfStat s) {
        this.s = s;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
