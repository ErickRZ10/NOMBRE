/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analizador2;

/**
 *
 * @author ejrz0
 */


public class SymbolEntry {
    public String tipo; // "variable" or "constante"
    public String nombre;
    public String tipoDato;
    public String valor;
    public String alcance;
    public boolean esConstante;
    public java.util.List<String> operaciones;

    public SymbolEntry(String tipo, String nombre, String tipoDato, String valor, String alcance, boolean esConstante) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.valor = valor;
        this.alcance = alcance;
        this.esConstante = esConstante;
        this.operaciones = new java.util.ArrayList<>();
    }
}