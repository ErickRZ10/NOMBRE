/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ejrz0
 */
package analizador2;

import java.io.IOException;
import java.io.StringReader;
import java_cup.runtime.Symbol;
import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {

    private static boolean isType(int s) {
        return s == analizador2.sym.Int || s == analizador2.sym.Float ||
               s == analizador2.sym.Double || s == analizador2.sym.CharType ||
               s == analizador2.sym.Bool || s == analizador2.sym.String;
    }

    // Simple one token buffer to allow pushing back tokens
    private static Symbol bufferedToken = null;

    private static Symbol nextToken(LexerCup lexer) throws IOException {
        if (bufferedToken != null) {
            Symbol t = bufferedToken;
            bufferedToken = null;
            return t;
        }
        return lexer.next_token();
    }

    private static void unreadToken(Symbol tok) {
        bufferedToken = tok;
    }

    private static String tokenToType(Symbol tok) {
        if(tok.sym == sym.NumeroEntero) return "int";
        if(tok.sym == sym.NumeroDecimal) return "float";
        if(tok.sym == sym.Char) return "char";
        if(tok.sym == sym.Cadena) return "string";
        if(tok.sym == sym.True || tok.sym == sym.False) return "bool";
        if(tok.sym == sym.None) return "none";
        if(tok.sym == sym.Identificador) {
            String t = SymbolTable.getType(tok.value.toString());
            if(t == null) {
                // tok.right contains the line number and starts at 0
                // so we add 1 to report the line correctly
                SymbolTable.addError("Error: variable no declarada " + tok.value.toString(), tok.right + 1);
                return "desconocido";
            }
            return t;
        }
        return "desconocido";
    }

    private static Expression readExpression(LexerCup lexer, Symbol first, int... stopSyms) throws IOException {
        java.util.Set<Integer> stops = new java.util.HashSet<>();
        if(stopSyms != null) for(int s : stopSyms) stops.add(s);
        if(stops.isEmpty()) stops.add(sym.PuntoComa);
        List<Symbol> tokens = new ArrayList<>();
        tokens.add(first);
        Symbol t = nextToken(lexer);
        while(!stops.contains(t.sym) && t.sym != sym.EOF) {
            tokens.add(t);
            t = nextToken(lexer);
        }
        unreadToken(t);
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
            // inspect each token to determine if the whole expression is numeric
            boolean numeric = true;
            boolean anyFloat = false;
            for(Symbol s : tokens) {
                switch(s.sym) {
                    case sym.NumeroDecimal:
                        anyFloat = true;
                        break;
                    case sym.NumeroEntero:
                        break;
                    case sym.Identificador: {
                        String tType = SymbolTable.getType(s.value.toString());
                        if(tType == null) {
                            SymbolTable.addError("Error: variable no declarada " + s.value.toString(), s.right + 1);
                            numeric = false;
                        } else if(tType.equals("float") || tType.equals("double")) {
                            anyFloat = true;
                        } else if(!tType.equals("int")) {
                            numeric = false;
                        }
                        break;
                    }
                    case sym.Suma:
                    case sym.Resta:
                    case sym.Multiplicacion:
                    case sym.Division:
                    case sym.Modulo:
                    case sym.Potencia:
                    case sym.Parentesis_a:
                    case sym.Parentesis_c:
                        break; // operators and parentheses are fine
                    default:
                        numeric = false;
                }
                if(!numeric) break;
            }
            if(numeric) {
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
            tok = nextToken(lexer);
            if(tok.sym == sym.EOF) break;

            SymbolTable.setCurrentScope(inMain ? "main" : "global");
            
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
                Symbol typeTok = nextToken(lexer);
                if(!isType(typeTok.sym)) continue; // invalid
                String tipoDato = ((String)typeTok.value).toLowerCase();
                Symbol idTok = nextToken(lexer);
                if(idTok.sym != sym.Identificador) continue;
                String nombre = idTok.value.toString();
                Symbol opTok = nextToken(lexer);
                if(opTok.sym != sym.Op_asignacion) continue;
                Symbol firstExpr = nextToken(lexer);
                Expression expr = readExpression(lexer, firstExpr, sym.PuntoComa);
                // tok.right contains the line number of the current token
                SymbolTable.declare(nombre, tipoDato, expr.valor, expr.tipo, true, inMain ? "main" : "global", tok.right + 1);
                if(!expr.tipo.equals("desconocido") && !expr.tipo.equals(tipoDato)) {
                    SymbolTable.addError("Error: tipo incompatible para " + nombre, tok.right + 1);
                }
            } else if(isType(tok.sym)) {
                String tipoDato = ((String)tok.value).toLowerCase();
                Symbol idTok = nextToken(lexer);
                if(idTok.sym != sym.Identificador) continue;
                String nombre = idTok.value.toString();
                Symbol nextTok = nextToken(lexer);
                if(nextTok.sym == sym.Corchete_a) {
                    Symbol sizeTok = nextToken(lexer);
                    int tam = 0;
                    if(sizeTok.sym == sym.NumeroEntero) {
                        tam = Integer.parseInt(sizeTok.value.toString());
                    }
                    nextToken(lexer); // Corchete_c
                    Symbol after = nextToken(lexer);
                    String arrayVal = "";
                    if(after.sym == sym.Op_asignacion) {
                        nextToken(lexer); // Corchete_a
                        List<String> vals = new ArrayList<>();
                        List<String> tipos = new ArrayList<>();
                        Symbol vtok = nextToken(lexer);
                        if(vtok.sym != sym.Corchete_c) {
                            while(true) {
                                Expression ev = readExpression(lexer, vtok, sym.Coma, sym.Corchete_c);
                                vals.add(ev.valor);
                                tipos.add(ev.tipo);
                                Symbol sep = nextToken(lexer);
                                if(sep.sym == sym.Coma) {
                                    vtok = nextToken(lexer);
                                    continue;
                                }
                                break;
                            }
                        }
                        arrayVal = "[" + String.join(", ", vals) + "]";
                        boolean arrError = false;
                        for(String tval : tipos) {
                            if(!tval.equals("desconocido") && !tval.equals(tipoDato)) {
                                SymbolTable.addError("Error: tipo incompatible para " + nombre, tok.right + 1);
                                arrError = true;
                                break;
                            }
                        }
                        nextToken(lexer); // PuntoComa
                        if(vals.size() > tam) {
                            SymbolTable.addError("Error: demasiados valores para arreglo " + nombre, tok.right + 1);
                            arrError = true;
                        }
                        if(!arrError) {
                            SymbolTable.declareArray(nombre, tipoDato, tam, arrayVal, inMain ? "main" : "global", tok.right + 1);
                        }
                    } else if(after.sym == sym.PuntoComa) {
                        SymbolTable.declareArray(nombre, tipoDato, tam, "", inMain ? "main" : "global", tok.right + 1);
                    }
                } else if(nextTok.sym == sym.Op_asignacion) {
                    Symbol firstExpr = nextToken(lexer);
                    Expression expr = readExpression(lexer, firstExpr, sym.PuntoComa);
                    SymbolTable.declare(nombre, tipoDato, expr.valor, expr.tipo, false, inMain ? "main" : "global", tok.right + 1);
                    if(!expr.tipo.equals("desconocido") && !expr.tipo.equals(tipoDato)) {
                        SymbolTable.addError("Error: tipo incompatible para " + nombre, tok.right + 1);
                    }
                } else if(nextTok.sym == sym.PuntoComa) {
                    SymbolTable.declare(nombre, tipoDato, "", null, false, inMain ? "main" : "global", tok.right + 1);
                } else {
                    // unexpected
                }
            } else if(tok.sym == sym.Identificador) {
                String nombre = tok.value.toString();
                Symbol nextTok = nextToken(lexer);
                if(nextTok.sym == sym.Corchete_a) {
                    Symbol idxFirst = nextToken(lexer);
                    Expression idxExpr = readExpression(lexer, idxFirst, sym.Corchete_c);
                    nextToken(lexer); // Corchete_c
                    Symbol opTok = nextToken(lexer);
                    if(opTok.sym != sym.Op_asignacion) continue;
                    Symbol firstExpr = nextToken(lexer);
                    Expression expr = readExpression(lexer, firstExpr, sym.PuntoComa);
                    if(!idxExpr.tipo.equals("int")) {
                        SymbolTable.addError("Error: \u00edndice no num\u00e9rico para " + nombre, tok.right + 1);
                    }
                    SymbolTable.assignArrayElement(nombre, idxExpr.valor, expr.tipo, expr.valor, tok.right + 1);
                } else if(nextTok.sym == sym.Op_asignacion) {
                    Symbol firstExpr = nextToken(lexer);
                    Expression expr = readExpression(lexer, firstExpr, sym.PuntoComa);
                    SymbolTable.assign(nombre, expr.tipo, expr.valor, tok.right + 1);
                }
            }
        }
        return SymbolTable.report();
    }
}