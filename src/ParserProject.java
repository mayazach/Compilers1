import java.io.DataInputStream;
import java.io.IOException;
import java.text.ParseException;

public class ParserProject {
	public static void main(String[] args){
	    DataInputStream in = new DataInputStream(System.in);
		Parser parser = new Parser(in);
        try {
            System.out.println(parser.Parse());
        } catch (ParseException e) {
            e.printStackTrace();
        }
	}
}

class Parser{
    char lookahead;
    int character;
    DataInputStream a;

    Parser(DataInputStream a){
        this.a = a;
        this.character = 1;
        try {
            lookahead = (char) a.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int Parse() throws ParseException{
        int exp;
        try {
            exp = Exp();
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(),1);
        }
        if(lookahead != '\n')
            throw new ParseException("Error at column: " + this.character, 1);
        return exp;
    }

    void consume(int symbol) throws ParseException,IOException{
        if(lookahead != symbol) {
            System.out.println(lookahead);
            throw new ParseException("Error at column: " + this.character, 1);
        }
        this.character++;
        lookahead = (char) a.read();
    }

    int Exp() throws ParseException{
        int term;
        try {
            term = Term();
            return Rest(term);
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(),1);
        }
    }

    int Rest(int term) throws ParseException{
        if(lookahead == '+'){
            try {
                this.consume(lookahead);
            } catch (ParseException | IOException e) {
                throw new ParseException(e.getMessage(),1);
            }
            try {
                term += Term();
            } catch (ParseException e) {
                throw new ParseException(e.getMessage(),1);
            }
            return Rest(term);
        }
        else if(lookahead == '-'){
            try {
                this.consume(lookahead);
            } catch (ParseException | IOException e) {
                throw new ParseException(e.getMessage(),1);
            }
            try {
                term -= Term();
            } catch (ParseException e) {
                throw new ParseException(e.getMessage(),1);
            }
            return Rest(term);
        }
        else
            return term;
    }

    int Term() throws ParseException{
        int factor;
        try {
            factor = Factor();
            return Term2(factor);
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(),1);
        }
    }

    int Term2(int factor) throws ParseException{
        if(lookahead == '*'){
            try {
                this.consume(lookahead);
            } catch (ParseException | IOException e) {
                throw new ParseException(e.getMessage(),1);
            }
            try {
                factor *= Factor();
            } catch (ParseException e) {
                throw new ParseException(e.getMessage(),1);
            }
            return Term2(factor);
        }
        else if(lookahead == '/'){
            try {
                this.consume(lookahead);
            } catch (ParseException | IOException e) {
                throw new ParseException(e.getMessage(),1);
            }
            try {
                factor /= Factor();
            } catch (ParseException e) {
                throw new ParseException(e.getMessage(),1);
            }
            return Term2(factor);
        }
        else
            return factor;
    }

    int Factor() throws ParseException{
        if(lookahead == '('){
            try {
                this.consume(lookahead);
            } catch (ParseException | IOException e) {
                throw new ParseException(e.getMessage(),1);
            }
            int result = Exp();
            try {
                this.consume(')');
            } catch (ParseException | IOException e) {
                throw new ParseException(e.getMessage(),1);
            }
            return result;
        }
        else if(Character.isDigit(lookahead)){
                int value = Character.getNumericValue(lookahead);
                try {
                    this.consume(lookahead);
                    return value;
                } catch (ParseException | IOException e) {
                    throw new ParseException("Error at column: " + this.character,1);
                }
        }
        else
            throw new ParseException("Unexpected terminal",1);
        }
}
