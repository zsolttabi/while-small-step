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
    Identifier Asign e = expression { $value = new Assignment($Identifier.text, $e.value); }
|
    statement SeqSeparator statement
|
    If expression Then statement Else statement
|
    While expression Do statement
;

skip returns [Statement value]:
    Skip { $value  = new Skip(); }
;

expression returns [Expression value]:

    Int { $value = new IntLiteral(Integer.parseInt($Int.text)); }
|
    e1 = expression Plus e2 = expression { $value = new Plus($e1.value, $e2.value); }
|
    e1 = expression Minus e2 = expression { $value = new Minus($e1.value, $e2.value); }
|
    Minus a = expression { $value = new Negate($a.value); }
|
    True { $value = new BoolLiteral(true); }
|
    False { $value = new BoolLiteral(false); }
|
    e1 = expression Equals e2 = expression { $value = new Equals($e1.value, $e2.value); }
|
    e1 = expression LessThen e2 = expression { $value = new LessThen($e1.value, $e2.value); }
|
    Not e = expression { $value = new Not($e.value); }
|
    e1 = expression And e2 = expression  { $value = new LessThen($e1.value, $e2.value); }
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