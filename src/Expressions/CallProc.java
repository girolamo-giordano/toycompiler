package Expressions;

import Visitor.*;
import java_cup.runtime.ComplexSymbolFactory.Location;

public class CallProc extends Expr{
    private String id;
    private ExprList list;

    public CallProc(Location sx, Location dx, String id, ExprList list) {
        super(sx, dx);
        this.id = id;
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ExprList getList() {
        return list;
    }

    public void setList(ExprList list) {
        this.list = list;
    }


    @Override
    public <T> T accept(VisitorInterface<T> v) {
        return v.visit(this);
    }
}
