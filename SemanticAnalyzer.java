package analizador2;

import java.io.IOException;
import java.io.StringReader;
import java_cup.runtime.Symbol;
import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {

    private static boolean isType(int s) {
        return s == analizador2.sym.Int || s == analizador2.sym.Float ||
               s == analizador2.sym.Bool || s == analizador2.sym.String;
    }

    private static String tokenToType(Symbol tok) {
        if(tok.sym == sym.NumeroEntero) return "int";
        if(tok.sym == sym.NumeroDecimal) return "float";
        if(tok.sym == sym.Cadena || tok.sym == sym.Char) return "string";
        if(tok.sym == sym.True || tok.sym == sym.False) return "bool";
        if(tok.sym == sym.None) return "none";
        if(tok.sym == sym.Identificador) {
            String t = SymbolTable.getType(tok.value.toString());
            if(t == null) {
                SymbolTable.addError("Error: variable no declarada " + tok.value.toString());
                return "desconocido";
            }
            return t;
        }
        return "desconocido";
    }

    private static Expression readExpression(LexerCup lexer, Symbol first) throws IOException {
        List<Symbol> tokens = new ArrayList<>();
        tokens.add(first);
        Symbol t = lexer.next_token();
        while(t.sym != sym.PuntoComa && t.sym != sym.EOF) {
            tokens.add(t);
            t = lexer.next_token();
        }
        // build string representation
        StringBuilder sb = new StringBuilder();
        for(Symbol s : tokens) {
            sb.append(s.value.toString()).append(' ');
        }
        String val = sb.toString().trim();
        String tipo;
        if(tokens.size() == 1) {
            tipo = tokenToType(tokens.get(0));
        } else {
            // simple heuristic: if all tokens numeric return numeric, else desconocido
            boolean anyFloat = false;
            boolean allNumeric = true;
            for(Symbol s : tokens) {
                if(s.sym == sym.NumeroDecimal) anyFloat = true;
                else if(s.sym == sym.NumeroEntero) { /* ok */ }
                else { allNumeric = false; break; }
            }
            if(allNumeric) {
                tipo = anyFloat ? "float" : "int";
            } else {
                tipo = "desconocido";
            }
        }
        return new Expression(tipo, val);
    }

    private static class Expression {
        String tipo;
        String valor;
        Expression(String t, String v) { tipo = t; valor = v; }
    }

    public static String analyze(String code) throws IOException {
        SymbolTable.clear();
        LexerCup lexer = new LexerCup(new StringReader(code));
        Symbol tok;
        boolean inMain = false;
        int braceDepth = 0;
        while(true) {
            tok = lexer.next_token();
            if(tok.sym == sym.EOF) break;

            if(tok.sym == sym.Main) {
                inMain = true;
                braceDepth = 0;
                continue;
            }
            if(inMain && tok.sym == sym.Llave_a) {
                braceDepth++;
            } else if(inMain && tok.sym == sym.Llave_c) {
                braceDepth--;
                if(braceDepth <= 0) {
                    inMain = false;
                }
            }
            if(tok.sym == sym.Const) {
                Symbol typeTok = lexer.next_token();
                if(!isType(typeTok.sym)) continue; // invalid
                String tipoDato = ((String)typeTok.value).toLowerCase();
                Symbol idTok = lexer.next_token();
                if(idTok.sym != sym.Identificador) continue;
                String nombre = idTok.value.toString();
                Symbol opTok = lexer.next_token();
                if(opTok.sym != sym.Op_asignacion) continue;
                Symbol firstExpr = lexer.next_token();
                Expression expr = readExpression(lexer, firstExpr);
                SymbolTable.declare(nombre, tipoDato, expr.valor, true, inMain ? "main" : "global");
                if(!expr.tipo.equals("desconocido") && !expr.tipo.equals(tipoDato)) {
                    SymbolTable.addError("Error: tipo incompatible para " + nombre);
                }
            } else if(isType(tok.sym)) {
                String tipoDato = ((String)tok.value).toLowerCase();
                Symbol idTok = lexer.next_token();
                if(idTok.sym != sym.Identificador) continue;
                String nombre = idTok.value.toString();
                Symbol nextTok = lexer.next_token();
                if(nextTok.sym == sym.Op_asignacion) {
                    Symbol firstExpr = lexer.next_token();
                    Expression expr = readExpression(lexer, firstExpr);
                    SymbolTable.declare(nombre, tipoDato, expr.valor, false, inMain ? "main" : "global");
                    if(!expr.tipo.equals("desconocido") && !expr.tipo.equals(tipoDato)) {
                        SymbolTable.addError("Error: tipo incompatible para " + nombre);
                    }
                } else if(nextTok.sym == sym.PuntoComa) {
                    SymbolTable.declare(nombre, tipoDato, "", false, inMain ? "main" : "global");
                } else {
                    // token unexpected, skip until semicolon
                }
            } else if(tok.sym == sym.Identificador) {
                String nombre = tok.value.toString();
                Symbol opTok = lexer.next_token();
                if(opTok.sym != sym.Op_asignacion) continue;
                Symbol firstExpr = lexer.next_token();
                Expression expr = readExpression(lexer, firstExpr);
                SymbolTable.assign(nombre, expr.tipo, expr.valor);
            }
        }
        return SymbolTable.report();
    }
}
