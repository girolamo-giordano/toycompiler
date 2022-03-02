package Visitor;

import Expressions.*;
import Util.ErrorParser;
import Util.Record;
import Util.SymbolTable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class TypeVisitor implements VisitorInterface<Object> {

    private Stack<SymbolTable>stack= new Stack<>();
    private SymbolTable global;

    @Override
    public Object visit(AssignStat ex) {
        Record d;
        boolean inside=false;
        ArrayList<Type.Typet> tipi;
        ArrayList<Type.Typet>tipi2=(ArrayList< Type.Typet>)ex.getIdl().accept(this);//mi prendo tutti i tipi degli idlist
        int countidlist=0;
        for(int i=0;i<ex.getE().getElist().size();i++)
        {
            Expr e=ex.getE().getElist().get(i);
            if(ex.getE().getElist().get(i).getObject() instanceof CallProc)
            {
                CallProc c= (CallProc) e.getObject();
                c.accept(this);
                d=lookUp(c.getId());
                tipi=d.getReturntype();
                for(int k=0;k<tipi.size();k++)
                {
                    //controllo se la chiamata a procedura restituisce i giusti tipi alla lista di id
                    if(!(tipi.get(k).equals(tipi2.get(countidlist)))){
                        throw new ErrorParser("Errore alla linea("+ex.getDx().getLine()+"): Sono stati passati tipi diversi");
                    }
                    inside=true;
                    countidlist++;
                }
            }
            if(inside)
                countidlist--;
            else {
                //controllo se l'espressione che stiamo assegnando ad un id abbia un tipo
                if(ex.getE().getElist().get(i).accept(this)== null)
                {
                    throw new ErrorParser("Errore alla linea("+ex.getDx().getLine()+"): Errore di tipi");
                }
                //controllo se l'espressione che stiamo passando abbia lo stesso tipo dell'id o nel caso del float l'expr può essere anche un int
                if(!((ex.getE().getElist().get(i).accept(this).equals(tipi2.get(countidlist))) || ex.getE().getElist().get(i).accept(this).equals(Type.Typet.INT) && tipi2.get(countidlist).equals(Type.Typet.FLOAT)))
                {
                    throw new ErrorParser("Errore alla linea("+ex.getDx().getLine()+"): Errore di tipi");
                }

            }
            countidlist++;
        }
        //se gli expr sono < degli id controllo che l'ultimo expr abbia il tipo adatto per i rimanenti id
        while(tipi2.size()!=countidlist)
        {
            Expr e=ex.getE().getElist().get(ex.getE().getElist().size()-1);
            if(e.getObject() instanceof CallProc)
            {
                d=lookUp(((CallProc) e).getId());
                tipi=d.getReturntype();
                if(tipi2.get(countidlist).equals(tipi.get(tipi.size()-1)))
                {
                    countidlist++;
                }
            }
            else
            {
                if(tipi2.get(countidlist).equals(e.accept(this))) {
                    countidlist++;
                }
                else {
                    countidlist++;
                    throw new ErrorParser("Errore alla linea("+ex.getDx().getLine()+"): Errore di tipi");
                }
            }
        }
        return null;
    }

    @Override
    public Object visit(CallProc ex) {
        ArrayList<Type.Typet>tipi=new ArrayList<>();
        Record j;
        j=lookUp(ex.getId());
        if(ex.getList()!=null){
            for(Expr e:ex.getList().getElist())
            {
                //se il callproc contiene un'altra chiamata a procedura mi prendo i tipi di ritorno di questa chiamata a procedura
                if(e.getObject() instanceof CallProc)
                {
                    ((CallProc) e.getObject()).accept(this);
                    CallProc cs= (CallProc) e.getObject();
                    Record el=lookUp(cs.getId());
                    for(Type.Typet ti:el.getReturntype())
                    {
                        tipi.add(ti);
                    }
                }
                //altrimenti aggiungo direttamente i tipi dei parametri passati alla procedura nella lista dei tipi
                else
                {
                    tipi.add((Type.Typet)e.accept(this));
                }
            }
            for(int i=0;i<tipi.size();i++)
            {
                    //controllo se i tipi passati tramite parametri sono quelli che necessita la chiamata a procedura
                    if(j.getParamtype().get(i).equals(tipi.get(i)));
                    else if(tipi.get(i)!= null && j.getParamtype().get(i).equals(Type.Typet.FLOAT) && tipi.get(i).equals(Type.Typet.INT));
                    else
                    {
                        throw new ErrorParser("Errore alla linea("+ex.getDx().getLine()+"): Errore passaggio di parametri nella chiamata a funzione:"+ex.getId()+"parametri richiesti:"+j.getParamtype());

                    }
            }

        }
        //qui passo il tipo di ritorno della chiamata a procedura
        if(j.getReturntype().size()==1)
        {
            return j.getReturntype().get(0);
        }
        return null;
    }

    @Override
    public Object visit(Elif ex) {
            //controllo se all'elif viene passato un espressione che restituisce un booleano
            if (ex.getE().accept(this).equals(Type.Typet.BOOL)) ;
            else {
                throw new ErrorParser("Errore alla linea("+ex.getDx().getLine()+"):Errore ritorno condizione else if, la variabile non è un valore booleano");
            }
        ex.getS().accept(this);
        return null;
    }

    @Override
    public Object visit(ElifList ex) {
        for(Elif x:ex.getElif())
        {
            x.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Else ex) {
        if(ex.getSl()!=null)
            ex.getSl().accept(this);
        return null;
    }

    @Override
    public Object visit(Expr ex) {
        return null;
    }

    @Override
    public Object visit(ExprAnd ex) {
        Record d= new Record(ex.hashCode(),optype2("and",ex.getEleft().accept(this),ex.getEright().accept(this)));
        stack.peek().addRecord(d);
        return optype2("and",ex.getEleft().accept(this),ex.getEright().accept(this));
    }

    @Override
    public Object visit(ExprCall ex) {
        return ex.getC().accept(this);
    }

    @Override
    public Object visit(ExprDiv ex) {
        Record d= new Record(ex.hashCode(),optype2("/",ex.getEleft().accept(this),ex.getEright().accept(this)));
        stack.peek().addRecord(d);
        return optype2("/",ex.getEleft().accept(this),ex.getEright().accept(this));
    }

    @Override
    public Object visit(ExprEq ex) {
        Record d= new Record(ex.hashCode(),optype2("<==>",ex.getEleft().accept(this),ex.getEright().accept(this)));
        stack.peek().addRecord(d);
        return optype2("<==>",ex.getEleft().accept(this),ex.getEright().accept(this));
    }

    @Override
    public Object visit(ExprFalse ex) {
        Record d= new Record(ex.hashCode(), Type.Typet.BOOL);
        stack.peek().addRecord(d);
        return Type.Typet.BOOL;
    }

    @Override
    public Object visit(ExprFloatConst ex) {
        Record d= new Record(ex.hashCode(), Type.Typet.FLOAT);
        stack.peek().addRecord(d);
        return Type.Typet.FLOAT;
    }

    @Override
    public Object visit(ExprGe ex) {
        Record d= new Record(ex.hashCode(),optype2("<==>",ex.getEleft().accept(this),ex.getEright().accept(this)));
        stack.peek().addRecord(d);
        return optype2("<==>",ex.getEleft().accept(this),ex.getEright().accept(this));
    }

    @Override
    public Object visit(ExprGt ex) {
        Record d= new Record(ex.hashCode(),optype2("<==>",ex.getEleft().accept(this),ex.getEright().accept(this)));
        stack.peek().addRecord(d);
        return optype2("<==>",ex.getEleft().accept(this),ex.getEright().accept(this));
    }

    @Override
    public Object visit(ExprId ex) {
        SymbolTable st=stack.peek();
        Record d=st.getRecord(ex.getId());
        if(d==null)
        {
            d= lookUp(ex.getId());
        }
        return d.getType();
    }

    @Override
    public Object visit(ExprIntConst ex) {
        Record d= new Record(ex.hashCode(), Type.Typet.INT);
        stack.peek().addRecord(d);
        return Type.Typet.INT;
    }

    @Override
    public Object visit(ExprLe ex) {
        Record d= new Record(ex.hashCode(),optype2("<==>",ex.getEleft().accept(this),ex.getEright().accept(this)));
        stack.peek().addRecord(d);
        return optype2("<==>",ex.getEleft().accept(this),ex.getEright().accept(this));
    }

    @Override
    public Object visit(ExprList ex) {
        for(Expr e:ex.getElist())
        {
            return e.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(ExprLt ex) {
        Record d= new Record(ex.hashCode(),optype2("<==>",ex.getEleft().accept(this),ex.getEright().accept(this)));
        stack.peek().addRecord(d);
        return optype2("<==>",ex.getEleft().accept(this),ex.getEright().accept(this));
    }

    @Override
    public Object visit(ExprMinus ex) {
        Record d= new Record(ex.hashCode(),optype2("-",ex.getEleft().accept(this),ex.getEright().accept(this)));
        stack.peek().addRecord(d);
        return optype2("-",ex.getEleft().accept(this),ex.getEright().accept(this));
    }

    @Override
    public Object visit(ExprMinusUn ex) {
        Record d= new Record(ex.hashCode(), optype1("-",ex.getEleft().accept(this)));
        stack.peek().addRecord(d);
        return optype1("-",ex.getEleft().accept(this));
    }

    @Override
    public Object visit(ExprNe ex) {
        Record d= new Record(ex.hashCode(),optype2("<==>",ex.getEleft().accept(this),ex.getEright().accept(this)));
        stack.peek().addRecord(d);
        return optype2("<==>",ex.getEleft().accept(this),ex.getEright().accept(this));
    }

    @Override
    public Object visit(ExprNot ex) {
        Record d= new Record(ex.hashCode(), optype1("not",ex.getEnode().accept(this)));
        stack.peek().addRecord(d);
        return optype1("not", ex.getEnode().accept(this));
    }

    @Override
    public Object visit(ExprNull ex) {
        Record d= new Record(ex.hashCode(), Type.Typet.NULL);
        stack.peek().addRecord(d);
        return Type.Typet.NULL;
    }

    @Override
    public Object visit(ExprOr ex) {
        Record d= new Record(ex.hashCode(),optype2("or",ex.getEleft().accept(this),ex.getEright().accept(this)));
        stack.peek().addRecord(d);
        return optype2("or",ex.getEleft().accept(this),ex.getEright().accept(this));
    }

    @Override
    public Object visit(ExprPlus ex) {
        Record d= new Record(ex.hashCode(),optype2("+",ex.getEleft().accept(this),ex.getEright().accept(this)));
        stack.peek().addRecord(d);
        //System.out.println("tipo:"+optype2("+",ex.getEleft().accept(this),ex.getEright().accept(this)));
        return optype2("+",ex.getEleft().accept(this),ex.getEright().accept(this));
    }

    @Override
    public Object visit(ExprStringConst ex) {
        Record d= new Record(ex.hashCode(), Type.Typet.STRING);
        stack.peek().addRecord(d);
        return Type.Typet.STRING;
    }

    @Override
    public Object visit(ExprTimes ex) {
        Record d= new Record(ex.hashCode(),optype2("*",ex.getEleft().accept(this),ex.getEright().accept(this)));
        stack.peek().addRecord(d);
        return optype2("*",ex.getEleft().accept(this),ex.getEright().accept(this));
    }

    @Override
    public Object visit(ExprTrue ex) {
        Record d= new Record(ex.hashCode(), Type.Typet.BOOL);
        stack.peek().addRecord(d);
        return Type.Typet.BOOL;
    }

    @Override
    public Object visit(IdList ex) {
        //salvo in un arraylist tutti i tipi degli id
        ArrayList<Type.Typet>tipi=new ArrayList<>();
        Record j;
        for(String s:ex.getElist())
        {
            j=lookUp(s);
            tipi.add(j.getType());
        }
        return tipi;
    }

    @Override
    public Object visit(IdListInit ex) {
        SymbolTable st=stack.peek();
        Expr e=ex.getE();
        if(e!= null)
        {
            if(e.getObject() instanceof CallProc)
            {
                Record d=lookUp(((CallProc) e.getObject()).getId());
                //controllo se assegno ad una variabile una funzione che restituisce void
                if(d.getReturntype().get(0).equals(Type.Typet.VOID))
                {
                    throw new ErrorParser("Errore alla linea("+ex.getDx().getLine()+"): Errore nel passaggio di una variabile ad una funzione VOID");
                }
                else
                {
                    for(Type.Typet t:d.getReturntype())
                    {
                        //controllo se il tipo restituito della funzione sia lo stesso della variabile essendo in idlistinit controlliamo sempre il primo valore di ritorno, perchè deve essere sempre dello stesso tipo
                        //() int,int,int
                        if(!t.equals(d.getReturntype().get(0)))
                        {
                            throw new ErrorParser("Errore alla linea("+ex.getDx().getLine()+"): Errore di tipi nella result list");
                        }
                        //controllo se il tipo di ritorno della procedura sia uguale a quello della variabile.
                        else
                        {
                            Record j=lookUp(ex.getId());
                            if(j.getType().equals(d.getReturntype().get(0)));
                            else
                            {
                                throw new ErrorParser("Errore alla linea("+ex.getDx().getLine()+"): Tipi di ritorno diversi");
                            }
                        }
                    }
                }
            }
            else
            {
                for(String s: ex.getIdl().keySet())
                {
                    Record d=lookUp(s);
                    if(!( e.accept(this)!= null && d.getType().equals(e.accept(this)) || (e.accept(this)!= null && d.getType().equals(Type.Typet.FLOAT) && (e.accept(this).equals(Type.Typet.INT) || e.accept(this).equals(Type.Typet.FLOAT)))))
                    {
                        throw new ErrorParser("Errore alla linea("+ex.getDx().getLine()+"): Tipi di ritorno diversi");
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Object visit(IfStat ex) {
            //controllo l'espressione passata all'if che deve essere un booleano
            if(ex.getE().accept(this)==null)
            {
                throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + "): Nella condizione dell'IF, la variabile non è un valore booleano");
            }
            if (ex.getE().accept(this).equals(Type.Typet.BOOL)) ;
            else {
                throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + "): Nella condizione dell'IF, la variabile non è un valore booleano");
            }
            ex.getSl().accept(this);
        ex.getEl().accept(this);
        ex.getEls().accept(this);
        return null;
    }

    @Override
    public Object visit(ParamDeclList ex) {
        for(ParDecl p:ex.getPl()) //per ogni dichiarazione di parametro all'interno della lista chiamiamo la pardecl.accept
        {
            p.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(ParDecl ex) {
        SymbolTable st=stack.peek(); //si prende il riferimento alla symbol table al top dello stack
        for(String s:ex.getIl().getElist()) //per ogni parametro si crea un nuovo record perchè i parametri della funzione sono le variabile della funzione perche controlla se le variabili all'interno della funzione sono uguali ai parametri all'interno della fuznione
        {
            Record r= new Record(ex,s);
            st.addRecord(r);
        }
        return null;
    }

    @Override
    public Object visit(Proc ex) {
        SymbolTable st=ex.getSt(); //Prendiamo la symboltable associata alla procedura
        stack.push(st);
        if(ex.getP()!=null){ //controlliamo se la funziona ha parametri
            ex.getP().accept(this);
        }
        ex.getV().accept(this); //controlliamo la dichiarazione delle variabili
        if(ex.getS()!=null)//controlliamo tutti gli stat
        {
            ex.getS().accept(this);
        }
        ex.getRe().accept(this);
        stack.pop();
        return null;
    }

    @Override
    public Object visit(ProcList ex) {
        for(Proc x: ex.getP())
        {
            x.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Program ex) {
        global= ex.getGlobaltable();//prendo il riferimento della symboltable globale
        stack.push(global);
        ex.getV().accept(this);//controllo la dichiarazione di variabili
        ex.getP().accept(this);//controllo la dichiarazione di procedure
        return null;
    }

    @Override
    public Object visit(ReadlnStat ex) {
        Iterator<SymbolTable>sta= stack.iterator();
        ArrayList<String> stringhe=ex.getIdlist().getElist();
        Record d=null;
        for(String s:stringhe){
            while(d==null)
            {
                d=sta.next().getRecord(s);
            }
                //controllo che nella read venga passata una stringa, un intero o un float
                if (!(d.getType().equals(Type.Typet.STRING)||d.getType().equals(Type.Typet.FLOAT)|| d.getType().equals(Type.Typet.INT))) {
                    throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + "): La variabile "+d.getId()+" è di tipo "+d.getType()+"la read vuole un tipo string");
                }
        }
        return null;
    }

    @Override
    public Object visit(ResultType ex) {
        return null;
    }

    @Override
    public Object visit(ResultTypeList ex) {
        return null;
    }

    @Override
    public Object visit(ReturnExprs ex) {
        SymbolTable st=stack.peek();
        ArrayList<Type.Typet>ret=getResultTypeList(st.getName()); // ottengo i tipi di ritorno della procedura
            for(int i=0;i<ret.size();i++)
            {
                if(ex.getEl()!=null)
                {
                    //System.out.println(ex.getEl().getElist().get(i));
                    //controllo i tipi di ritorno effettivi con quelli della result type list
                    if(!(ret.get(i).equals(ex.getEl().getElist().get(i).accept(this)) || (ret.get(i).equals(Type.Typet.FLOAT)&&ex.getEl().getElist().get(i).accept(this).equals(Type.Typet.INT))))
                    {
                        //controllo se nelle espressioni di ritorno ho passato una funzione e confronto i suoi tipi di ritorno con la result list
                        if(ex.getEl().getElist().get(i) instanceof ExprCall)
                        {
                            Record d= lookUp(((ExprCall) ex.getEl().getElist().get(i)).getC().getId());
                            ArrayList<Type.Typet>tipi=d.getReturntype();
                            for(int j=0;j<tipi.size();j++)
                            {
                                if(!tipi.get(j).equals(ret.get(i)))
                                {
                                    throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + "): Il tipo dei parametri restituiti nella funzione non sono quelli aspettati");
                                }
                                else
                                {
                                    i++;
                                }
                            }
                        }
                        else
                        {
                            throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + "): Il tipo dei parametri restituiti nella funzione non sono quelli aspettati");
                        }
                    }
                }
            }
        return null;
    }


    @Override
    public Object visit(Stat ex) {
        return null;
    }

    @Override
    public Object visit(StatAssign ex) {
        ex.getAs().accept(this);
        return null;
    }

    @Override
    public Object visit(StatCallProc ex) {
        ex.getCp().accept(this);
        return null;
    }

    @Override
    public Object visit(StatIf ex) {
        ex.getS().accept(this);
        return null;
    }

    @Override
    public Object visit(StatList ex) {
        for(Stat x:ex.getS())
        {
            x.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(StatReadln ex) {
        ex.getRln().accept(this);
        return null;
    }

    @Override
    public Object visit(StatWhile ex) {
        ex.getWs().accept(this);
        return null;
    }

    @Override
    public Object visit(StatWrite ex) {
        ex.getWs().accept(this);
        return null;
    }

    @Override
    public Object visit(Type ex) {
        return null;
    }

    @Override
    public Object visit(TypeBool ex) {
        return null;
    }

    @Override
    public Object visit(TypeFloat ex) {
        return null;
    }

    @Override
    public Object visit(TypeInt ex) {
        return null;
    }

    @Override
    public Object visit(TypeString ex) {
        return null;
    }

    @Override
    public Object visit(VarDecl ex) {
        SymbolTable st=stack.peek(); //si prende la prima symbol table sopra lo stack
        Record d= new Record(ex);
        st.addRecord(d);
        ex.getIdl().accept(this);
        return null;
    }

    @Override
    public Object visit(VarDeclList ex) {
        for(VarDecl x:ex.getV())
        {
            x.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(WhileStat ex) {
        //controllo che l'espressione di condizione nel while stat venga passato un booleano
            if (ex.getE().accept(this).equals(Type.Typet.BOOL)) ;
            else {
                throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + "): Nella condizione del WHILE, la variabile non è un valore booleano");
            }
        if(ex.getSl() != null)
        {
            ex.getSl().accept(this);
        }
        ex.getSl2().accept(this);
        return null;
    }

    @Override
    public Object visit(WriteStat ex) {
        for(Expr e:ex.getEl().getElist())
        {
            e.accept(this);
        }
        return null;
    }

    private ArrayList<Type.Typet> getResultTypeList(String name) {
        Record d=global.getRecord(name);
        return d.getReturntype();
    }

    private Record lookUp(String s){
        Iterator<SymbolTable>sta=stack.iterator();
        Record d=null;
        while(sta.hasNext() && d==null)
        {
            d=sta.next().getRecord(s);
            if(d!=null)
            {
                break;
            }
        }
        return d;
    }

    private Type.Typet optype2(String op, Object t1, Object t2)
    {
        if(t1 == null || t2 == null)
        {
            return null;
        }
        switch (op)
        {
            case "+":
                if(t1.equals(Type.Typet.INT) && t2.equals(Type.Typet.INT))
                {
                    return Type.Typet.INT;
                }
                else if(t1.equals(Type.Typet.INT) && t2.equals(Type.Typet.FLOAT))
                {
                    return Type.Typet.FLOAT;
                }
                else if(t1.equals(Type.Typet.FLOAT) && t2.equals(Type.Typet.INT))
                {
                    return Type.Typet.FLOAT;
                }
                else if(t1.equals(Type.Typet.FLOAT) && t2.equals(Type.Typet.FLOAT))
                {
                    return Type.Typet.FLOAT;
                }break;
            case "-":
                if(t1.equals(Type.Typet.INT) && t2.equals(Type.Typet.INT))
                {
                    return Type.Typet.INT;
                }
                else if(t1.equals(Type.Typet.INT) && t2.equals(Type.Typet.FLOAT))
                {
                    return Type.Typet.FLOAT;
                }
                else if(t1.equals(Type.Typet.FLOAT) && t2.equals(Type.Typet.INT))
                {
                    return Type.Typet.FLOAT;
                }
                else if(t1.equals(Type.Typet.FLOAT) && t2.equals(Type.Typet.FLOAT))
                {
                    return Type.Typet.FLOAT;
                }break;
            case "*":
                if(t1.equals(Type.Typet.INT) && t2.equals(Type.Typet.INT))
                {
                    return Type.Typet.INT;
                }
                else if(t1.equals(Type.Typet.INT) && t2.equals(Type.Typet.FLOAT))
                {
                    return Type.Typet.FLOAT;
                }
                else if(t1.equals(Type.Typet.FLOAT) && t2.equals(Type.Typet.INT))
                {
                    return Type.Typet.FLOAT;
                }
                else if(t1.equals(Type.Typet.FLOAT) && t2.equals(Type.Typet.FLOAT))
                {
                    return Type.Typet.FLOAT;
                }break;
            case "/":
                if(t1.equals(Type.Typet.INT) && t2.equals(Type.Typet.INT))
                {
                    return Type.Typet.INT;
                }
                else if(t1.equals(Type.Typet.INT) && t2.equals(Type.Typet.FLOAT))
                {
                    return Type.Typet.FLOAT;
                }
                else if(t1.equals(Type.Typet.FLOAT) && t2.equals(Type.Typet.INT))
                {
                    return Type.Typet.FLOAT;
                }
                else if(t1.equals(Type.Typet.FLOAT) && t2.equals(Type.Typet.FLOAT))
                {
                    return Type.Typet.FLOAT;
                }break;
            case "and":
                if(t1.equals(Type.Typet.BOOL) && t2.equals(Type.Typet.BOOL))
                {
                    return Type.Typet.BOOL;
                }break;
            case "or":
                if(t1.equals(Type.Typet.BOOL) && t2.equals(Type.Typet.BOOL))
                {
                    return Type.Typet.BOOL;
                }break;
            case "<==>":
                if(t1.equals(Type.Typet.BOOL) && t2.equals(Type.Typet.BOOL))
                {
                    return Type.Typet.BOOL;
                }
                else if(t1.equals(Type.Typet.INT) && t2.equals(Type.Typet.INT))
                {
                    return Type.Typet.BOOL;
                }
                else if(t1.equals(Type.Typet.INT) && t2.equals(Type.Typet.FLOAT))
                {
                    return Type.Typet.BOOL;
                }
                else if(t1.equals(Type.Typet.FLOAT) && t2.equals(Type.Typet.INT))
                {
                    return Type.Typet.BOOL;
                }else if(t1.equals(Type.Typet.FLOAT) && t2.equals(Type.Typet.FLOAT))
                {
                    return Type.Typet.BOOL;
                }
                break;

        }
        return null;
    }
    private Type.Typet optype1(String op, Object t1){
        switch(op)
        {
            case "-":
                if(t1.equals(Type.Typet.INT))
                {
                    return Type.Typet.INT;
                }
                else if(t1.equals(Type.Typet.FLOAT))
                {
                    return Type.Typet.FLOAT;
                }
                break;

            case "not":
                if(t1.equals(Type.Typet.BOOL))
                {
                    return Type.Typet.BOOL;
                }break;
        }
        return null;
    }
}
