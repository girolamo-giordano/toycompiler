package Expressions;
import Visitor.*;

import java_cup.runtime.ComplexSymbolFactory;

public class ExprFloatConst extends Expr {
    private Float i=0.0f;
    public ExprFloatConst(ComplexSymbolFactory.Location sx, ComplexSymbolFactory.Location dx, Float num){
        super (sx,dx,num);
        this.i=num;
    }
    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }

    public Float getI() {
        return i;
    }

    public void setI(float i) {
        this.i = i;
    }
}
