// Circuit.flex
//
// CS2A Language Processing
//
// Description of lexer for circuit description language.
//
// Ian Stark
 //This is how we pass tokens to the parser
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.*;
import java_cup.runtime.ComplexSymbolFactory.Location;

%%
// Declarations for JFlex
%unicode
//classe lexer implementa sym
%implements sym
%public // fa si che la classe generata sia public
//genera classe lexer
%class Lexer
%char  //tiene traccia del numero dei caratteri tramite la variabile yychar dall'inizio dell'input al token corrente
%line // tiene traccia della linea, usato per gli errori tramite yyline
%column

//Fa sì che il metodo scansionato venga dichiarato come valori di restituzione del tipo specificato.
//Le azioni specificate possono quindi restituire complexSymbol come token.
%type ComplexSymbol
%cup // Declare that we expect to use Java CUP

%{  //codice che va inserito all'interno del lexer --> user code
    StringBuffer string = new StringBuffer();

    //viene passato il file da dove verranno letti i lessemi ed un symbolFactory per gestire le informazioni sui token
    /*public Lexer(java.io.Reader in, ComplexSymbolFactory sf){
        this(in);
        symbolFactory = sf;
    }
    ComplexSymbolFactory symbolFactory;
    */

    //si tiene traccia della linea,colonna e info sul token
    private ComplexSymbol symbol(int sym)
    {
        return new ComplexSymbol(null,sym,
        new Location(yyline+1,yycolumn+1,(int)yychar),
        new Location(yyline+1,yycolumn+yylength(),(int)yychar+yylength()));
    }

  private ComplexSymbol symbol(int sym, String name) {
      return new ComplexSymbol(name, sym, new Location(yyline+1,yycolumn+1,(int)yychar), new Location(yyline+1,yycolumn+yylength(),(int)yychar+yylength()));
  }

  private ComplexSymbol symbol(int sym, String name, Object val) {
      Location left = new Location(yyline+1,yycolumn+1,(int)yychar);
      Location right= new Location(yyline+1,yycolumn+yylength(), (int)yychar+yylength());
      return new ComplexSymbol(name, sym, left, right,val);
  }
  private ComplexSymbol symbol(int sym, String name, Object val,int buflength) {
      Location left = new Location(yyline+1,yycolumn+yylength()-buflength,(int)yychar+yylength()-buflength);
      Location right= new Location(yyline+1,yycolumn+yylength(),(int) yychar+yylength());
      return new ComplexSymbol(name, sym, left, right,val);
  }
  /*private void error(String message) {
    System.out.println("Error at line "+(yyline+1)+", column "+(yycolumn+1)+" : "+message);
  }*/
%}



comment = "/*" [^*] "*/" | "/*" "*"+ "/"
whitespace = [ \r\n\t\f]
digit = [0-9]
nonzerodigit = [1-9]
letterOrUnderscore=[a-zA-Z_]
number = (-)?({nonzerodigit}{digit}*|0)
identifier={letterOrUnderscore}({digit} | {letterOrUnderscore})*
stringConst=\"[^]*\"
floatingNumber=  (-)?(0 | [1-9][0-9]*)\.[0-9]+

//dichiarazioni degli stati
%state STRING
%state COMMENT


%% //divisore

//iniziano le regole lessicali
<YYINITIAL>{
//punteggiatura
";" {return symbol(sym.SEMI,"SEMI");}
"," {return symbol(sym.COMMA,"COMMA");}
"(" {return symbol(sym.LPAR,"LPAR");}
")" {return symbol(sym.RPAR,"RPAR");}
":" {return symbol(sym.COLON,"COLON");}

/*keywords*/
"int" {return symbol(sym.INT,"INT");}
"string" {return symbol(sym.STRING,"STRING");}
"float" {return symbol(sym.FLOAT,"FLOAT");}
"bool" {return symbol(sym.BOOL,"BOOL");}
"void" {return symbol(sym.VOID,"VOID");}
"->" { return symbol(sym.RETURN,"RETURN");}

//condizioni
"if" {return symbol(sym.IF,"IF");}
"then" {return symbol(sym.THEN,"THEN");}
"elif" {return symbol(sym.ELIF,"ELIF");}
"fi" {return symbol(sym.FI,"FI");}
"else" {return symbol(sym.ELSE,"ELSE");}

//CICLO
"while" {return symbol(sym.WHILE,"WHILE");}
"do" {return symbol(sym.DO,"DO");}
"od" {return symbol(sym.OD,"OD");}

//specifiche linguaggio
"proc" {return symbol(sym.PROC,"PROC");}
"corp" {return symbol(sym.CORP,"CORP");}

//read e write
"readln" {return symbol(sym.READ,"READ");}
"write" {return symbol(sym.WRITE,"WRITE");}

//operatori
":=" {return symbol(sym.ASSIGN,"ASSIGN");}

//const

{floatingNumber} {return symbol(sym.FLOAT_CONST,"FLOAT_CONST",Float.parseFloat(yytext()));}

/*operator*/
//la classe symbol è presa da sopra
"+" {return symbol(sym.PLUS,"PLUS");}
"-" {return symbol(sym.MINUS,"MINUS");}
"*" {return symbol(sym.TIMES,"TIMES");}
"/" {return symbol(sym.DIV,"DIV");}
"=" {return symbol(sym.EQ,"EQ");}
"<>" {return symbol(sym.NE,"NE");}
"<" {return symbol(sym.LT,"LT");}
"<=" {return symbol(sym.LE,"LE");}
">" {return symbol(sym.GT,"GT");}
">=" {return symbol(sym.GE,"GE");}
"&&" {return symbol(sym.AND,"AND");}
"||" {return symbol(sym.OR,"OR");}
"!" {return symbol(sym.NOT,"NOT");}
"null" {return symbol(sym.NULL,"NULL");}
"true" {return symbol(sym.TRUE,"TRUE");}
"false" {return symbol(sym.FALSE,"FALSE");}

//id
{identifier} {return symbol(sym.ID,"ID",yytext());} //yytext() legge la stringa in input e aggiunge il contenuto della stringa letterale analizzata finora.

//integer
{number} {return symbol(sym.INT_CONST,"INTEGER",Integer.parseInt(yytext()));}


{whitespace} { /* ignore */ }

\" {string.setLength(0); yybegin(STRING);} // se legge lo " capisce che inizia una stringa e va allo stato STRING

"/*" {yybegin(COMMENT);} // se legge lo * capisce che inizia un commento e va allo stato COMMENT


}

<STRING>{
 \" {yybegin(YYINITIAL);return symbol(sym.STRING_CONST,"STRING_CONST",string.toString());}
 [^\n\r\"\\]+ { string.append( yytext() ); }
 \\t { string.append('\t'); } //append inserisce nella stringa i caratteri riconosciuti
 \\n { string.append('\n'); }
 \\r { string.append('\r'); }
 \\\" { string.append('\"'); }
 \\ { string.append('\\'); }
 \n {}
 \r {}
 \t {}
 <<EOF>>  { throw new Error("Stringa costante non completata\n"); }
}

<COMMENT> {
"*/" {yybegin(YYINITIAL);}
[^*] {}
<<EOF>> {throw new Error("Commento non chiuso\n");}
}

<<EOF>> { return symbol(sym.EOF); }

[^]  { throw new Error("\n\nCarattere non riconosciuto < "+ yytext()+" >\n"); }

// End of file