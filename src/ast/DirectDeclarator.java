package ast;

public class DirectDeclarator extends Expression{

    public String name;
    public String bracketLeft;
    public String bracketRight;

    public DirectDeclarator(String name, String bracketLeft, String bracketRight){
        this.name = name;
        this.bracketLeft = bracketLeft;
        this.bracketRight = bracketRight;
    }
}
