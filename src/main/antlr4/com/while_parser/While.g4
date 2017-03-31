grammar While;

@parser::header {
    import ast.AST;
    import ast.statement.*;
    import ast.expression.*;
    import ast.expression.values.*;
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
    Identifier Asign a = aExp { $value = new Assingment($Identifier.text, $a.value); }
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
    Int { $value = new IntLiteral(Integer.parseInt($Int.text)); }
    |
    a1 = aExp Plus a2 = aExp { $value = new Plus($a1.value, $a2.value); }
    |
    a1 = aExp Minus a2 = aExp { $value = new Minus($a1.value, $a2.value); }
    |
    Minus a = aExp { $value = new Negate($a.value); }
;

bExp returns [Expression value]:
    True { $value = new BoolLiteral(true); }
    |
    False { $value = new BoolLiteral(false); }
    |
    a1 = aExp Equals a2 = aExp { $value = new Equals($a1.value, $a2.value); }
    |
    a1 = aExp LessThen a2 = aExp { $value = new LessThen($a1.value, $a2.value); }
    |
    Not b = bExp { $value = new Not($b.value); }
    |
    b1 = bExp And b2 = bExp  { $value = new LessThen($b1.value, $b2.value); }
;

If: 'if' ;

Then: 'then' ;

Else: 'else' ;

While: 'while' ;

Do: 'do' ;

Asign: ':=' ;

SeqSeparator: ';' ;

Skip: 'SKIP' ;

Identifier: ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')* ;

Int: '-'?('1'..'9') ('0'..'9')* ;

True: 'tt' ;

False: 'ff' ;

Plus: '+' ;

Minus: '-';

Equals: '=' ;

LessThen: '<' ;

Not: '!' ;

And: '&&' ;

WS: [ \t|\r\n] -> skip;