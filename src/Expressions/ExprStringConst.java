package Expressions;
import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory;

public class ExprStringConst extends Expr {
    private String s;
    public ExprStringConst(ComplexSymbolFactory.Location sx, ComplexSymbolFactory.Location dx, String s){
        super (sx,dx,s);
        this.s=s;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
