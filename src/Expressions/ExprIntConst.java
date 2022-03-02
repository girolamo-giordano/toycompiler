package Expressions;
import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class ExprIntConst extends Expr {
    private Integer i=0;
    public ExprIntConst(Location sx, Location dx, Integer num){
        super (sx,dx,num);
        this.i=num;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }
}
