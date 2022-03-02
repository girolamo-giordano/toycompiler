package Util;

import Expressions.*;
import java_cup.runtime.Symbol;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SymbolTable {

    private ArrayList<Record>recordList;
    private String name;

    public ArrayList<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(ArrayList<Record> recordList) {
        this.recordList = recordList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SymbolTable(){
        recordList=new ArrayList<>();
    }

    public SymbolTable(String s){
        recordList=new ArrayList<>();
        this.name=s;
    }

    public void addRecord(Record r)
    {
        recordList.add(r);
    }

    public Record getRecord(String s) //inserisco la stringa per vedere se è presente il record
    {
        for(Record r:recordList)
        {
            if(r.getId()!= null)
            {
                if(r.getId().equals(s))
                {
                    return r;
                }
            }
        }
        return null;
    }


    public Record getRecord(int hashcode) //inserisco l'hashcode' per vedere se è presente il record
    {
        for(Record r:recordList)
        {
            //System.out.println(r.getHash()+":"+hashcode);
            if(r.getHash() == hashcode)
            {
                return r;
            }
        }
        return null;
    }

    public boolean contains(Record r) //controlla se il record passato è presente nella symboltable
    {
        for(Record j:recordList)
        {
            if(j.getId().equals(r.getId()) || r.getVardeclrip().size()!=0)
            {
                return true;
            }
        }
        if(r.getVardeclrip().size()!=0)
        {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                "recordList=" + recordList +
                '}';
    }
}
