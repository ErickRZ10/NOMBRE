package analizador2;

import java.util.*;

public class SymbolTable {
    private static Map<String, SymbolEntry> tabla = new LinkedHashMap<>();
    private static List<String> errores = new ArrayList<>();

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
        tabla.put(nombre, new SymbolEntry(tipo, nombre, tipoDato, valor, alcance, constante));
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
        e.valor = valor;
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
        sb.append("Tipo\tNombre\tTipoDato\tValor\tAlcance\n");
        for(SymbolEntry e : tabla.values()) {
            sb.append(e.tipo).append('\t').append(e.nombre).append('\t').append(e.tipoDato)
              .append('\t').append(e.valor).append('\t').append(e.alcance).append('\n');
        }
        if(!errores.isEmpty()) {
            sb.append("\nErrores Sem\u00e1nticos:\n");
            for(String err : errores) sb.append(err).append('\n');
        }
        return sb.toString();
    }
}
