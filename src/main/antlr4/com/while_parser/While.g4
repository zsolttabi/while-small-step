grammar While;

@parser::header {
    import ast.AST;
    import ast.statement.*;
    import ast.statement.interfaces.*;
    import ast.expression.*;
    import ast.expression.operations.*;
    import ast.expression.interfaces.*;
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

    id = expression Assign e = expression { $value = Assignment.of($id.value, $e.value); }
|
    s1 = statement SeqSeparator s2 = statement { $value = Sequence.of($s1.value, $s2.value); }
|
    IfT e = expression Then s1 = statement Else s2 = statement { $value = If.of($e.value, $s1.value, $s2.value); }
|
    WhileT e = expression Do s = statement { $value = While.of($e.value, $s.value); }
|
    Skip { $value  = new Skip(); }
;

expression returns [Expression value]:

    Identifier { $value = new Identifier($Identifier.text); }
|
    Int { $value = new IntValue(Integer.parseInt($Int.text)); }
|
    e1 = expression Plus e2 = expression { $value = BinOp.arithOp($Plus.text, $e1.value, $e2.value, Integer::sum); }
|
    e1 = expression Minus e2 = expression { $value = BinOp.arithOp($Minus.text, $e1.value, $e2.value, (i1, i2) -> i1 - i2); }
|
    Minus a = expression { $value = UnOp.intOp($Minus.text, $a.value,  i -> -1 * i); }
|
    True { $value = new BoolValue(true); }
|
    False { $value = new BoolValue(false); }
|
    e1 = expression Equals e2 = expression { $value = BinOp.intRelOp($Equals.text, $e1.value, $e2.value, Object::equals); }
|
    e1 = expression LessThen e2 = expression { $value = BinOp.intRelOp($LessThen.text, $e1.value, $e2.value, (i1,i2) -> i1 < i2); }
|
    Not e = expression { $value = UnOp.boolOp($Not.text, $e.value, b -> !b); }
|
    e1 = expression And e2 = expression  { $value = BinOp.boolOp($And.text, $e1.value, $e2.value, Boolean::logicalAnd); }
;

IfT: 'if' ;

Then: 'then' ;

Else: 'else' ;

WhileT: 'while' ;

Do: 'do' ;

Assign: ':=' ;

SeqSeparator: ';' ;

Skip: 'SKIP' ;

True: 'tt' ;

False: 'ff' ;

Plus: '+' ;

Minus: '-';

Equals: '=' ;

LessThen: '<' ;

Not: '!' ;

And: '&&' ;

Int: '0' | ('-'? ('1'..'9') ('0'..'9')*) ;

Identifier: ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')* ;

WS: [ \t|\r\n] -> channel(HIDDEN);