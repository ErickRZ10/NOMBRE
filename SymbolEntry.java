package analizador2;

public class SymbolEntry {
    public String tipo; // "variable" or "constante"
    public String nombre;
    public String tipoDato;
    public String valor;
    public String alcance;
    public boolean esConstante;

    public SymbolEntry(String tipo, String nombre, String tipoDato, String valor, String alcance, boolean esConstante) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.valor = valor;
        this.alcance = alcance;
        this.esConstante = esConstante;
    }
}
