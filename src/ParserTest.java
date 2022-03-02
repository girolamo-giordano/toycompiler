// Circuit.java


import Expressions.*;
import Visitor.*;
import java_cup.runtime.ComplexSymbolFactory;
import org.w3c.dom.Element;

import java.io.*;

public class ParserTest {
    public static void main(String[] args) throws Exception {
        String filePath = args[0];
        ComplexSymbolFactory sf = new ComplexSymbolFactory();
        Parser parser  = new Parser((new Lexer(new FileReader(filePath))),sf);
        Program s=(Program) parser.parse().value;
        //Program s=(Program) parser.debug_parse().value;
         // l'uso di p.debug_parse() al posto di p.parse() produce tutte le azioni del parser durante il riconoscimento
        XMLVisitor v= new XMLVisitor();
        v.appendRoot((Element) v.visit(s));
        v.getXMLtree(filePath);
        ScopeVisitor se=new ScopeVisitor();
        se.visit(s);
        TypeVisitor tv= new TypeVisitor();
        tv.visit(s);
        CodeVisitor cv= new CodeVisitor();
        String command= (String) cv.visit(s);
        BufferedWriter writer = new BufferedWriter(new FileWriter("out.c"));
        writer.write(command);
        writer.close();

    }
}
// End of file
