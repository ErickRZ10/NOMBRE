package analizador2;
import java_cup.runtime.Symbol;
%%
%class LexerCup
%type java_cup.runtime.Symbol
%cup
%full
%line
%column
%char
%unicode

L=[a-zA-Z_]+
D=[0-9]+
WHITESPACE = [ \t\r\n]+

%{
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    }
    private Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    }
%}
%%


("int")     { return new Symbol(sym.Int, yychar, yyline, yytext()); }
("float")   { return new Symbol(sym.Float, yychar, yyline, yytext()); }
("bool")    { return new Symbol(sym.Bool, yychar, yyline, yytext()); }
("string")  { return new Symbol(sym.String, yychar, yyline, yytext()); }

("true")   { return new Symbol(sym.True, yychar, yyline, yytext()); }
("false")  { return new Symbol(sym.False, yychar, yyline, yytext()); }
("none")   { return new Symbol(sym.None, yychar, yyline, yytext()); }
("main")   { return new Symbol(sym.Main, yychar, yyline, yytext()); }


("from")    { return new Symbol(sym.From, yychar, yyline, yytext()); }
("import")  { return new Symbol(sym.Import, yychar, yyline, yytext()); }

/* ─────────────────────────────
   Palabras clave - Condicionales
   ───────────────────────────── */
( "if" )       { return new Symbol(sym.If, yychar, yyline, yytext()); }
( "elif" )     { return new Symbol(sym.Elif, yychar, yyline, yytext()); }
( "else" )     { return new Symbol(sym.Else, yychar, yyline, yytext()); }

/* ─────────────────────────────
   Palabras clave - Ciclos
   ───────────────────────────── */
( "while" )    { return new Symbol(sym.While, yychar, yyline, yytext()); }
( "for" )      { return new Symbol(sym.For, yychar, yyline, yytext()); }
( "in" )       { return new Symbol(sym.In, yychar, yyline, yytext()); }

/* ─────────────────────────────
   Funciones y retorno
   ───────────────────────────── */
( "def" )      { return new Symbol(sym.Def, yychar, yyline, yytext()); }
( "print" )      { return new Symbol(sym.Print, yychar, yyline, yytext()); }
("input")   { return new Symbol(sym.Input, yychar, yyline, yytext()); }
( "return" )   { return new Symbol(sym.Return, yychar, yyline, yytext()); }
("const")      { return new Symbol(sym.Const, yychar, yyline, yytext()); }


/* ─────────────────────────────
   Operadores Aritméticos
   ───────────────────────────── */
( "**" )       { return new Symbol(sym.Potencia, yychar, yyline, yytext()); }
/* Operador Multiplicacion */
( "*" )        { return new Symbol(sym.Multiplicacion, yychar, yyline, yytext()); }
/* Operador Division */
( "/" )        { return new Symbol(sym.Division, yychar, yyline, yytext()); }
/* Operador Suma */
( "+" )      { return new Symbol(sym.Suma, yychar, yyline, yytext()); }
/* Operador Resta */
( "-" )        { return new Symbol(sym.Resta, yychar, yyline, yytext()); }
/* Operador Modulo */
( "%" )        { return new Symbol(sym.Modulo, yychar, yyline, yytext()); }
( "break" )        { return new Symbol(sym.Break, yychar, yyline, yytext()); }
/* ─────────────────────────────
   Operadores Relacionales
   ───────────────────────────── */
( "==" | "!=" | ">" | "<" | ">=" | "<=" ) { return new Symbol(sym.Op_relacional, yychar, yyline, yytext()); }

/* ─────────────────────────────
   Operadores Asignación
   ───────────────────────────── */
( "=" | "+=" | "-=" | "*=" | "/=" | "%=" ) { return new Symbol(sym.Op_asignacion, yychar, yyline, yytext()); }

/* ─────────────────────────────
   Operadores Lógicos
   ───────────────────────────── */
( "&&" | "||" | "!" | "&" | "\\|" ) { return new Symbol(sym.Op_logico, yychar, yyline, yytext()); }

/* ─────────────────────────────
   Delimitadores
   ───────────────────────────── */
/* Paréntesis */
( "(" )      { return new Symbol(sym.Parentesis_a, yychar, yyline, yytext()); }
( ")" )      { return new Symbol(sym.Parentesis_c, yychar, yyline, yytext()); }

/* Llaves */
( "{" )      { return new Symbol(sym.Llave_a, yychar, yyline, yytext()); }
( "}" )      { return new Symbol(sym.Llave_c, yychar, yyline, yytext()); }

/* Corchetes */
( "[" )      { return new Symbol(sym.Corchete_a, yychar, yyline, yytext()); }
( "]" )      { return new Symbol(sym.Corchete_c, yychar, yyline, yytext()); }

/* Otros símbolos */
( ";" )        { return new Symbol(sym.PuntoComa, yychar, yyline, yytext()); }
( ":" )        { return new Symbol(sym.DosPuntos, yychar, yyline, yytext()); }
"," { return new Symbol(sym.Coma, yychar, yyline, yytext()); }


( "\\." )      { return new Symbol(sym.Punto, yychar, yyline, yytext()); }
( "->" )       { return new Symbol(sym.Flecha, yychar, yyline, yytext()); }

/* ─────────────────────────────
   Literales
   ───────────────────────────── */
/* Número entero y flotante */
( {D}+\.({D}+) )  { return new Symbol(sym.NumeroDecimal, yychar, yyline, yytext()); }
( {D}+ )           { return new Symbol(sym.NumeroEntero, yychar, yyline, yytext()); }

/* Strings y chars */
( "\""([^\"]|\.)*? "\"" )     { return new Symbol(sym.Cadena, yychar, yyline, yytext()); }
( "'"([^\']|\.)? "'" )         { return new Symbol(sym.Char, yychar, yyline, yytext()); }

/* ─────────────────────────────
   Identificadores
   ───────────────────────────── */
( {L}({L}|{D})* ) { return new Symbol(sym.Identificador, yychar, yyline, yytext()); }

/* ─────────────────────────────
   Ignorados: espacios y comentarios
   ───────────────────────────── */

( "//".* )      { /* ignorar */ }
{WHITESPACE} { /* ignorar espacios en blanco */ }
("/*"([^*]|\*+[^*/])*"*"+"/")    { /* Comentario multilínea: ignorar */ }
/* ─────────────────────────────
   Error léxico
   ───────────────────────────── */
.               { return new Symbol(sym.Error, yychar, yyline, yytext()); }