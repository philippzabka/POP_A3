package ast;

import java.util.ArrayList;
import java.util.List;

public class Declaration extends Expression {

    public String type;
    public List<String> variables;

    public Declaration(String type, ArrayList<String> variables){
        this.type = type;
        this.variables = variables;
    }
}
