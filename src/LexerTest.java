import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;

import java.io.FileReader;

public class LexerTest {

    public static void main(String[] args) throws Exception {
        String filePath = args[0];
        ComplexSymbolFactory sf = new ComplexSymbolFactory();

        Lexer lexer = new Lexer(new FileReader(filePath));

        ComplexSymbolFactory.ComplexSymbol token;
        try {
            while (true) {
                token = lexer.next_token();
                if(token.sym == 0)
                    break;

                System.out.println(token);

            }
        } catch (Exception e) {
            System.out.println("File parsing ended!!");
        }

    }

}