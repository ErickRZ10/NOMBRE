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
    
     private static Double numericValue(String nombre) {
        SymbolEntry e = tabla.get(nombre);
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

        double parseFactor() { String tok = tokens[pos];
            if(tok.equals("(")) { pos++; double val = parseExpression(); if(pos < tokens.length && tokens[pos].equals(")")) pos++; return val; }
            pos++; return parseNumberOrVar(tok); }

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
    }

    public static void addError(String e) {
        errores.add(e);
    }

    public static void declare(String nombre, String tipoDato, String valor, boolean constante, String alcance) {
        if(tabla.containsKey(nombre)) {
            errores.add("Error: doble declaraci\u00f3n de " + nombre);
            return;
        }
        String tipo = constante ? "constante" : "variable";
        SymbolEntry e = new SymbolEntry(tipo, nombre, tipoDato, valor, alcance, constante);
        e.operaciones.add("Declaraci\u00f3n");
        if(valor != null && !valor.isEmpty()) {
            String op = "Asignaci\u00f3n: " + valor;
            boolean simpleConst = isSimpleConstant(valor);
            if(tipoDato.equals("int") || tipoDato.equals("float")) {
                Double res = evalNumericExpr(valor);
                if(res != null) {
                    e.valor = res % 1 == 0 ? Integer.toString(res.intValue()) : res.toString();
                    if(!simpleConst) op += " = " + e.valor;
                }
            }
            e.operaciones.add(op);
        }
        tabla.put(nombre, e);
    }

    public static void assign(String nombre, String tipoDato, String valor) {
        SymbolEntry e = tabla.get(nombre);
        if(e == null) {
            errores.add("Error: variable no declarada " + nombre);
            return;
        }
        if(e.esConstante) {
            errores.add("Error: no se puede modificar la constante " + nombre);
            return;
        }
        if(tipoDato != null && !tipoDato.equals("desconocido") && !e.tipoDato.equals(tipoDato)) {
            errores.add("Error: tipo incompatible para " + nombre + ". Se esperaba " + e.tipoDato + " y se obtuvo " + tipoDato);
        }
        String op = "Asignaci\u00f3n: " + valor;
        boolean simpleConst = isSimpleConstant(valor);
        if(e.tipoDato.equals("int") || e.tipoDato.equals("float")) {
            Double res = evalNumericExpr(valor);
            if(res != null) {
                e.valor = res % 1 == 0 ? Integer.toString(res.intValue()) : res.toString();
                if(!simpleConst) op += " = " + e.valor;
            } else {
                e.valor = valor;
            }
        } else {
            e.valor = valor;
        }
        e.operaciones.add(op);
    }

    public static String getType(String nombre) {
        SymbolEntry e = tabla.get(nombre);
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
        sb.append("Tabla de S\u00edmbolos:\n");
        sb.append("Tipo\tNombre\tTipoDato\tValoar\tAlcance\tSecuencia de Operaciones\n");
        for(SymbolEntry e : tabla.values()) {
            sb.append(e.tipo).append('\t').append(e.nombre).append('\t').append(e.tipoDato)
              .append('\t').append(e.valor).append('\t').append(e.alcance).append('\t');
            sb.append(String.join(", ", e.operaciones)).append('\n');
        }
        if(!errores.isEmpty()) {
            sb.append("\nErrores Sem\u00e1nticos:\n");
            for(String err : errores) sb.append(err).append('\n');
        }
        return sb.toString();
    }
}
