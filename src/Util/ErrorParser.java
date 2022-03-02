package Util;

public class ErrorParser extends Error{
    public ErrorParser()
    {
        super("ERRORE DURANTE LA COMPILAZIONE");
    }
    public ErrorParser(String s)
    {
        System.err.println("ERRORE DURANTE LA COMPILAZIONE:"+s);
        return;
    }
}
