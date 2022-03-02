package Visitor;

import Expressions.*;
import Util.ErrorParser;
import Util.Record;
import Util.SymbolTable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class ScopeVisitor implements VisitorInterface<Object> {

    private Stack<SymbolTable> stack = new Stack<>(); //stack per tenere le symboltable
    private SymbolTable global; //symboltable globale

    @Override
    public Object visit(AssignStat ex) {
        ex.getIdl().accept(this);
        ex.getE().accept(this);
        String k = "";
        for (String s : ex.getIdl().getElist()) { //fa il controllo se la variabile è stata dichiarata
            Record d = lookUp(s);
            if (d == null) {
                k = s;
                throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + "): Variabile:" + k + " non dichiarata");
            }
        }
        int n = 0;
        for (int i = 0; i < ex.getE().getElist().size(); i++) {
            if (ex.getE().getElist().get(i) instanceof ExprCall) { //calcolo se ci sono delle chiamate a procedura e mi salvo le loro dimensione
                Record l = lookUp(((ExprCall) ex.getE().getElist().get(i)).getC().getId());
                n += l.getReturntype().size() - 1;
            }
        }
        //confronto le liste e verifico se il numero degli id è minore del numero associato alle espressioni
        if (ex.getE().getElist().size() + n > ex.getIdl().getElist().size()) {
            throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + ") :Numero di variabili minore del numero delle espressioni");
        }
        return null;
    }


    @Override
    public Object visit(CallProc ex) { //controlla se la funzione è già stata dichiarata
        SymbolTable st = stack.peek();
        Record d = lookUp(ex.getId());
        int n = 0;

        if (d == null) {
            throw new ErrorParser("Errore alla linea( " + ex.getDx().getLine() + ") : Funzione " + ex.getId() + " non dichiarata");
        }
        if (ex.getList() == null && d.getParamtype().size() > 0) { //controllo se la funzione ha ricevuto il giusto numero di parametri
            throw new ErrorParser("Errore alla linea( " + ex.getDx().getLine() + ") : Funzione " + ex.getId() + " non ha ricevuto il numero di parametri giusto");
        }
        if (ex.getList() != null) {
            for (Expr e : ex.getList().getElist()) {
                if (e.getObject() instanceof CallProc) { //calcolo numero parametri di ritorno se l'espressione è una funzione
                    Record j = lookUp(((CallProc) e.getObject()).getId());
                    n += j.getReturntype().size() - 1;
                }
            }

            //viene controllato la dimensione della lista delle espressioni e la dimensione della lista dei parametri
            if (ex.getList().getElist().size() + n != d.getParamtype().size()) {
                throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + ") : Funzione " + ex.getId() + " non ha ricevuto il numero di parametri giusto");
            }
        }
        if (ex.getList() != null) {
            ex.getList().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Elif ex) {
        ex.getE().accept(this);
        ex.getS().accept(this);
        return null;
    }

    @Override
    public Object visit(ElifList ex) {
        for (Elif e : ex.getElif()) {
            e.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Else ex) {
        if (ex.getSl() != null) {
            ex.getSl().accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Expr ex) {
        return null;
    }

    @Override
    public Object visit(ExprAnd ex) {
        ex.getEleft().accept(this);
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprCall ex) {
        ex.getC().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprDiv ex) {
        ex.getEleft().accept(this);
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprEq ex) {
        ex.getEleft().accept(this);
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprFalse ex) {
        return null;
    }

    @Override
    public Object visit(ExprFloatConst ex) {
        return null;
    }

    @Override
    public Object visit(ExprGe ex) {
        ex.getEleft().accept(this);
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprGt ex) {
        ex.getEleft().accept(this);
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprId ex) {
        SymbolTable st = stack.peek();

        if (lookUp(ex.getId()) == null) {
            throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + "): Variabile:" + ex.getId() + " non dichiarata");
        }
        return null;
    }

    @Override
    public Object visit(ExprIntConst ex) {
        return null;
    }

    @Override
    public Object visit(ExprLe ex) {
        ex.getEleft().accept(this);
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprList ex) {
        for (Expr e : ex.getElist()) {
            e.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(ExprLt ex) {
        ex.getEleft().accept(this);
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprMinus ex) {
        ex.getEleft().accept(this);
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprMinusUn ex) {
        ex.getEleft().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprNe ex) {
        ex.getEleft().accept(this);
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprNot ex) {
        ex.getEnode().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprNull ex) {
        return null;
    }

    @Override
    public Object visit(ExprOr ex) {
        ex.getEleft().accept(this);
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprPlus ex) {
        ex.getEleft().accept(this);
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprStringConst ex) {
        return null;
    }

    @Override
    public Object visit(ExprTimes ex) {
        ex.getEleft().accept(this);
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprTrue ex) {
        return null;
    }

    @Override
    public Object visit(IdList ex) {
        //se la lista dei parametri dello stesso tipo sia stato dichiarato piu di una volta
        if (ex.getIdlist().size() > 0) {
            throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + ") : Parametro: " + ex.getIdlist().get(0) + " già dichiarato\"");
        }
        return null;
    }

    @Override
    public Object visit(IdListInit ex) {
        int a = ex.getIdl().keySet().size();
        Expr e;
        if (ex.getE() != null) {
            e = ex.getE();
            if (e.getObject() instanceof CallProc) { //siamo nel caso in cui si dichiara una variabile a cui assegno il valore di ritorno di una funzione
                Record j = lookUp(((CallProc) e.getObject()).getId());
                if (a < j.getReturntype().size()) {
                    throw new ErrorParser("Errore alla linea("+ex.getDx().getLine()+"): Il numero dei parametri passati dalla procedura risulta maggiore di quello della lista degli id");
                }
            }
        }
        return null;
    }

    @Override
    public Object visit(IfStat ex) {
        ex.getE().accept(this);
        ex.getEl().accept(this);
        ex.getEls().accept(this);
        ex.getSl().accept(this);
        return null;
    }

    @Override
    public Object visit(ParamDeclList ex) {
        checkDoubleParam(ex);//controllo se vengono dichiarati parametri con lo stesso nome
        for (ParDecl p : ex.getPl()) //per ogni dichiarazione di parametro all'interno della lista chiamiamo la pardecl.accept
        {
            p.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(ParDecl ex) {
        SymbolTable st = stack.peek(); //si prende il riferimento alla symbol table al top dello stack

        //per ogni parametro si crea un nuovo record perchè i parametri della funzione sono le variabile della
        //funzione perche controlla se le variabili all'interno della funzione sono uguali ai parametri all'interno della fuznione
        for (String s : ex.getIl().getElist())
        {
            Record r = new Record(ex, s);
            st.addRecord(r);
        }
        ex.getIl().accept(this);
        return null;
    }

    @Override
    public Object visit(Proc ex) {
        SymbolTable st = new SymbolTable(ex.getId()); //creamo una nuova symbol table e facciamo il push all'interno dello stack
        stack.push(st);
        if (ex.getP() != null) { //controlliamo se la funziona ha parametri
            ex.getP().accept(this);
        }
        ex.getV().accept(this);
        ex.getR().accept(this);
        if (ex.getS() != null) {
            ex.getS().accept(this);
        }
        ex.getRe().accept(this);

        //controllo se non passo nessun espressione ad una funzione che resituisce un valore non VOID
        if (ex.getRe().getEl() == null) {
            if (!ex.getR().getR().get(0).getT().getType().equals(Type.Typet.VOID)) {
                throw new ErrorParser("Errore alla linea ( " + ex.getDx().getLine() + " ): Il numero di parametri restituiti dalla funzione ha numero diverso da quello aspettato");
            }
            // controllo se il numero di parametri restituito dalla funzione sia quello aspettato
        } else {
            if (ex.getRe().getEl() != null) {
                //System.out.println(ex.getId());
                {
                    //System.out.println("dente");
                    int count = 0;
                    for (Expr e : ex.getRe().getEl().getElist()) {

                        if (e instanceof ExprCall) {
                            Record d = lookUp(((ExprCall) e).getC().getId());
                            count += d.getReturntype().size();
                        } else {
                            count++;
                        }
                    }
                    if (ex.getR().getR().size() != count) {
                        throw new ErrorParser("Errore alla linea ( " + ex.getDx().getLine() + " ): Il numero di parametri restituiti dalla funzione ha numero diverso da quello aspettato");
                    }
                }
            }
        }
        ex.setSt(st);
        stack.pop();
        return null;
    }

    @Override
    public Object visit(ProcList ex) {
        for (Proc x : ex.getP()) {
            x.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(Program ex) {
        SymbolTable st = new SymbolTable("global");
        global = st;
        stack.push(st); //creiamo una symbol table globale

        //visita di tutti i processi dichiarati all'interno del program in modo tale che quando una una chiamata a funziona avviene pprima della
        // dichiarazione non da errore
        for (Proc p : ex.getP().getP())
        {
            Record d = new Record(p);
            if (global.contains(d)) {
                throw new ErrorParser("Errore alla linea( " + ex.getDx().getLine() + "): Funzione" + d.getId() + " già dichiarata");
            } else {
                global.addRecord(d);
            }
        }

        ex.setGlobaltable(global);
        ex.getV().accept(this); //controllo variabile
        ex.getP().accept(this); //controllo proc e creo stack per ogni procedura
        return null;
    }

    @Override
    public Object visit(ReadlnStat ex) {
        for (String s : ex.getIdlist().getElist()) {
            if (lookUp(s) == null) {
                throw new ErrorParser("Errore alla linea (" + ex.getDx().getLine() + "): Variabile:" + s + " non dichiarata ");
            }
        }
        ex.getIdlist().accept(this);
        return null;
    }

    @Override
    public Object visit(ResultType ex) {

        return null;
    }

    @Override
    public Object visit(ResultTypeList ex) {
        if (ex.getR().size() > 1) {

            for (ResultType r : ex.getR()) {
                if (r.getT().getType().equals(Type.Typet.VOID)) {
                    throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + "): La funzione deve restituire al massimo un VOID");
                }
            }
        }
        return null;
    }

    @Override
    public Object visit(ReturnExprs ex) {
        if (ex.getEl() != null) {
            ex.getEl().accept(this);
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
    public Object visit(StatList ex) { //chiamiamo tutti gli stat tranne write
        for (Stat s : ex.getS()) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(StatReadln ex) { //argomenti della read sono stati dichiarati
        ex.getRln().accept(this);
        return null;
    }

    @Override
    public Object visit(StatWhile ex) { //controlla lo statlist iniziale, l'espressione , e lo statlist del body
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
        SymbolTable st = stack.peek();//si prende la prima symbol table sopra lo stack
        for (String s : ex.getIdl().getIdl().keySet()) {
            Record d = new Record(ex, s);
            Record j = lookUp(s);

            //controlla se ci sono più variabili dichiarate , creiamo un nuovo record a partire
            // da vardecl e vediamo se questo è già all'interno della tabella
            //j.getT().equals(Record.Type.PROC) -> per non far chiamare una variabile con lo stesso nome di una procedura
            if (!(st.contains(d) || (j != null && j.getT().equals(Record.Type.PROC))))
            {
                st.addRecord(d);
            } else {
                if (d.getVardeclrip().size() > 0) {
                    throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + "): Parametro:" + d.getVardeclrip().get(0) + " già dichiarato");
                } else {
                    throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + "): Parametro:" + d.getId() + " già dichiarato");
                }
            }
        }
        ex.getIdl().accept(this);
        return null;
    }

    @Override
    public Object visit(VarDeclList ex) {
        for (VarDecl x : ex.getV()) {
            x.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(WhileStat ex) {
        if (ex.getSl() != null) {
            ex.getSl().accept(this);
        }
        ex.getE().accept(this);
        ex.getSl2().accept(this);
        return null;
    }

    @Override
    public Object visit(WriteStat ex) {
        for (Expr e : ex.getEl().getElist()) {
            /*if (e.getObject() instanceof CallProc) {
                Record d = lookUp(((CallProc) e.getObject()).getId());
                //if (d.getReturntype().size() > 1) {
                    //System.out.println("non puoi passare una funzione con più di un valore di ritorno nella write");
                //}
            }*/

            //controllo se nell'operazione binaria viene utilizzata una funzione che ha più di un parametro di ritorno
            if (e.getObject() instanceof ExprCall) {
                Record d = lookUp(((ExprCall) e.getObject()).getC().getId());
                if (d.getReturntype().size() > 1) {
                    throw new ErrorParser("non puoi passare una funzione con più di un valore di ritorno nella write");
                }
            }
            if (e.getObject2() instanceof ExprCall) {
                if (e.getObject2() != null) {
                    Record d = lookUp(((ExprCall) e.getObject2()).getC().getId());
                    if (d.getReturntype().size() > 1) {
                        throw new ErrorParser("non puoi passare una funzione con più di un valore di ritorno nella write");
                    }
                }

            }
            e.accept(this);
        }
        return null;
    }

    private Record lookUp(String s) {
        Iterator<SymbolTable> sta = stack.iterator();
        Record d = null;
        while (sta.hasNext() && d == null) {
            d = sta.next().getRecord(s);
            if (d != null) {
                break;
            }
        }
        return d;
    }

    private void checkDoubleParam(ParamDeclList ex) {
        for (int i = 0; i < ex.getPl().size() - 1; i++) //triplo for in cui si controlla e ci sono dichiarazioni con lo stesso nome ma con tipo di variabile differente all'interno della lista dei parametri
        {
            for (int j = i + 1; j < ex.getPl().size(); j++) {
                for (String s : ex.getPl().get(i).getIl().getElist()) {
                    if (ex.getPl().get(j).getIl().getElist().contains(s)) {
                        throw new ErrorParser("Errore alla linea(" + ex.getDx().getLine() + "): Parametro:" + s + " già dichiarato");
                    }
                }
            }
        }

    }
}

