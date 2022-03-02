package Visitor;

import Expressions.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class XMLVisitor implements VisitorInterface<Object> {

    private Document document;

    public XMLVisitor(){
        try{
            document=DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            System.err.println("Errore generazione xml");
        }
    }

    public void appendRoot(Element e){
        document.appendChild(e);
    }

    public Object visit(AssignStat assignStat) {
        Element e= document.createElement("AssignStat");
        LinkedHashMap<String,Expr> exs=assignStat.getIdle();
        exs.forEach((key, tab) -> {
                    Element j = document.createElement("AssignOp");
                    if(tab!=null) {
                        j.setTextContent("(ID,"+key+")");
                        j.appendChild((Node) tab.accept(this));
                    }
                    else{
                        j.setTextContent("(ID,"+key+")");
                    }
                    e.appendChild(j);
                }
        );
        return e;
    }

    @Override
    public Object visit(CallProc ex) {
        Element e = document.createElement("CallProc");
        e.setTextContent("(ID," + ex.getId() + ")");
        if(ex.getList()==null)
        {
            return e;
        }
        ArrayList<Expr> exs=ex.getList().getElist();
        Collections.reverse(exs);
        if(exs==null) {
            return e;
        }
        else{
            for(Expr el:exs){
                e.appendChild((Node) el.accept(this));
            }
            return e;
        }

    }

    @Override
    public Object visit(Elif ex) {
        Element e = document.createElement("Elif");
        Element j= document.createElement("ElifCond");
        j.appendChild((Node) ex.getE().accept(this));
        e.appendChild(j);
        Element k=document.createElement("ElifBody");
        k.appendChild((Node) ex.getS().accept(this));
        e.appendChild(k);
        return e;
    }

    @Override
    public Object visit(ElifList ex) {
        Element e = document.createElement("ElifList");
        if(ex.getElif()!=null)
        {
            ArrayList<Elif> eli=ex.getElif();
            Collections.reverse(eli);
            for (Elif ef:eli) {
                e.appendChild((Node) ef.accept(this));
            }
        }
        return e;
    }

    @Override
    public Object visit(Else ex) {
        Element e = document.createElement("Else");
        if(ex.getSl()!=null) {
            e.appendChild((Node) ex.getSl().accept(this));
        }
        return e;
    }

    @Override
    public Object visit(Expr ex) {
        Element e=document.createElement("Expr");
        return e;
    }

    @Override
    public Object visit(ExprAnd ex) {
        Element e= document.createElement("ExprAnd");
        Element j= (Element) ex.getEleft().accept(this);
        Element g= (Element) ex.getEright().accept(this);
        e.appendChild(j);
        e.appendChild(g);
        return e;
    }

    @Override
    public Object visit(ExprCall ex) {
        Element e= document.createElement("ExprCall");
        Element j= (Element) ex.getC().accept(this);
        e.appendChild(j);
        return e;
    }

    @Override
    public Object visit(ExprDiv ex) {
        Element e= document.createElement("ExprDiv");
        Element j= (Element) ex.getEleft().accept(this);
        Element g= (Element) ex.getEright().accept(this);
        e.appendChild(j);
        e.appendChild(g);
        return e;
    }

    @Override
    public Object visit(ExprEq ex) {
        Element e= document.createElement("ExprEq");
        Element j= (Element) ex.getEleft().accept(this);
        Element g= (Element) ex.getEright().accept(this);
        e.appendChild(j);
        e.appendChild(g);
        return e;
    }

    @Override
    public Object visit(ExprFalse ex) {
        Element e= document.createElement("ExprFalse");
        e.setTextContent("FALSE");
        return e;
    }

    @Override
    public Object visit(ExprFloatConst ex) {
        Element e= document.createElement("ExprFloatConst");
        e.setTextContent(ex.getI().toString());
        return e;
    }

    @Override
    public Object visit(ExprGe ex) {
        Element e= document.createElement("ExprGe");
        Element j= (Element) ex.getEleft().accept(this);
        Element g= (Element) ex.getEright().accept(this);
        e.appendChild(j);
        e.appendChild(g);
        return e;
    }

    @Override
    public Object visit(ExprGt ex) {
        Element e= document.createElement("ExprGt");
        Element j= (Element) ex.getEleft().accept(this);
        Element g= (Element) ex.getEright().accept(this);
        e.appendChild(j);
        e.appendChild(g);
        return e;
    }

    @Override
    public Object visit(ExprId ex) {
        Element e= document.createElement("ExprId");
        e.setTextContent(ex.getId());
        return e;
    }

    @Override
    public Object visit(ExprIntConst ex) {
        Element e= document.createElement("ExprIntConst");
        //e.setAttribute("INT",ex.getI().toString());
        //e.setNodeValue(ex.getI().toString());
        e.setTextContent(ex.getI().toString());
        return e;
    }

    @Override
    public Object visit(ExprLe ex) {
        Element e= document.createElement("ExprLe");
        Element j= (Element) ex.getEleft().accept(this);
        Element g= (Element) ex.getEright().accept(this);
        e.appendChild(j);
        e.appendChild(g);
        return e;
    }

    @Override
    public Object visit(ExprList ex) {
        Element e= document.createElement("ExprList");
        ArrayList<Expr> exs=ex.getElist();
        Collections.reverse(exs);
            for (Expr el : exs) {
                e.appendChild((Node) el.accept(this));
            }
            return e;

    }

    @Override
    public Object visit(ExprLt ex) {
        Element e= document.createElement("ExprLt");
        Element j= (Element) ex.getEleft().accept(this);
        Element g= (Element) ex.getEright().accept(this);
        e.appendChild(j);
        e.appendChild(g);
        return e;
    }

    @Override
    public Object visit(ExprMinus ex) {
        Element e= document.createElement("ExprMinus");
        Element j= (Element) ex.getEleft().accept(this);
        Element g= (Element) ex.getEright().accept(this);
        e.appendChild(j);
        e.appendChild(g);
        return e;
    }

    @Override
    public Object visit(ExprMinusUn ex) {
        Element e= (Element) ex.getEleft().accept(this);
        Element minus= document.createElement("ExprMinusUn");
        minus.appendChild(e);
        return  minus;
    }

    @Override
    public Object visit(ExprNe ex) {
        Element e= document.createElement("ExprNe");
        Element j= (Element) ex.getEleft().accept(this);
        Element g= (Element) ex.getEright().accept(this);
        e.appendChild(j);
        e.appendChild(g);
        return e;
    }

    @Override
    public Object visit(ExprNot ex) {
        Element e= (Element) ex.getEnode().accept(this);
        Element notex= document.createElement("ExprNot");
        notex.appendChild(e);
        return  notex;

    }

    @Override
    public Object visit(ExprNull ex) {
        Element e= document.createElement("ExprNull");
        e.setTextContent("NULL");
        return e;
    }

    @Override
    public Object visit(ExprOr ex) {
        Element e= document.createElement("ExprOr");
        Element j= (Element) ex.getEleft().accept(this);
        Element g= (Element) ex.getEright().accept(this);
        e.appendChild(j);
        e.appendChild(g);
        return e;
    }

    @Override
    public Object visit(ExprPlus ex) {

        Element e= document.createElement("ExprPlus");
        Element j= (Element) ex.getEleft().accept(this);
        Element g= (Element) ex.getEright().accept(this);
        e.appendChild(j);
        e.appendChild(g);
        return e;
    }

    @Override
    public Object visit(ExprStringConst ex) {
        Element e= document.createElement("ExprStringConst");
        e.setTextContent(ex.getS());
        return e;
    }

    @Override
    public Object visit(ExprTimes ex) {
        Element e= document.createElement("ExprTimes");
        Element j= (Element) ex.getEleft().accept(this);
        Element g= (Element) ex.getEright().accept(this);
        e.appendChild(j);
        e.appendChild(g);
        return e;
    }

    @Override
    public Object visit(ExprTrue ex) {
        Element e= document.createElement("ExprTrue");
        e.setTextContent("TRUE");
        return e;
    }

    @Override
    public Object visit(IdList ex) {
        Element e= document.createElement("IdList");
        ArrayList<String>exlist=ex.getElist();
        String tot="";
        for(String s:exlist)
        {
            tot+="(ID,"+s+")";
        }
        e.setTextContent(tot);
        return e;
    }

    @Override
    public Object visit(IdListInit ex) {
        Element e= document.createElement("IdListInit");
        LinkedHashMap<String,Expr> exs=ex.getIdl();
        exs.forEach((key, tab) -> {
                    Element j = document.createElement("IdInit");
                    if(tab!=null) {
                        j.setTextContent("(ID,"+key+")");
                        j.appendChild((Node) tab.accept(this));
                    }
                    else{
                        j.setTextContent("(ID,"+key+")");
                    }
                    e.appendChild(j);
                }
                );
            return e;
    }

    @Override
    public Object visit(IfStat ex) {
        Element e = document.createElement("IfStat");
        if(ex.getSl()!=null) {
            Element j=document.createElement("IfCond");
            j.appendChild((Node) ex.getE().accept(this));
            e.appendChild(j);
        }
        Element r=document.createElement("IfBody");
        r.appendChild((Node) ex.getSl().accept(this));
        r.appendChild((Node) ex.getEl().accept(this));
        r.appendChild((Node) ex.getEls().accept(this));
        e.appendChild(r);
        return e;
    }

    @Override
    public Object visit(ParamDeclList ex) {
        Element e= document.createElement("ParamDeclList");
        ArrayList<ParDecl> exs=ex.getPl();
        for (ParDecl el : exs) {
            e.appendChild((Node) el.accept(this));
        }
        return e;
    }

    @Override
    public Object visit(ParDecl ex) {
        Element e= document.createElement("ParDecl");
        Element j= (Element) ex.getT().accept(this);
        e.appendChild(j);
        j.appendChild((Node) ex.getIl().accept(this));
        return e;
    }

    @Override
    public Object visit(Proc ex) {
        Element e = document.createElement("Proc");
        e.setTextContent("(ID,"+ex.getId()+")");
        if(ex.getP()!=null)
        {
            e.appendChild((Node) ex.getP().accept(this));
        }
        e.appendChild((Node) ex.getR().accept(this));
        Element j=document.createElement("ProcBody");
        j.appendChild((Node) ex.getV().accept(this));
        if(ex.getS()!=null)
        {
            j.appendChild((Node) ex.getS().accept(this));
        }
        j.appendChild((Node) ex.getRe().accept(this));
        e.appendChild(j);
        return e;
    }

    @Override
    public Object visit(ProcList ex) {
        Element e= document.createElement("ProcList");
        ArrayList<Proc> exs=ex.getP();
        Collections.reverse(exs);
        for (Proc el : exs) {
            e.appendChild((Node) el.accept(this));
        }
        return e;
    }

    @Override
    public Object visit(Program ex) {
        Element e= document.createElement("Program");
        if(ex.getV()!=null)
        {
            e.appendChild((Node) ex.getV().accept(this));
        }
        e.appendChild((Node) ex.getP().accept(this));
        return e;
    }

    @Override
    public Object visit(ReadlnStat ex) {
        Element e = document.createElement("ReadlnStat");
        if(ex.getIdlist()==null)
        {
            return e;
        }
        else
        {
            e.appendChild((Node) ex.getIdlist().accept(this));
        }
        return e;
    }

    @Override
    public Object visit(ResultType ex) {
        Element e= document.createElement("ResultType");
        if(ex.getT()!=null) {
            e.appendChild((Node) ex.getT().accept(this));
        }
        else{
            Element j=document.createElement("TypeVoid");
            e.appendChild(j);
        }
        return e;
    }

    @Override
    public Object visit(ResultTypeList ex) {
        Element e= document.createElement("ResultTypeList");
        ArrayList<ResultType> exs=ex.getR();
        Collections.reverse(exs);
        for (ResultType el : exs) {
            e.appendChild((Node) el.accept(this));
        }
        return e;
    }

    @Override
    public Object visit(ReturnExprs ex) {
        Element e= document.createElement("ReturnExprs");
        if(ex.getEl()!=null)
        {
            e.appendChild((Node) ex.getEl().accept(this));
        }
        return e;
    }

    @Override
    public Object visit(Stat ex) {
        Element e= document.createElement("Stat");
        return e;
    }

    @Override
    public Object visit(StatAssign ex) {
        Element e = document.createElement("StatAssign");
        e.appendChild((Node) ex.getAs().accept(this));
        return e;
    }

    @Override
    public Object visit(StatCallProc ex) {
        Element e = document.createElement("StatCallProc");
        e.appendChild((Node) ex.getCp().accept(this));
        return e;
    }

    @Override
    public Object visit(StatIf ex) {
        Element e = document.createElement("StatIf");
        e.appendChild((Node) ex.getS().accept(this));
        return e;
    }

    @Override
    public Object visit(StatList ex) {
        Element e= document.createElement("StatList");
        if(ex.getS()!=null) {
            ArrayList<Stat> exs = ex.getS();
            Collections.reverse(exs);
            for (Stat el : exs) {
                e.appendChild((Node) el.accept(this));
            }
        }
        return e;
    }

    @Override
    public Object visit(StatReadln ex) {
        Element e = document.createElement("StatReadln");
        e.appendChild((Node) ex.getRln().accept(this));
        return e;
    }

    @Override
    public Object visit(StatWhile ex) {
        Element e = document.createElement("StatWhile");
        e.appendChild((Node) ex.getWs().accept(this));
        return e;
    }

    @Override
    public Object visit(StatWrite ex) {
        Element e = document.createElement("StatWrite");
        e.appendChild((Node) ex.getWs().accept(this));
        return e;
    }

    @Override
    public Object visit(Type ex) {
        Element e= document.createElement("Type");
        return e;
    }

    @Override
    public Object visit(TypeBool ex) {
        Element e= document.createElement("TypeBool");
        return e;
    }

    @Override
    public Object visit(TypeFloat ex) {
        Element e= document.createElement("TypeFloat");
        return e;
    }

    @Override
    public Object visit(TypeInt ex) {
        Element e= document.createElement("TypeInt");
        return e;
    }

    @Override
    public Object visit(TypeString ex) {
        Element e= document.createElement("TypeString");
        return e;
    }

    @Override
    public Object visit(VarDecl ex) {
        Element e= document.createElement("VarDecl");
        Element j= (Element) ex.getT().accept(this);
        j.appendChild((Node) ex.getIdl().accept(this));
        e.appendChild(j);
        return e;
    }

    @Override
    public Object visit(VarDeclList ex) {
        Element e= document.createElement("VarDeclList");
        ArrayList<VarDecl> exs=ex.getV();
        Collections.reverse(exs);
        for (VarDecl el : exs) {
            e.appendChild((Node) el.accept(this));
        }
        return e;
    }

    @Override
    public Object visit(WhileStat ex) {
        Element e = document.createElement("WhileStat");
        if(ex.getSl()!=null) {
            e.appendChild((Node) ex.getSl().accept(this));
        }
        e.appendChild((Node) ex.getE().accept(this));
        Element r=document.createElement("WhileBody");
        r.appendChild((Node) ex.getSl2().accept(this));
        e.appendChild(r);
        return e;
    }

    @Override
    public Object visit(WriteStat ex) {
        Element e = document.createElement("WriteStat");
        e.appendChild((Node) ex.getEl().accept(this));
        return e;
    }



    public void getXMLtree(String filePath) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(filePath+".xml"));
        transformer.setOutputProperty(OutputKeys.INDENT,"yes");
        transformer.transform(domSource, streamResult);
        System.out.println("XML file creato con successo");
    }
}
