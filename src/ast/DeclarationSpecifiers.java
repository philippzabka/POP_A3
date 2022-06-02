package ast;

import java.util.ArrayList;

public class DeclarationSpecifiers extends Expression{

    public ArrayList<String> specifiers;

    public DeclarationSpecifiers(ArrayList<String> specifiers){
        this.specifiers = specifiers;
    }
}
