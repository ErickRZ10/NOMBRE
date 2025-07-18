/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ejrz0
 */
package analizador2;

import java.util.*;

public class SymbolTable {
    private static Map<String, SymbolEntry> tabla = new LinkedHashMap<>();
    private static List<String> errores = new ArrayList<>();
    private static String currentScope = "global";

    public static void setCurrentScope(String scope) {
        currentScope = scope == null ? "global" : scope;
    }

    private static String makeKey(String nombre, String alcance) {
        return nombre + "@" + alcance;
    }

    private static SymbolEntry findEntry(String nombre) {
        String key = makeKey(nombre, currentScope);
        SymbolEntry e = tabla.get(key);
        if(e == null && !currentScope.equals("global")) {
            e = tabla.get(makeKey(nombre, "global"));
        }
        return e;
    }
    
     private static Double numericValue(String nombre) {
        SymbolEntry e = findEntry(nombre);
        if(e == null) return null;
        try {
            return Double.parseDouble(e.valor);
        } catch(NumberFormatException ex) {
            return null;
        }
    }

    private static class Evaluator {
        private final String[] tokens;
        private int pos = 0;
        Evaluator(String expr) { this.tokens = expr.trim().isEmpty() ? new String[0] : expr.trim().split("\\s+"); }

        double parseExpression() { double val = parseTerm();
            while(pos < tokens.length) {
                String op = tokens[pos];
                if(op.equals("+") || op.equals("-")) { pos++; double t = parseTerm(); val = op.equals("+") ? val + t : val - t; }
                else break;
            }
            return val; }

        double parseTerm() { double val = parseFactor();
            while(pos < tokens.length) {
                String op = tokens[pos];
                if(op.equals("*") || op.equals("/")) { pos++; double f = parseFactor(); val = op.equals("*") ? val * f : val / f; }
                else break;
            }
            return val; }

        double parseFactor() {
            String tok = tokens[pos];
            if(tok.equals("(")) {
                pos++;
                double val = parseExpression();
                if(pos < tokens.length && tokens[pos].equals(")")) pos++;
                return val;
            }
            if(tok.equals("-")) {
                pos++;
                return -parseFactor();
            }
            if(tok.equals("+")) {
                pos++;
                return parseFactor();
            }
            pos++;
            return parseNumberOrVar(tok);
        }

        double parseNumberOrVar(String tok) {
            try { return Double.parseDouble(tok); } catch(NumberFormatException ex) {
                Double v = numericValue(tok);
                if(v != null) return v;
                throw new RuntimeException("Invalid token " + tok);
            }}
    }

        private static Double evalNumericExpr(String expr) {
        try {
            Evaluator ev = new Evaluator(expr);
            return ev.parseExpression();
        } catch(Exception ex) {
            return null;
        }
    }
        
    private static boolean numericExprHasInvalidTokens(String expr) {
        String[] toks = expr.trim().isEmpty() ? new String[0] : expr.trim().split("\\s+");
        for(String tok : toks) {
            if(tok.isEmpty()) continue;
            if(tok.equals("+") || tok.equals("-") || tok.equals("*") || tok.equals("/") ||
               tok.equals("%") || tok.equals("(") || tok.equals(")")) continue;
            if(tok.matches("-?\\d+(\\.\\d+)?")) continue;
            SymbolEntry ent = findEntry(tok);
            if(ent != null) {
                if(!ent.tipoDato.equals("int") && !ent.tipoDato.equals("float")) return true;
            } else {
                if(tok.matches("\".*\"") || tok.matches("'.*'") ||
                   tok.equalsIgnoreCase("true") || tok.equalsIgnoreCase("false") ||
                   tok.equalsIgnoreCase("none")) return true;
                return true;
            }
        }
        return false;
    }


    private static boolean isSimpleConstant(String valor) {
        valor = valor.trim();
        if(valor.matches("-?\\d+(\\.\\d+)?")) return true;
        if(valor.matches("\".*\"")) return true;
        if(valor.matches("'.*'")) return true;
        if(valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("false") || valor.equalsIgnoreCase("none"))
            return true;
        return false;
    }

    public static void clear() {
        tabla.clear();
        errores.clear();
        currentScope = "global";
    }

   public static void addError(String e, int linea) {
        errores.add("L\u00ednea " + linea + ": " + e);
    }

    public static void addError(String e) {
        errores.add(e);
    }

    public static void declare(String nombre, String tipoDato, String valor, String tipoValor, boolean constante, String alcance, int linea) {
        String key = makeKey(nombre, alcance);
        if(tabla.containsKey(key)) {
            addError("Error: doble declaraci\u00f3n de " + nombre, linea);
            return;
        }
        String tipo = constante ? "constante" : "variable";
        SymbolEntry e = new SymbolEntry(tipo, nombre, tipoDato, "", alcance, constante);
        e.operaciones.add("Declaraci\u00f3n");
        boolean hayError = false;
        boolean asignar = true;
        if(valor != null && !valor.isEmpty()) {
            if(tipoValor == null || tipoValor.equals("desconocido") || !tipoDato.equals(tipoValor)) {
                hayError = true;
                asignar = false;
            }
            String op = "Asignaci\u00f3n: " + valor;
            boolean simpleConst = isSimpleConstant(valor);
            if(tipoDato.equals("int") || tipoDato.equals("float")) {
                if(numericExprHasInvalidTokens(valor)) {
                    addError("Error: tipo incompatible en operaci\u00f3n para " + nombre, linea);
                    hayError = true;
                }
                if(!hayError && asignar) {
                    Double res = evalNumericExpr(valor);
                    if(res != null) {
                        e.valor = res % 1 == 0 ? Integer.toString(res.intValue()) : res.toString();
                        if(!simpleConst) op += " = " + e.valor;
                    }
                }
                } else {
                if(!hayError && asignar) e.valor = valor;
            }
            if(!hayError && asignar) e.operaciones.add(op);
        }
        if(!hayError)
            tabla.put(key, e);
    }

    public static void declareArray(String nombre, String tipoDato, int tamano, String valor,
                                    String alcance, int linea) {
        String key = makeKey(nombre, alcance);
        if(tabla.containsKey(key)) {
            addError("Error: doble declaraci\u00f3n de " + nombre, linea);
            return;
        }
        SymbolEntry e = new SymbolEntry("variable", nombre, tipoDato + "[]", "", alcance, false, true, tamano);
        e.operaciones.add("Declaraci\u00f3n de arreglo");
        if(valor != null && !valor.isEmpty()) {
            e.valor = valor;
            e.operaciones.add("Asignaci\u00f3n: " + valor);
        }
        tabla.put(key, e);
    }

    public static void assign(String nombre, String tipoDato, String valor, int linea) {
        SymbolEntry e = findEntry(nombre);
        if(e == null) {
            addError("Error: variable no declarada " + nombre, linea);
            return;
        }
        if(e.esConstante) {
            addError("Error: no se puede modificar la constante " + nombre, linea);
            return;
        }
        boolean hayError = false;
        if(tipoDato != null && !tipoDato.equals("desconocido") && !e.tipoDato.equals(tipoDato)) {
            addError("Error: tipo incompatible para " + nombre + ". Se esperaba " + e.tipoDato + " y se obtuvo " + tipoDato, linea);
            hayError = true;
        }
        String op = "Asignaci\u00f3n: " + valor;
        boolean simpleConst = isSimpleConstant(valor);
         if(e.tipoDato.equals("int") || e.tipoDato.equals("float")) {
            if(numericExprHasInvalidTokens(valor)) {
                addError("Error: tipo incompatible en operaci\u00f3n para " + nombre, linea);
                hayError = true;
            }
            if(!hayError) {
                Double res = evalNumericExpr(valor);
                if(res != null) {
                    e.valor = res % 1 == 0 ? Integer.toString(res.intValue()) : res.toString();
                    if(!simpleConst) op += " = " + e.valor;
                } else {
                    e.valor = valor;
                }
            }
        } else {
            if(!hayError) e.valor = valor;
        }
        if(!hayError) e.operaciones.add(op);
    }

     public static void assignArrayElement(String nombre, String indice, String tipoDato, String valor, int linea) {
        SymbolEntry e = findEntry(nombre);
        if(e == null) {
            addError("Error: variable no declarada " + nombre, linea);
            return;
        }
        if(!e.esArreglo) {
            addError("Error: " + nombre + " no es un arreglo", linea);
            return;
        }
        String elemTipo = e.tipoDato.replace("[]", "");
        if(tipoDato != null && !tipoDato.equals("desconocido") && !elemTipo.equals(tipoDato)) {
            addError("Error: tipo incompatible para " + nombre, linea);
            return;
        }
        Double idxVal = evalNumericExpr(indice);
        if(idxVal == null || idxVal % 1 != 0) {
            addError("Error: \u00edndice no num\u00e9rico para " + nombre, linea);
            return;
        }
        int idx = idxVal.intValue();
        if(idx < 0 || idx >= e.tamano) {
            addError("Error: \u00edndice fuera de rango para " + nombre, linea);
            return;
        }

        java.util.List<String> elems = new java.util.ArrayList<>();
        if(e.valor != null && !e.valor.isEmpty()) {
            String valStr = e.valor.trim();
            if(valStr.startsWith("[") && valStr.endsWith("]")) {
                valStr = valStr.substring(1, valStr.length() - 1);
            }
            if(!valStr.isEmpty()) {
                for(String part : valStr.split(",")) {
                    elems.add(part.trim());
                }
            }
        }
        while(elems.size() < e.tamano) elems.add("");

        boolean hayError = false;
        String op = "Asignaci\u00f3n elemento [" + idx + "]: " + valor;
        if(elemTipo.equals("int") || elemTipo.equals("float")) {
            if(numericExprHasInvalidTokens(valor)) {
                addError("Error: tipo incompatible en operaci\u00f3n para " + nombre, linea);
                hayError = true;
            }
            if(!hayError) {
                Double res = evalNumericExpr(valor);
                if(res != null) {
                    valor = res % 1 == 0 ? Integer.toString(res.intValue()) : res.toString();
                }
            }
        }
        if(!hayError) {
            elems.set(idx, valor);
            e.valor = "[" + String.join(", ", elems) + "]";
            e.operaciones.add(op);
        }
    }

   public static String getType(String nombre) {
        SymbolEntry e = findEntry(nombre);
        return e == null ? null : e.tipoDato;
    }


    public static List<SymbolEntry> getEntries() {
        return new ArrayList<>(tabla.values());
    }

    public static List<String> getErrors() {
        return new ArrayList<>(errores);
    }

    public static String report() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nTabla de S\u00edmbolos:\n");
        sb.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        sb.append("ID\tTipo\tNombre\tTipoDato\tTama\u00f1o\tValor\tAlcance\tSecuencia de Operaciones\n");
        sb.append("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- ----\n");
        int id = 1;
        for(SymbolEntry e : tabla.values()) {
            sb.append(id++).append('\t');
            sb.append(e.tipo).append('\t').append(e.nombre).append('\t').append(e.tipoDato)
              .append('\t');
            sb.append(e.esArreglo ? e.tamano : "").append('\t');
            String val = e.valor;
            if(e.esArreglo) {
                if(val == null || val.isEmpty()) {
                    val = "{}";
                } else {
                    val = val.trim();
                    if(val.startsWith("[") && val.endsWith("]")) {
                        val = val.substring(1, val.length() - 1);
                    }
                    val = "{" + val.replaceAll("\\s+", "") + "}";
                }
            }
            sb.append(val).append('\t').append(e.alcance).append('\t');
            sb.append(String.join(", ", e.operaciones)).append('\n');
            
        }
        if(!errores.isEmpty()) {
            sb.append("\nErrores Sem\u00e1nticos:\n");
            for(String err : errores) sb.append(err).append('\n');
        }  else {sb.append("\nAn\u00e1lisis Sem\u00e1ntico Realizado Correctamente\n");}
        return sb.toString();
    }
}
