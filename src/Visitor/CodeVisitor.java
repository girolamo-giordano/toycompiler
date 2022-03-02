package Visitor;

import Expressions.*;
import Util.Record;
import Util.SymbolTable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Stack;

public class CodeVisitor implements VisitorInterface<Object> {
    private Stack<SymbolTable> stack= new Stack<>();
    private SymbolTable global;
    private StringBuilder sb;
    private int globalc=0;
    private LinkedHashMap<Integer,ArrayList<String>> hashmap=new LinkedHashMap<>();
    private ArrayList<String>allfunct=new ArrayList<>();
    private boolean writestat=false;

    @Override
    public Object visit(AssignStat ex) {

        ArrayList<String>listaid=ex.getIdl().getElist();//lista contenente tutti gli id dell'assignstat
        ArrayList<Expr>listaexpr=ex.getE().getElist();//lista contenente tutti gli expr dell'assignstat
        int count=0;

        for(int i=0;i<listaexpr.size();i++)
        {
            Expr e= listaexpr.get(i);
            if(e.getObject() instanceof CallProc)
            {
                //Record d=lookUp(((CallProc) e.getObject()).getId());
                ((CallProc) e.getObject()).accept(this); //se come expr mi ritrovo una callproc, effettuo una chiamata con l'accept per ottenere la sua variabile dove viene salvato il valore di ritorno
                ArrayList<String> tmpval=hashmap.get(e.getObject().hashCode());//qui ottengo l'arraylist delle variabili che hanno il valore di ritorno della callproc all'interno della listexpr di assignstat
                if(tmpval != null)
                {
                    for(String s:tmpval)
                    {
                        //faccio la lookup per capire il tipo del tipo di ritorno
                        Record k=lookUp(listaid.get(count));
                        //System.out.println(k);
                        if(k!= null && k.getType().equals(Type.Typet.STRING))
                        {
                            sb.append("strcpy("+listaid.get(count)+","+s+");\n");
                        }
                        else
                        {

                            sb.append(listaid.get(count)+"="+s+";\n");
                        }
                        count++;
                    }
                }
                else
                {
                    //scrivo se la callproc non ha variabili temporanee dove ha salvato il suo valore di ritorno
                    sb.append(listaid.get(count)+"=");
                    ((CallProc) e.getObject()).accept(this);
                    count++;
                }
            }
            else
            {
                Record l=null;
                if(e instanceof ExprId)
                {
                    l=lookUp(((ExprId) e).getId());
                }
                //controllo se l'expr è una stringconst o un id che rappresenta la stringa così da scrivere strcpy
                if(e instanceof ExprStringConst || (l != null && l.getType().equals(Type.Typet.STRING)))
                {
                    sb.append("strcpy("+listaid.get(count)+",");
                    if(l!= null && l.getType().equals(Type.Typet.STRING))
                    {
                        e.accept(this);
                    }
                    else
                    {
                        sb.append("\"");
                        e.accept(this);
                        sb.append("\"");
                    }
                    sb.append(")");
                }
                else
                {
                    //qui assegno una stringa ad un'altra stringa
                    Record m=lookUp(listaid.get(count));
                    if(m!= null && m.getType().equals(Type.Typet.STRING))
                    {
                        sb.append("strcpy("+listaid.get(count)+",");
                        e.accept(this);
                        sb.append(")");
                    }
                    else
                    {
                        ArrayList<Expr>sprs=new ArrayList<>();//lista utilizzata per vedere se nell'albero ci sono altre chiamate ad exprcall, questo per avere il valore di ritorno prima che venga chiamata
                        sprs.add(e);
                        getAllexprC(sprs);
                        if(finderExprCall(e))
                        {
                            writestat=true;//variabile che definisce il comportamento all'interno dell'exprcall
                            sb.append(listaid.get(count)+"=");
                            e.accept(this);
                            //sb.deleteCharAt(sb.length()-1);
                            writestat=false;
                        }
                        else
                        {
                            sb.append(listaid.get(count)+"=");
                            e.accept(this);
                        }
                    }
                    /*if(e.getObject() instanceof ExprCall || e.getObject2() instanceof ExprCall)
                    {
                        sb.deleteCharAt(sb.length()-1);
                        sb.deleteCharAt(sb.length()-1);
                    }*/

                }
                sb.append(";\n");
                count++;

            }
        }

        /*for(int i=0;i<listaexpr.size();i++)
        {
            if(listaexpr.get(i).getObject2()!=null)
            {
                if(listaexpr.get(i).getObject2() instanceof ExprCall)
                {
                    e2= (Expr) listaexpr.get(i).getObject2();
                    ((ExprCall) listaexpr.get(i).getObject2()).accept(this);
                }
            }
            if(listaexpr.get(i).getObject()!=null)
            {
                if(listaexpr.get(i).getObject() instanceof ExprCall)
                {
                    e1= (Expr) listaexpr.get(i).getObject2();
                    ((ExprCall) listaexpr.get(i).getObject()).accept(this);
                }
            }
            if(listaexpr.get(i).getObject() instanceof CallProc)
            {
                ((CallProc) listaexpr.get(i).getObject()).accept(this);
            }
        }
        for(int i=0;i<listaexpr.size();i++)
        {
            if( (e1 !=null && e1.getObject() instanceof CallProc) || (e2!=null && e2.getObject() instanceof CallProc ))
            {
                if(e1 != null)
                {
                    if (e1.getObject() instanceof CallProc)
                    {
                        ArrayList<String>tempv=hashmap.get(e1.getObject().hashCode());
                        if(tempv!=null)
                        {
                            for(String s:tempv)
                            {
                                sb.append(listaid.get(count)+"="+s+";\n");
                                count++;
                            }
                            count=0;
                        }
                    }
                }
                if(e2 != null)
                {
                    if (e2.getObject() instanceof CallProc)
                    {
                        ArrayList<String>tempv=hashmap.get(e2.getObject().hashCode());
                        if(tempv!=null)
                        {
                            for(String s:tempv)
                            {
                                sb.append(listaid.get(count)+"="+s+";\n");
                                count++;
                            }
                            count=0;
                        }
                    }
                }

            }
            if(listaexpr.get(i).getObject() instanceof CallProc)
            {
                ArrayList<String>tempv=hashmap.get(listaexpr.get(i).getObject().hashCode());
                if(tempv!=null)
                {
                    for(String s:tempv)
                    {
                        sb.append(listaid.get(count)+"="+s+";\n");
                        count++;
                    }
                }
            }
            else
            {
                Expr e=listaexpr.get(i);
                if(e instanceof ExprStringConst || e instanceof ExprId || e instanceof  ExprFloatConst || e instanceof  ExprIntConst)
                {
                    sb.append(listaid.get(count)+"=");
                    e.accept(this);
                    sb.append(";\n");
                }
                else
                {
                    sb.append(listaid.get(count)+"=");
                    listaexpr.get(i).accept(this);
                    sb.append(";\n");
                }
                count++;
            }
        }
        while(count<listaid.size())
        {
            if(listaexpr.get(listaexpr.size()-1).getObject() instanceof CallProc)
            {
                ArrayList<String>tempv=hashmap.get(listaexpr.get(listaexpr.size()-1).getObject().hashCode());
                if(tempv!=null)
                {
                    for(String s:tempv)
                    {
                        sb.append(listaid.get(count)+"="+s+";\n");
                        count++;
                    }
                }
                else {
                    count++;
                }
            }
            else
            {
                Expr e=listaexpr.get(listaexpr.size()-1);
                if(e instanceof ExprStringConst || e instanceof ExprId || e instanceof  ExprFloatConst || e instanceof  ExprIntConst)
                {
                    sb.append(listaid.get(count)+"=");
                    e.accept(this);
                    sb.append(";\n");
                }
                else
                {
                    sb.append(listaid.get(count)+"=");
                    listaexpr.get(listaexpr.size()-1).accept(this);
                    sb.append(";\n");
                }
                count++;
            }
        }*/
        return null;
    }

    @Override
    public Object visit(CallProc ex) {
        ArrayList<String>tempval;//lista che conterrà i valori di eventuali exprcall chiamati all'interno della procedura
        ArrayList<String>strtemp=new ArrayList<>();//lista utilizzata per salvare le variabili globali instanziate con la callproc che verranno salvate all'interno dell'hashmap
        Record d=lookUp(ex.getId());
        if(ex.getList()!=null || d.getReturntype().size()>0)
        {
            if(ex.getList()!= null)
            {
                //controllo se ci sono delle exprcall, perchè il loro risultato deve essere scritto in una variabile temp prima che possa essere utilizzato
                for(Expr e:ex.getList().getElist())
                {
                    if(e instanceof ExprCall)
                    {
                        e.accept(this);
                    }
                }
            }
            boolean added=false;
            sb.append(ex.getId()+"(");
            if(ex.getList() != null)
            {
                for(Expr e:ex.getList().getElist())
                {
                    //inserisco come parametri le variabili temporanee che sono il return delle exprcall passate come parametri
                    if(e instanceof ExprCall)
                    {
                        tempval=hashmap.get(((ExprCall) e).getC().hashCode());
                        for(String s:tempval)
                        {
                            sb.append(s+",");
                        }
                    }
                    //altrimenti procedo normalmente chiamando l'accept di tutti gli altri elementi
                    else {
                        e.accept(this);
                        sb.append(",");
                    }
                }
                added=true;
            }
            //vengono instanziate le variabili globali per le procedure che saranno passati come parametri per salvare il valore di ritorno
            if(d.getReturntype().size()>0)//>1
            {
                for(Type.Typet t:d.getReturntype())
                {
                    //se la callproc resitituisce void le variabili di ritorno non vengono chiamate
                    if(!t.equals(Type.Typet.VOID))
                    {
                        //per le stringhe le variabili globali sono instanziate come arrey di char
                        if(t.equals(Type.Typet.STRING))
                        {
                            sb.insert(sb.lastIndexOf("//variabili globali\n"),"\n"+"char temp"+globalc+"[999];\n");
                            strtemp.add("temp"+globalc);
                            sb.append("temp"+globalc+",");
                            globalc++;
                        }
                        //altrimenti vengono instanziati con il loro tipo dato dal valore di ritorno della callproc
                        else
                        {
                            sb.insert(sb.lastIndexOf("//variabili globali\n"),"\n"+t.toString().toLowerCase()+" temp"+globalc+"=0;\n");
                            strtemp.add("temp"+globalc);
                            sb.append("&temp"+globalc+",");
                            globalc++;
                        }
                        added=true;
                    }

                }
                //salvo i valori delle variabili di ritorno all'interno di un hashmap
                hashmap.put(ex.hashCode(),strtemp);
            }
            if(added)
                sb.deleteCharAt(sb.length()-1);
            sb.append(");\n");

        }
        //caso in cui non ha ne parametri e ne valori di ritorno
        else {
            sb.append(ex.getId()+"();\n");
        }
        return null;
    }

    @Override
    public Object visit(Elif ex) {
        return null;
    }

    @Override
    public Object visit(ElifList ex) {
        for(Elif e:ex.getElif())
        {
            /*if(e.getE().getObject() instanceof ExprCall)
            {
                if(((ExprCall) e.getE().getObject()).getC().getList() != null)
                {
                    System.out.println("errore: non puoi usare nell'elif una funzione con parametri");
                }
            }
            if(e.getE().getObject2() instanceof ExprCall)
            {
                if(((ExprCall) e.getE().getObject2()).getC().getList() != null)
                {
                    System.out.println("errore: non puoi usare nell'elif una funzione con parametri");
                }
            }*/
            //controllo se l'expr è una exprcall
            sb.append("else if(");
            if(finderExprCall(e.getE()))
            {
                writestat=true;
                e.getE().accept(this);
                writestat=false;
            }
            //System.out.println(e.getE().getClass());
            /*if(e.getE().getObject() instanceof ExprCall || e.getE().getObject2() instanceof ExprCall)
            {
                if(e.getE().getObject() instanceof ExprCall)
                {
                    //ArrayList<String> str=hashmap.get(((ExprCall) e.getE().getObject()).getC().hashCode());
                    //System.out.println(str.get(0));
                    //e.getE().setObject(str.get(0));
                    writestat=true;
                    //sb.deleteCharAt(sb.lastIndexOf(";"));
                }
                if(e.getE().getObject2() instanceof ExprCall)
                {
                    //ArrayList<String> str=hashmap.get(((ExprCall) e.getE().getObject2()).getC().hashCode());
                    //System.out.println(str.get(0));
                    //e.getE().setObject2(str.get(0));
                    writestat=true;
                    //sb.deleteCharAt(sb.lastIndexOf(";"));
                }
                e.getE().accept(this);
                writestat=false;
                //sb.deleteCharAt(sb.lastIndexOf("\n"));
                //sb.deleteCharAt(sb.lastIndexOf("\n"));
            }*/
            else
            {
                e.getE().accept(this);
            }
            sb.append(")\n{\n");
            e.getS().accept(this);
            sb.append("}\n");
        }
        return null;
    }

    @Override
    public Object visit(Else ex) {

        if(ex.getSl()!=null) {
            sb.append("else\n{\n");
            ex.getSl().accept(this);
            sb.append("}\n");
        }

        return null;
    }

    @Override
    public Object visit(Expr ex) {
        //ex.accept(this);
        return null;
    }

    @Override
    public Object visit(ExprAnd ex) {
        ex.getEleft().accept(this);
        sb.append("&&");
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprCall ex) {
        Record d=lookUp(ex.getC().getId());
        boolean corr=false; //un booleano che ci informa se abbiamo inserito delle variabili riferite a delle exprcall per poi poter eliminare l'ultima virgola
        ArrayList<String> strtemp=new ArrayList<>();//variabili associati ai valori di ritorno delle exprcall chiamate
        if(!writestat) {
            //inizializzo le variabili globali delle exprcall
            for (Type.Typet t : d.getReturntype()) {
                if (t.equals(Type.Typet.STRING)) {
                    sb.insert(sb.lastIndexOf("//variabili globali\n"), "\n" + "char* temp" + globalc + "=0;\n");
                    strtemp.add("temp" + globalc);
                    globalc++;
                } else {
                    if(!t.equals(Type.Typet.VOID))
                    {
                        sb.insert(sb.lastIndexOf("//variabili globali\n"), "\n" + t.toString().toLowerCase() + " temp" + globalc + "=0;\n");
                        strtemp.add("temp" + globalc);
                        globalc++;
                    }

                }

            }
            //salvo le variabili instanziate per conservare il valore di ritorno all'interno dell'hashmap
            hashmap.put(ex.getC().hashCode(), strtemp);
            //controllo se l'exprcall abbia altri exprcall così da instanziare altre variabili
            if (ex.getC().getList() != null) {
                for (Expr e : ex.getC().getList().getElist()) {
                    if (e instanceof ExprCall)
                        e.accept(this);
                }
            }
            if (d.getReturntype().size() > 0) {
                //sb.append(strtemp.get(0)+"="+ex.getC().getId()+"(");
                sb.append(ex.getC().getId() + "(");
                if (ex.getC().getList() != null) {
                    for (Expr e : ex.getC().getList().getElist()) {
                        //se l'expr passato come parametro all'exprcall è un exprcall mi prendo le variabili che riferiscono al suo valore di ritorno all'interno dell'hashmap
                        if (e instanceof ExprCall) {
                            ArrayList<String> strngexpr = hashmap.get(((ExprCall) e).getC().hashCode());
                            for (String s : strngexpr) {
                                sb.append(s + ",");
                            }
                            corr = true;
                        } else {
                            e.accept(this);
                            sb.append(",");
                            corr = true;
                        }
                    }
                    //passo le variabili in cui verranno salvati i valori di ritorno
                    for (String s : strtemp) {
                        sb.append("&" + s + ",");
                    }
                } else {
                    //se la callproc non ha parametri inserisco solo le variabili dove verranno salvati i valori di ritorno
                    ArrayList<String> str = hashmap.get(ex.getC().hashCode());
                    for (String s : str) {
                        sb.append("&" + s + ",");
                        corr = true;
                    }
                }
                if (corr)
                    sb.deleteCharAt(sb.length() - 1);
                sb.append(");\n");
            } else {
                //il caso in cui i valori di ritorno sono uguali a 0 è chiamo direttamente l'exprcall
                sb.append(ex.getC().getId() + "(");
                if (ex.getC().getList() != null) {
                    for (Expr e : ex.getC().getList().getElist()) {
                        if (e instanceof ExprCall) {
                            ArrayList<String> strngexpr = hashmap.get(((ExprCall) e).getC().hashCode());
                            for (String s : strngexpr) {
                                sb.append(s + ",");
                            }
                            corr = true;
                        } else {
                            corr = true;
                            e.accept(this);
                            sb.append(",");
                        }
                    }
                }
                for (String s : strtemp) {
                    sb.append("&" + s + ",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(");\n");
            }
        }
        else
        {
            //viene utilizzato per inserire normalmente le variabili associate ai valori di ritorno
            ArrayList<String>str=hashmap.get(ex.getC().hashCode());
            if(str!=null)
            {
                for(String s:str)
                {
                    sb.append(s);
                }
            }
        }
        return null;
    }

    @Override
    public Object visit(ExprDiv ex) {
        //System.out.println(ex.hashCode());
        ex.getEleft().accept(this);
        sb.append("/");
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprEq ex) {
        /*if(ex.getEright() instanceof ExprCall)
        {
            ex.setEright(((ExprCall) ex.getEright()).getC());
        }
        if(ex.getEleft() instanceof ExprCall)
        {
            ex.setEleft(((ExprCall) ex.getEleft()).getC());
        }*/
        ex.getEleft().accept(this);
        sb.append("==");
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprFalse ex) {
        sb.append("false");
        return null;
    }

    @Override
    public Object visit(ExprFloatConst ex) {
        sb.append(ex.getI());
        return null;
    }

    @Override
    public Object visit(ExprGe ex) {
        ex.getEleft().accept(this);
        sb.append(">=");
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprGt ex) {
        ex.getEleft().accept(this);
        sb.append(">");
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprId ex) {
        sb.append(ex.getId());
        return null;
    }

    @Override
    public Object visit(ExprIntConst ex) {
        sb.append(ex.getI());
        return null;
    }

    @Override
    public Object visit(ExprLe ex) {
        ex.getEleft().accept(this);
        sb.append("<=");
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprList ex) {
        for(Expr e:ex.getElist())
        {
            e.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(ExprLt ex) {
        ex.getEleft().accept(this);
        sb.append("<");
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprMinus ex) {
        ex.getEleft().accept(this);
        sb.append("-");
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprMinusUn ex) {
        sb.append("-");
        ex.getEleft().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprNe ex) {
        boolean strcmp=false;
        if(ex.getEleft() instanceof ExprStringConst || ex.getEright() instanceof ExprStringConst) //controlli da fare agli altri operatori binari
        {
            sb.append("strcmp(");
            if(ex.getEleft() instanceof ExprStringConst)
            {

                sb.append("\"");
                ex.getEleft().accept(this);
                sb.append("\"");
            }
            else
            {
                ex.getEleft().accept(this);
            }
            strcmp=true;
            sb.append(",");
        }
        else
        {
            ex.getEleft().accept(this);
        }
        if(!strcmp)
            sb.append("!=");
        if(ex.getEright() instanceof ExprStringConst)
        {
            sb.append("\"");
            ex.getEright().accept(this);
            sb.append("\"");
        }
        else
        {
            ex.getEright().accept(this);
        }
        if(strcmp)
        {
            sb.append(")!=0");
        }
        return null;
    }

    @Override
    public Object visit(ExprNot ex) {
        sb.append("!");
        ex.getEnode().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprNull ex) {
        sb.append("nanl");
        return null;
    }

    @Override
    public Object visit(ExprOr ex) {
        ex.getEleft().accept(this);
        sb.append("||");
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprPlus ex) {
        ex.getEleft().accept(this);
        sb.append("+");
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprStringConst ex) {
        String s=ex.getS();
        if(s.contains("\n"))
        {
            s=s.replace("\n", "\\n");
        }
        sb.append(s);
        return null;
    }

    @Override
    public Object visit(ExprTimes ex) {
        /*
        if(ex.getEright() instanceof ExprCall)
        {
            ex.setEright(((ExprCall) ex.getEright()).getC());
        }
        if(ex.getEleft() instanceof ExprCall)
        {
            ex.setEleft(((ExprCall) ex.getEleft()).getC());
        }*/
        ex.getEleft().accept(this);
        sb.append("*");
        ex.getEright().accept(this);
        return null;
    }

    @Override
    public Object visit(ExprTrue ex) {
        sb.append("true");
        return null;
    }

    @Override
    public Object visit(IdList ex) {
        for(String s: ex.getElist())
        {
            sb.append(s+",");
        }
        sb.deleteCharAt(sb.length()-1);
        return null;
    }

    @Override
    public Object visit(IdListInit ex) {
        for(String s:ex.getIdl().keySet())
        {
            sb.append(s+",");
        }
        sb.deleteCharAt(sb.length()-1);
        if(ex.getE() != null)
        {
            sb.append("=");
            ex.getE().accept(this);
        }
        sb.append(";\n");
        return null;
    }

    @Override
    public Object visit(IfStat ex) {
        ArrayList<Expr>epr=new ArrayList<>();//lista per aggiungere l'expr e fare la ricerca ricorsiva se l'expr contiene exprcall
        epr.add(ex.getE());
        getAllexprC(epr);
        for(Elif e:ex.getEl().getElif())
        {
            //faccio la stessa chiamata ricorsiva per avere la creazioni delle variabili di ritorno delle callproc prima di generare il codice dell'if e dell'elif
            ArrayList<Expr>eprs=new ArrayList<>();
            eprs.add(e.getE());
            getAllexprC(eprs);
        }
        sb.append("if(");
        //System.out.println(ex.getE().getObject().getClass()+":"+ex.getE().getObject2().getClass());
        /*if(ex.getE().getObject() instanceof ExprCall || ex.getE().getObject() instanceof ExprMinus)
        {
            if(ex.getE().getObject() instanceof ExprMinus) // vanno aggiunte le altre operazioni binarie
            {
                System.out.println("errore: non puoi usare nell'if una funzione con parametri");
            }
            if(((ExprCall) ex.getE().getObject()).getC().getList() != null)
            {
                System.out.println("errore: non puoi usare nell'if una funzione con parametri");
            }
        }
        if(ex.getE().getObject2() instanceof ExprCall ||ex.getE().getObject2() instanceof ExprMinus )
        {
            if(ex.getE().getObject2() instanceof ExprMinus)
            {
                System.out.println("errore: non puoi usare nell'if una funzione con parametri");
            }
            if(((ExprCall) ex.getE().getObject2()).getC().getList() != null)
            {
                System.out.println("errore: non puoi usare nell'if una funzione con parametri");
            }
        }*/
        //se sono presenti exprcall setto la variabile writestat a true
        if(finderExprCall(ex.getE()))
        {
            writestat=true;
            ex.getE().accept(this);
            writestat=false;
        }
        /*if(ex.getE().getObject() instanceof ExprCall || ex.getE().getObject2() instanceof ExprCall)
        {
            if(ex.getE().getObject() instanceof ExprCall)
            {
                writestat=true;
            }
            if(ex.getE().getObject2() instanceof ExprCall)
            {
                writestat=true;
            }
            ex.getE().accept(this);
            writestat=false;
            /*sb.deleteCharAt(sb.lastIndexOf(";"));
            sb.deleteCharAt(sb.lastIndexOf(";"));
            sb.deleteCharAt(sb.lastIndexOf("\n"));
            sb.deleteCharAt(sb.lastIndexOf("\n"));
        }*/
        //altrimenti viene scritto direttamente l'expr
        else
        {
            if(ex.getE() instanceof ExprCall)
            {
                ex.getE().accept(this);
                sb.deleteCharAt(sb.length()-1);
                sb.deleteCharAt(sb.length()-1);
            }
            else
            {
                ex.getE().accept(this);
            }
        }
        sb.append(")\n{\n");
        for(Stat s:ex.getSl().getS())
        {
            s.accept(this);
        }
        sb.append("}\n");
        ex.getEl().accept(this);
        ex.getEls().accept(this);
        return null;
    }

    @Override
    public Object visit(ParamDeclList ex) {
        return null;
    }

    @Override
    public Object visit(ParDecl ex) {
        return null;
    }

    @Override
    public Object visit(Proc ex) {
        SymbolTable st=ex.getSt();
        String nfunc="";
        stack.push(st);
        sb.append(ex.getId()+"(");
        nfunc+=ex.getId()+"(";
        if(ex.getP() != null){
            // qui inserisco i parametri
            for(ParDecl e:ex.getP().getPl())
            {
                String stype=e.getT().getType().toString().toLowerCase();
                for(String id:e.getIl().getElist())
                {
                    if(stype.equals("string"))
                    {
                        sb.append("char* "+id+",");
                        nfunc+=("char* "+id+",");
                    }
                    else
                    {
                        if(!stype.equals("void"))
                        {
                            sb.append(stype+" "+id+",");
                            nfunc+=(stype+" "+id+",");
                        }
                    }
                }
                //e.getT().accept(this);
                //sb.append(" ");
                //e.getIl().accept(this);
                //sb.append(",");
                //sb.append(e.getT().getType().toString().toLowerCase()+",");
            }
            /*if(ex.getR().getR().size()<2)
            {
                sb.deleteCharAt(sb.length()-1);
                nfunc=nfunc.substring(0,nfunc.length()-1);
            }*/

        }
        //
        //qui inserisco i parametri che non sono altro che i valori di ritorno della funzione
        if(ex.getR().getR().size()>0) //1
        {
            for(int i=0;i<ex.getR().getR().size();i++)
            {
                if(!ex.getR().getR().get(i).getT().getType().equals(Type.Typet.VOID))
                {
                    if(ex.getR().getR().get(i).getT().getType().equals(Type.Typet.STRING))
                    {
                        sb.append("char *par"+i+",");
                        nfunc+="char *par"+i+",";
                    }
                    else
                    {
                        sb.append(ex.getR().getR().get(i).getT().getType().toString().toLowerCase()+" *par"+i+",");
                        nfunc+=ex.getR().getR().get(i).getT().getType().toString().toLowerCase()+" *par"+i+",";
                    }

                }
                else
                {
                    if(ex.getP() == null)
                    {
                        sb.append(",");
                        nfunc+=",";
                    }
                }
            }
            sb.deleteCharAt(sb.length()-1);
            nfunc=nfunc.substring(0,nfunc.length()-1);
        }
        nfunc+=");";
        sb.append(")");
        sb.append("\n{\n");
        //aggiungi alla funzione le variabili dichiarate
        ex.getV().accept(this);
        if(ex.getS()!=null){
            ex.getS().accept(this);
        }
        ex.getRe().accept(this);
        sb.append("}\n\n");
        stack.pop();//da rimuovere
        //il return serve per far si che ci sia una dichiarazione anticipata delle funzioni
        return nfunc;
    }

    @Override
    public Object visit(ProcList ex) {
        for(Proc c:ex.getP())
        {
            String s="";
            //controllo le funzioni con un tipo di ritorno e scrivo la funzione a partire dal tipo di ritorno
            if(c.getR().getR().size()==1)
            {
                //il tipo string corrisponde a char*
                if(c.getR().getR().get(0).getT().getType().equals(Type.Typet.STRING))
                {
                    sb.append("char* ");
                    s+="char* ";
                }
                else
                {
                    sb.append(c.getR().getR().get(0).getT().getType().toString().toLowerCase()+" ");
                    s+=c.getR().getR().get(0).getT().getType().toString().toLowerCase()+" ";
                }
            }
            //se la funzione restituisce più di un parametro faccio restituire void perchè gestisco i return con i puntatori
            else{
                sb.append("void ");
                s+="void ";
            }
            //sb.append(c.getId()+"(");
            s+=(String)c.accept(this);
            allfunct.add(s);
            //System.out.println(s);
            //sb.append(")\n");

        }
        return null;
    }

    @Override
    public Object visit(Program ex) {
        String functdec="";
        global=ex.getGlobaltable();
        stack.push(global);
        sb= new StringBuilder();
        sb.append("#include <stdio.h>\n#include <string.h>\n#include <math.h>\ntypedef int bool;\n#define false 0\n#define true 1\n\n\n");
        sb.append("//dichiarazioni funzioni\n");
        sb.append("//variabili globali\n");
        ex.getV().accept(this);
        sb.append("\n");
        ex.getP().accept(this);
        for(String s:allfunct)
        {
            functdec+=s+"\n";
        }
        sb.insert(sb.lastIndexOf("//dichiarazioni funzioni\n"),functdec);
        return sb.toString();
    }

    @Override
    public Object visit(ReadlnStat ex) {
        sb.append("scanf(\"");
        for(String s:ex.getIdlist().getElist())
        {
            Record d=lookUp(s);
            if(d.getType().equals(Type.Typet.FLOAT))
            {
                sb.append("%f");
            }
            if(d.getType().equals(Type.Typet.INT) || d.getType().equals(Type.Typet.BOOL))
            {
                sb.append("%d");
            }
            if(d.getType().equals(Type.Typet.STRING))
            {
                sb.append("%s");
            }

        }
        sb.append("\",");
        for(String s: ex.getIdlist().getElist())
        {
            sb.append("&"+s+",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(");\n");
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
        int count=0;
        //se la callproc ha un singolo valore di ritorno
        if(ex.getEl()!= null && ex.getEl().getElist().size()==1)
        {
            Expr e=ex.getEl().getElist().get(0);
                //se l'expr è una exprcall prendo il valore di ritorno associato ad esso e lo assegno alla variabile di ritorno della callproc
                if(e instanceof ExprCall)
                {
                    e.accept(this);
                    ArrayList<String>string=hashmap.get(((ExprCall) e).getC().hashCode());
                    if(string != null)
                    {
                        for(String s:string)
                        {
                            sb.append("*par"+count+"="+s+";\n");
                            count++;
                        }
                    }
                }
                else {
                    //viene controllato se l'expr è una exprstringconst
                    if(e instanceof ExprStringConst)
                    {
                        sb.append("strcpy(par0,\"");
                        e.accept(this);
                        sb.append("\");\n");
                    }
                    else
                    {
                        sb.append("*par0=");
                        e.accept(this);
                        sb.append(";\n");
                    }
                    sb.append("return ");
                    if(e instanceof ExprStringConst)
                    {
                        sb.append("\"");
                        e.accept(this);
                        sb.append("\"");
                        sb.append(";\n");
                    }
                    else
                    {
                        e.accept(this);
                        sb.append(";\n");
                    }
                }

        }
        else
        {
            //viene trattato il contesto in cui ci sia più di un valore di ritorno
            if(ex.getEl()!=null)
            {
                for(Expr e:ex.getEl().getElist())
                {
                    if(e instanceof ExprCall)
                    {
                        e.accept(this);
                        ArrayList<String>string=hashmap.get(((ExprCall) e).getC().hashCode());
                        if(string != null)
                        {
                            for(String s:string)
                            {
                                sb.append("*par"+count+"="+s+";\n");
                                count++;
                            }
                        }
                    }
                    else
                    {
                        if(e instanceof ExprStringConst)
                        {
                            sb.append("strcpy(par"+count+",\"");
                            e.accept(this);
                            sb.append("\");\n");
                        }
                        else
                        {
                            sb.append("*par"+count+"=");
                            e.accept(this);
                            sb.append(";\n");
                        }
                    }
                    count++;

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
        for(Stat s:ex.getS())
        {
            s.accept(this);
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
        sb.append(ex.getType().toString().toLowerCase());
        return null;
    }

    @Override
    public Object visit(TypeFloat ex) {
        sb.append(ex.getType().toString().toLowerCase());
        return null;
    }

    @Override
    public Object visit(TypeInt ex) {
        sb.append(ex.getType().toString().toLowerCase());
        return null;
    }

    @Override
    public Object visit(TypeString ex) {
        sb.append(ex.getType().toString().toLowerCase());
        return null;
    }

    @Override
    public Object visit(VarDecl ex) {
        String type=ex.getT().getType().toString().toLowerCase();
        //se la variabile è una stringa scrivo char
        for(String s:ex.getIdl().getIdl().keySet())
        {
            Expr e=ex.getIdl().getE();
            if(type.equals("string"))
            {
                sb.append("char "+s+"[999]");
            }
            else
            {
                if(!type.equals("void"))
                    sb.append(type+" "+s);
            }
            if(e!=null){
                sb.append("=");
                if(e instanceof ExprStringConst)
                {
                    sb.append("\"");
                }
                //System.out.println(s+":"+e.getClass());
                e.accept(this);
                if(e instanceof ExprStringConst)
                {
                    sb.append("\"");
                }
            }
            sb.append(";\n");
        }
        return null;
    }

    @Override
    public Object visit(VarDeclList ex) {
        for(VarDecl v:ex.getV())
        {
            v.accept(this);
        }
        return null;
    }

    @Override
    public Object visit(WhileStat ex) {
        sb.append("while(");
        ex.getE().accept(this);
        sb.append(")\n{\n");
        if(ex.getSl() != null)
        {
            ex.getSl().accept(this);
        }
        ex.getSl2().accept(this);
        sb.append("}\n");
        return null;
    }

    @Override
    public Object visit(WriteStat ex) {
        ArrayList<Expr>listexpr=new ArrayList<>();
        //chiamo il metodo per ottenere tutte le variabili corrispondenti al result della callproc
        getAllexprC(ex.getEl().getElist());
        sb.append("printf(\"");
        for(Expr e:ex.getEl().getElist())
        {
            Record d=lookUp(e.hashCode());
            if(d!= null)
            {
                //controllo se il record proviene da un'operazione binaria
                if(d.getT().equals(Record.Type.BIN))
                {
                    //l'unico caso in cui può essere null è quando non è uno string
                    if(d.getType()== null)
                    {
                        sb.append("%d");
                    }
                    else
                    {
                        if(d.getType().equals(Type.Typet.STRING))
                        {
                            sb.append("%s");
                        }
                        else if(d.getType().equals(Type.Typet.INT) || d.getType().equals(Type.Typet.BOOL))
                        {
                            sb.append("%d");
                        }
                        else if(d.getType().equals(Type.Typet.FLOAT))
                        {
                            sb.append("%f");
                        }
                    }

                }
            }
            //a seconda dell'oggetto faccio il giusto casting per il lookup
            else if(e.getObject() instanceof CallProc)
            {
                //System.out.println("presente");
                d=lookUp(((CallProc) e.getObject()).getId());
                //System.out.println(d.getReturntype().get(0));
                //d.setType(d.getReturntype().get(0));
            }
            else
            {
                d=lookUp((String) e.getObject());
            }
            //qui controllo se il valore è una variabile
            if(d.getT().equals(Record.Type.VAR))
            {
                if(d.getType().equals(Type.Typet.STRING))
                {
                    sb.append("%s");
                }
                else if(d.getType().equals(Type.Typet.INT) || d.getType().equals(Type.Typet.BOOL))
                {
                    sb.append("%d");
                }
                else if(d.getType().equals(Type.Typet.FLOAT))
                {
                    sb.append("%f");
                }
            }
            //qui controllo se la variabile è una procedura
            else if(d.getT().equals((Record.Type.PROC)))
            {
                //ArrayList<String>str=hashmap.get(e.getObject().hashCode());
                //System.out.println(str.size());
                //per ciascun valore di ritorno viene effettuata l'append del tipo
                for(Type.Typet t:d.getReturntype())
                {
                    if(t.equals(Type.Typet.STRING))
                    {
                        sb.append("%s");
                    }
                    else if(t.equals(Type.Typet.INT) || t.equals(Type.Typet.BOOL))
                    {
                        sb.append("%d");
                    }
                    else if(t.equals(Type.Typet.FLOAT))
                    {
                        sb.append("%f");
                    }
                }
            }
            //le expr vengono salvate tutte in una lista per poi essere richiamate dopo
            listexpr.add(e);
        }
        sb.append("\",");
        for(Expr e:listexpr)
        {
            //se è una stringa costante devo aggiungere i doppi apici
            if(e instanceof ExprStringConst)
            {
                sb.append("\"");
                e.accept(this);
                sb.append("\"");
            }
            else if(e instanceof ExprCall)
            {
                //chiamo i valori di ritorno della callproc
                ArrayList<String>str=hashmap.get(e.getObject().hashCode());
                for(String s:str)
                {
                    sb.append(s+",");
                }
                sb.deleteCharAt(sb.length()-1);
            }
            else
            {
                //qui controllo se uno degli oggetti dell'expr è un exprcall ed inserisco la variabile di ritorno exprcall settando writestat a true
                if(e.getObject()!= null && e.getObject() instanceof ExprCall)
                {
                    writestat=true;
                }
                if(e.getObject2() != null && e.getObject2() instanceof ExprCall)
                {
                    writestat=true;
                }
                e.accept(this);
                writestat=false;

            }
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(");\n");
        return null;
    }

    private Record lookUp(String s){
        Iterator<SymbolTable> sta=stack.iterator();
        Record d=null;
        while(sta.hasNext() && d==null)
        {
            SymbolTable st=sta.next();
            d=st.getRecord(s);
            if(d!=null)
            {
                break;
            }
        }
        return d;
    }

    private Record lookUp(int s){
        Iterator<SymbolTable> sta=stack.iterator();
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

    //metodo ricorsivo che permette di avere i valori di ritorno delle exprcall
    private void getAllexprC(ArrayList<Expr> exprs)
    {
        if(exprs!= null)
        {
            for(Expr e:exprs)
            {
                if(e instanceof ExprCall || e instanceof CallProc)
                {
                    //System.out.println(e.getObject().getClass());
                    e.accept(this);
                }
                else
                {
                    ArrayList<Expr>expr=new ArrayList<>();
                    if(e.getObject() != null && e.getObject() instanceof Expr)
                    {
                        expr.add((Expr) e.getObject());
                    }
                    if(e.getObject2() != null && e.getObject2() instanceof Expr)
                    {
                        expr.add((Expr) e.getObject2());
                    }
                    getAllexprC(expr);
                }
            }
        }
    }

    //metodo che trova in modo ricorsivo degli exprcall
    private boolean finderExprCall(Expr e)
    {
        boolean find=false;
        boolean find2=false;

        if(e.getObject() instanceof ExprCall)
        {
            return true;
        }
        if(e.getObject2() instanceof ExprCall)
        {
            return true;
        }
        if(e.getObject() instanceof Expr)
        {
            find=finderExprCall((Expr) e.getObject());
        }
        if(e.getObject2() instanceof Expr)
        {
            find2=finderExprCall((Expr) e.getObject2());
        }
        if(find || find2)
        {
            return true;
        }
        return false;
    }
}
