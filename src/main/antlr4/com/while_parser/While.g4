grammar While;

@parser::header {
    import ast.AST;
    import ast.statement.*;
    import ast.expression.*;
}

@parser::members {

    public static AST parseAst(String input) {
        WhileParser parser = new WhileParser(new CommonTokenStream(new WhileLexer(new ANTLRInputStream(input))));
        return new AST(parser.start().value);
    }

}

start returns [Statement value]:
    s = statement { $value = $s.value; }
;

statement returns [Statement value]:
    s = skip { $value  = $s.value; }
    |
    Var Asign aExp {}
    |
    statement SeqSeparator statement
    |
    If expression Then statement Else statement
    |
    While bExp Do statement
;

skip returns [Statement value]:
    Skip { $value  = new Skip(); }
;

expression returns [Expression value]:
    aExp
    |
    bExp
;

aExp returns [Expression value]:
    Integer
    |
    Var
    |
    aExp Plus aExp
    |
    aExp Minus aExp
    |
    Minus aExp
;

bExp returns [Expression value]:
    True { $value = new BoolLiteral(true); }
    |
    False { $value = new BoolLiteral(false); }
    |
    a1 = aExp Equals a2 = aExp { $value = new Equality($a1.value, $a2.value); }
    |
    aExp LessThen aExp
    |
    Not bExp
    |
    bExp And bExp
;

If: 'if' ;

Then: 'then' ;

Else: 'else' ;

While: 'while' ;

Do: 'do' ;

Asign: ':=' ;

SeqSeparator: ';' ;

Skip: 'SKIP' ;

Var: ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')* ;

Integer: '-'?('1'..'9') ('0'..'9')* ;

True: 'tt' ;

False: 'ff' ;

Plus: '+' ;

Minus: '-';

Equals: '=' ;

LessThen: '<' ;

Not: '!' ;

And: '&&' ;

WS: [ \t|\r\n] -> skip;