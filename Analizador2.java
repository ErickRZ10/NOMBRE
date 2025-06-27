/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package analizador2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Analizador2 {

    public static void generarLexer(){
        //String ruta = "C:/Users/PC-24/Documents/LenguajesFormalesyAutomatasU2/Analizador2/src/analizador2/entrada.jflex";
        String ruta = new File("").getAbsolutePath() + "/src/analizador2/entrada.jflex";
        File archivo = new File(ruta);
        JFlex.Main.generate(archivo);
    }
    //public static void checkLexer(){
    //    frmLeer ventana = new frmLeer();
    //    ventana.setVisible(true);
   // }
    public static void generar() throws IOException, Exception{
        //String path0 = "C:/Users/PC-24/Documents/LenguajesFormalesyAutomatasU2/Analizador2/";
        String path0 = new File("").getAbsolutePath() + "/";
        String path = path0 + "src/analizador2/";
        String rutaC = path + "entradaCup.jflex";
        String fileG = "Syntactic.java";
        String[] rutaS = {"-parser","Syntactic", path + "Grammar.cup"};
        File archivo2;
        archivo2 = new File(rutaC);
        JFlex.Main.generate(archivo2);
        System.out.println("Fin lexico");
        java_cup.Main.main(rutaS);
        System.out.println("Ruta---");
        Path rutaSym = Paths.get(path + "Sym.java");
        if(Files.exists(rutaSym)){
            Files.delete(rutaSym);
        }
        Files.move(
            Paths.get(path0 + "sym.java"),
            Paths.get(path + "sym.java")
        );
        Path rutaSin = Paths.get(path + fileG);
        if(Files.exists(rutaSin)){
            Files.delete(rutaSin);
        }
        Files.move(
            Paths.get(path0 + fileG),
            Paths.get(path + fileG)
        );
        
    }
    public static void main(String[] args) throws Exception {

        generarLexer();
        generar();
        //checkLexer();
    }
    
}
