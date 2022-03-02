package Util;

import Expressions.*;

import java.util.ArrayList;

public class Record { // va all'interno della symbol table dove all'interno ci può essere
    private String id;
    public enum Type{ // sia una variabile o un procedura o un operazione binaria
        VAR,
        PROC,
        BIN;
    }
    private Type t; //si riferisce all'enum
    private Expressions.Type.Typet type; //class Type.java
    private ArrayList<Expressions.Type.Typet>paramtype=new ArrayList<>(); //salvare il tipo
    private ArrayList<Expressions.Type.Typet>returntype=new ArrayList<>(); //salvare il tipo di ritorno di una procedura
    private ArrayList<String>vardeclrip= new ArrayList<>(); //tenere traccia se si ripete due volte la stessa variabile, controllo interno
    private int hashcode; //utilizzato per i record con le operazioni binarie


    public Record(int hashcode, Expressions.Type.Typet type)
    {
        this.hashcode=hashcode;
        t=Type.BIN;
        this.type=type;
    }

    public Record(VarDecl vd) //record può contenere una dichiarazione di variabil
    {
        this.id=vd.getIdl().getId();
        this.type=vd.getT().getType();
        this.t=Type.VAR;
        this.vardeclrip=vd.getIdl().getIdlist(); //si prende gli id degli idlist per non ripetere due id uguali
    }

    public Record(VarDecl vd,String s) //record può contenere una dichiarazione di variabili
    {
        this.id=s;
        this.type=vd.getT().getType();
        this.t=Type.VAR;
        this.vardeclrip=vd.getIdl().getIdlist(); //si prende gli id degli idlist per non ripetere due id uguali
    }

    public Record(ParDecl p,String s) // perche prendendo la s che è nome del metodo e la p con il quale prendo il tipo creiamo un record che sia un pardecl
    {
        this.id=s;
        this.type=p.getT().getType();
        paramtype.add(this.type);
        this.t=Type.VAR;
    }


    public Record(Proc op) //record può contenere un proc
    {
        this.id=op.getId();
        t=Type.PROC;
        if(op.getP()!=null) { //controlla se nella proc c'è qualcosa se non c'è niente vedo solo il tipo di ritorno
            for (ParDecl p : op.getP().getPl()) { //per ogni parametro all'interno della procedua controllo il tipo
                if (p.getT() == null) { // se è uguale a null lo setto come void
                    paramtype.add(Expressions.Type.Typet.VOID);
                } else {
                    for(int i=0;i<p.getIl().getElist().size();i++)
                        paramtype.add(p.getT().getType()); //altrimenti se non ha null significa che ha un proprio tipo
                }
            }
        }
        for (ResultType rt : op.getR().getR()) {
            if (rt.getT() == null) {
                returntype.add(Expressions.Type.Typet.VOID);
            } else {

                returntype.add(rt.getT().getType());
            }
        }
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getVardeclrip() {
        return vardeclrip;
    }

    public void setVardeclrip(ArrayList<String> vardeclrip) {
        this.vardeclrip = vardeclrip;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getHash()
    {
        return hashcode;
    }

    public Type getT() {
        return t;
    }

    public void setT(Type t) {
        this.t = t;
    }

    public Expressions.Type.Typet getType() {
        return type;
    }

    public void setType(Expressions.Type.Typet type) {
        this.type = type;
    }

    public ArrayList<Expressions.Type.Typet> getParamtype() {
        return paramtype;
    }

    public void setParamtype(ArrayList<Expressions.Type.Typet> paramtype) {
        this.paramtype = paramtype;
    }

    public ArrayList<Expressions.Type.Typet> getReturntype() {
        return returntype;
    }

    public void setReturntype(ArrayList<Expressions.Type.Typet> returntype) {
        this.returntype = returntype;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id='" + id + '\'' +
                ", t=" + t +
                ", type=" + type +
                ", paramtype=" + paramtype +
                ", returntype=" + returntype +
                ", vardeclrip=" + vardeclrip +
                '}';
    }
}
