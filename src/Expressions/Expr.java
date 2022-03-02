package Expressions;

import Visitor.*;
import java_cup.runtime.ComplexSymbolFactory.Location;


public abstract class Expr {
    private Location dx;
    private Location sx;
    private Object object;
    private Object object2;
    private String segno;

    public Expr(Location sx, Location dx, Object ob)
    {
        this.dx=dx;
        this.sx=sx;
        object=ob;
    }
    public Expr(Location sx, Location dx, Object ob,Object ob2,String segno)
    {
        this.dx=dx;
        this.sx=sx;
        object=ob;
        object2=ob2;
        this.segno=segno;
    }

    public Expr(Location sx, Location dx)
    {
        this.dx=dx;
        this.sx=sx;
        object=null;
    }

    public Expr(Object o)
    {

        object=o;

    }

    public Expr()
    {
        this.dx=null;
        this.sx=null;
        object=null;
    }

    public Object getObject2() {
        return object2;
    }

    public void setObject2(Object object2) {
        this.object2 = object2;
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

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public abstract <T> T accept(VisitorInterface<T> visitor);

    @Override
    public String toString() {
        return "Expr{" +
                "dx=" + dx +
                ", sx=" + sx +
                ", object=" + object +
                ", object2=" + object2 +
                '}';
    }
}
