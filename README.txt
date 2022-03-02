Lo sviluppo del progetto è stato eseguito dividendo le varie fasi del compilatori in diverse classi.
Package src/Visitor:
- ScopeVisitor:Questa classe si occupa della gestione dell'analisi sintattica e quindi del controllo dei costrutti e del lancio di eventuali errori
               sintattici. Questa implementa l'interfaccia VisitorInterface contenente un metodo visit per ogni costrutto del nostro
               linguaggio. Oltre i metodi visit questa classe implementa due metodi: lookUP(String s) che data una stringa s e una symbol table cerca in
               quest'ultima il record passato come parametro; checkDoubleParam(ParamDeclList ex) ci sono dichiarazioni con lo stesso nome all'interno
               della lista dei parametri.
               Il program e ogni proc hanno la propria symbol table.
               Lo scoping è gestito tramite uno stack.

- TypeVisitor: Raccoglie informazioni di tipo ,e le memorizza nella tabella dei simboli per poterle usare nella fase successiva. In questa classe avviene
               il type checking ovvero la verifica dell'adeguatezza degli operandi nelle operazioni seguendo le tabelle sotto esposte.

- CodeVisitor: In questa classe si è fatta la trasformazione dal linguaggio TOY al C. Per la realizzazione si sono effettuate alcune scelte progettuali
               come:
               Il mancato uso del "null".
               Le assegnazioni multiple vengono divise in assegnazioni singole.
               Le procedure che restituiscono uno o più valori di ritorno vengono salvati in variabili di servizio in quanto è possibile nel C passare puntatori di variabili.
               Abbiamo deciso che i valori booleani saranno stampati come 1 e 0.
               E' possibile assegnare un intero ad una variabile dichiarata “float”.
               Nella “write” è possibile invocare funzioni che restituiscono uno o più valori di ritorno.
               Le funzioni possono restituire funzioni che hanno più valori di ritorno.
               Alle funzioni possono essere passati, come parametri, funzioni che restituiscono più valori effettuando il salvataggio dei valori di ritorno nelle variabili associzte alle chiamate a procedura prima che venga chiamata la procedura.
               Le stringhe vengono convertite in array di caratteri, è possibile effettuare un assegnazione di stringa attraverso la strcpy.




optype1
op1   operand   result
-     integer   integer
-     float     float
not   bool      bool

optype2
OP          1OPERAND       2OPERAND     RESULT
+ - * /     int            int          int
+ - * /     int            float        float
+ - * /     float          int          float
+ - * /     float          float        float
and or      bool           bool         bool
<==>        bool           bool         bool
<==>        int            int          bool
<==>        int            float        bool
<==>        float          int          bool
<==>        float          float        bool

TABELLA COMPATIBILITA' TIPI
int --> int
float ---> float||int
bool --->bool
string --->string

TABELLE DI INFERENZE

Γ => null:null
Γ => true:bool
Γ => false:bool
Γ => int:int
Γ=> float:float
Γ=>string:string

x:τ ∈ Γ
__________
Γ => x:τ

(f(τ1,τ2...τn)-> τ1',τ2'...τn')∈ Γ  Γ=>e1:τ1  Γ=>e2:τ2....
______________________________________________________________-
Γ=>f(τ1,τ2...τn): τ1',τ2'...τn'

(f(τ1,τ2...τn)->void)∈Γ  Γ=>e1:τ1  Γ=>e2:τ2 .......
____________________________________________________
Γ=>f(e1,e2...en)

(main()=> void) ∈ Γ
_____________________
Γ => main()

Γ => e:τ1 optype1(Op1,τ1)=τ
___________________________
Γ => op1 e: τ


Γ => e1:τ1 Γ => e2:τ2 optype2(Op2,τ1,τ2)=τ
___________________________________________
Γ => (e1 op2 e) :τ


Γ => e:bool  Γ =>stmt
_________________________
Γ =>while e do stmt od


Γ => s:bool  Γ =>e  Γ =>s1
___________________________________
Γ =>while s return e do s1 od


Γ => e1:bool  Γ =>s1  Γ => e2:bool  Γ =>s2 Γ =>s3
________________________________________________
Γ => if e1 then sl elif e2 then s2.... else s3 FI


x:τ ∈ Γ  Γ=>e:τ' t'<=t
________________________
Γ =>x=e


Γ => e1:τ1  Γ => e1:τ1..........
________________________________
Γ=>write(e1,e2....)


(e1:τ1)∈Γ  (e1:τ1)∈Γ .....
________________________________
Γ=>readln(e1,e2....)




