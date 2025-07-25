/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizador2;

import static analizador2.Tokens.*;


import java.awt.Color;
import java.io.File;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import javax.swing.JFileChooser;
import java.io.BufferedReader;
import java.io.File;       
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import java.io.StringReader;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java_cup.runtime.Symbol;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class FrmPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form FrmPrincipal
     */
    
    /** Archivo actualmente abierto en el editor */
    private File archivoActual;
    private boolean hayCambios = false;

    
    public FrmPrincipal() {
        initComponents();
        jTextArea1 = new javax.swing.JTextArea("1\n");
        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(230, 230, 230));
        jTextArea1.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jScrollPane1.setRowHeaderView(jTextArea1);
        this.setLocationRelativeTo(null);
        actualizarTitulo();

        areaTexto.getDocument().addDocumentListener(new DocumentListener() {
            private void cambio() { hayCambios = true; actualizarTitulo(); actualizarNumerosLinea(); }
            @Override public void insertUpdate(DocumentEvent e) { cambio(); }
            @Override public void removeUpdate(DocumentEvent e) { cambio(); }
            @Override public void changedUpdate(DocumentEvent e) { cambio(); }
        });
        actualizarNumerosLinea();

        jLabel5.setText("Liiiiisssssstoo");
        // Confirmación al intentar cerrar la ventana
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                 if (!confirmarGuardarCambios()) {
                    return; // El usuario canceló el cierre
                }
                 dispose();
            }
        });
    }
    
    private void analizarLexico() throws IOException{
        int cont = 1;
        String expr = (String) areaTexto.getText();
        Lexer lexer = new Lexer(new StringReader(expr));
        String resultado = "LINEA " + cont + "\t\tSIMBOLO\n";
        while (true) {
            Tokens token = lexer.yylex();
            if (token == null) {
                txtAnalizarLex.setText(resultado);
                return;
            }
            cont++;
switch (token) {
    case ERROR:
        resultado += "  <Símbolo no definido>\n";
        break;
    case IDENTIFIER:
        resultado += "  <IDENTIFIER>\t" + lexer.lexeme + "\n";
        break;
    case INTEGER_LITERAL:
        resultado += "  <INTEGER_LITERAL>\t" + lexer.lexeme + "\n";
        break;
    case FLOAT_LITERAL:
        resultado += "  <FLOAT_LITERAL>\t" + lexer.lexeme + "\n";
        break;
    case STRING_LITERAL:
        resultado += "  <STRING_LITERAL>\t" + lexer.lexeme + "\n";
        break;
    case CHAR_LITERAL:
        resultado += "  <CHAR_LITERAL>\t" + lexer.lexeme + "\n";
        break;
    case BYTE_LITERAL:
        resultado += "  <BYTE_LITERAL>\t" + lexer.lexeme + "\n";
        break;
    case BOOL_LITERAL:
        resultado += "  <BOOL_LITERAL>\t" + lexer.lexeme + "\n";
        break;
    case NULL_LITERAL:
        resultado += "  <NULL_LITERAL>\t" + lexer.lexeme + "\n";
        break;
    case IF:
    resultado += "  <IF>\t\t" + lexer.lexeme + "\n";
    break;
case ELIF:
    resultado += "  <ELIF>\t\t" + lexer.lexeme + "\n";
    break;
case ELSE:
    resultado += "  <ELSE>\t\t" + lexer.lexeme + "\n";
    break;
case WHILE:
    resultado += "  <WHILE>\t\t" + lexer.lexeme + "\n";
    break;
case FOR:
    resultado += "  <FOR>\t\t" + lexer.lexeme + "\n";
    break;
case IN:
    resultado += "  <IN>\t\t" + lexer.lexeme + "\n";
    break;
case RETURN:
    resultado += "  <RETURN>\t\t" + lexer.lexeme + "\n";
    break;
case PRINT:
    resultado += "  <PRINT>\t\t" + lexer.lexeme + "\n";
    break;
case TRUE:
    resultado += "  <TRUE>\t\t" + lexer.lexeme + "\n";
    break;
case FALSE:
    resultado += "  <FALSE>\t\t" + lexer.lexeme + "\n";
    break;
case NONE:
    resultado += "  <NONE>\t\t" + lexer.lexeme + "\n";
    break;
case AS:
    resultado += "  <AS>\t\t" + lexer.lexeme + "\n";
    break;
case FROM:
    resultado += "  <FROM>\t\t" + lexer.lexeme + "\n";
    break;
case IMPORT:
    resultado += "  <IMPORT>\t\t" + lexer.lexeme + "\n";
    break;
case PASS:
    resultado += "  <PASS>\t\t" + lexer.lexeme + "\n";
    break;
case BREAK:
    resultado += "  <BREAK>\t\t" + lexer.lexeme + "\n";
    break;
case INPUT:
    resultado += "  <INPUT>\t\t" + lexer.lexeme + "\n";
    break;
case CONTINUE:
    resultado += "  <CONTINUE>\t" + lexer.lexeme + "\n";
    break;
 case MAIN:
    resultado += "  <MAIN>\t\t" + lexer.lexeme + "\n";
    break;   
//RUST
case DEF:
    resultado += "  <DEF>\t\t" + lexer.lexeme + "\n";
    break;
case FN:
    resultado += "  <FN>\t\t" + lexer.lexeme + "\n";
    break;
case LET:
    resultado += "  <LET>\t\t" + lexer.lexeme + "\n";
    break;
case MUT:
    resultado += "  <MUT>\t\t" + lexer.lexeme + "\n";
    break;
case STRUCT:
    resultado += "  <STRUCT>\t\t" + lexer.lexeme + "\n";
    break;
case IMPL:
    resultado += "  <IMPL>\t\t" + lexer.lexeme + "\n";
    break;
case MATCH:
    resultado += "  <MATCH>\t\t" + lexer.lexeme + "\n";
    break;
case LOOP:
    resultado += "  <LOOP>\t\t" + lexer.lexeme + "\n";
    break;
case ENUM:
    resultado += "  <ENUM>\t\t" + lexer.lexeme + "\n";
    break;
case MOD:
    resultado += "  <MOD>\t\t" + lexer.lexeme + "\n";
    break;
case PUB:
    resultado += "  <PUB>\t\t" + lexer.lexeme + "\n";
    break;
case REF:
    resultado += "  <REF>\t\t" + lexer.lexeme + "\n";
    break;
case SELF:
    resultado += "  <SELF>\t\t" + lexer.lexeme + "\n";
    break;
case SUPER:
    resultado += "  <SUPER>\t\t" + lexer.lexeme + "\n";
    break;
case USE:
    resultado += "  <USE>\t\t" + lexer.lexeme + "\n";
    break;
case CRATE:
    resultado += "  <CRATE>\t\t" + lexer.lexeme + "\n";
    break;
case CONST:
    resultado += "  <CONST>\t\t" + lexer.lexeme + "\n";
    break;
case STATIC:
    resultado += "  <STATIC>\t\t" + lexer.lexeme + "\n";
    break;
case TYPE:
    resultado += "  <TYPE>\t\t" + lexer.lexeme + "\n";
    break;
case TRAIT:
    resultado += "  <TRAIT>\t\t" + lexer.lexeme + "\n";
    break;
case ASYNC:
    resultado += "  <ASYNC>\t\t" + lexer.lexeme + "\n";
    break;
case AWAIT:
    resultado += "  <AWAIT>\t\t" + lexer.lexeme + "\n";
    break;
case MOVE:
    resultado += "  <MOVE>\t\t" + lexer.lexeme + "\n";
    break;
case UNSAFE:
    resultado += "  <UNSAFE>\t\t" + lexer.lexeme + "\n";
    break;
    
// DATOS PRIMITIVOS
case INT:
    resultado += "  <INT>\t\t" + lexer.lexeme + "\n";
    break;
case FLOAT:
    resultado += "  <FLOAT>\t\t" + lexer.lexeme + "\n";
    break;
case BOOL:
    resultado += "  <BOOL>\t\t" + lexer.lexeme + "\n";
    break;
case STRING:
    resultado += "  <STRING>\t\t" + lexer.lexeme + "\n";
    break;
case CHAR:
    resultado += "  <CHAR>\t\t" + lexer.lexeme + "\n";
    break;
case BYTE:
    resultado += "  <BYTE>\t\t" + lexer.lexeme + "\n";
    break;
case UNIT:
    resultado += "  <UNIT>\t\t" + lexer.lexeme + "\n";
    break;

    
 //DATOS COMPUESTOS/ABSTRACTOS
    case VECTOR:
    resultado += "  <VECTOR>\t\t" + lexer.lexeme + "\n";
    break;
case TUPLE:
    resultado += "  <TUPLE>\t\t" + lexer.lexeme + "\n";
    break;
case OPTION:
    resultado += "  <OPTION>\t\t" + lexer.lexeme + "\n";
    break;
case RESULT:
    resultado += "  <RESULT>\t\t" + lexer.lexeme + "\n";
    break;
case MAP:
    resultado += "  <MAP>\t\t" + lexer.lexeme + "\n";
    break;
case SET:
    resultado += "  <SET>\t\t" + lexer.lexeme + "\n";
    break;
case ARRAY:
    resultado += "  <ARRAY>\t\t" + lexer.lexeme + "\n";
    break;
    
    
 //OPERADORES
    case PLUS:
    resultado += "  <PLUS>\t\t" + lexer.lexeme + "\n";
    break;
case MINUS:
    resultado += "  <MINUS>\t\t" + lexer.lexeme + "\n";
    break;
case MULTIPLY:
    resultado += "  <MULTIPLY>\t" + lexer.lexeme + "\n";
    break;
case DIVIDE:
    resultado += "  <DIVIDE>\t\t" + lexer.lexeme + "\n";
    break;
case MODULO:
    resultado += "  <MODULO>\t\t" + lexer.lexeme + "\n";
    break;
case POWER:
    resultado += "  <POWER>\t" + lexer.lexeme + "\n";
    break;

case EQUALS:
    resultado += "  <EQUALS>\t\t" + lexer.lexeme + "\n";
    break;
case NOT_EQUALS:
    resultado += "  <NOT_EQUALS>\t" + lexer.lexeme + "\n";
    break;
case GREATER_THAN:
    resultado += "  <GREATER_THAN>\t" + lexer.lexeme + "\n";
    break;
case LESS_THAN:
    resultado += "  <LEES_THAN>\t" + lexer.lexeme + "\n";
    break;
case GREATER_EQUALS:
    resultado += "  <GREATER_EQUALS>\t" + lexer.lexeme + "\n";
    break;
case LESS_EQUALS:
    resultado += "  <LESS_EQUALS>\t" + lexer.lexeme + "\n";
    break;

case AND:
    resultado += "  <AND>\t\t" + lexer.lexeme + "\n";
    break;
case OR:
    resultado += "  <OR>\t\t" + lexer.lexeme + "\n";
    break;
case NOT:
    resultado += "  <NOT>\t\t" + lexer.lexeme + "\n";
    break;

case ASSIGN:
    resultado += "  <ASSIGN>\t\t" + lexer.lexeme + "\n";
    break;
case PLUS_ASSIGN:
    resultado += "  <PLUS_ASSIGN>\t" + lexer.lexeme + "\n";
    break;
case MINUS_ASSIGN:
    resultado += "  <MINUS_ASSIGN>\t" + lexer.lexeme + "\n";
    break;
case MULTIPLY_ASSIGN:
    resultado += "  <MULTIPLY_ASSIG>\t" + lexer.lexeme + "\n";
    break;
case DIVIDE_ASSIGN:
    resultado += "  <DIVIDE_ASSIGN>\t" + lexer.lexeme + "\n";
    break;
case MODULO_ASSIGN:
    resultado += "  <MODULO_ASSIGN>\t" + lexer.lexeme + "\n";
    break;

    
//DELIMITADORES
    
case LEFT_PAREN:
    resultado += "  <LEFT_PAREN>\t" + lexer.lexeme + "\n";
    break;
case RIGHT_PAREN:
    resultado += "  <RIGHT_PAREN>\t" + lexer.lexeme + "\n";
    break;
case LEFT_BRACE:
    resultado += "  <LEFT_BRACE>\t" + lexer.lexeme + "\n";
    break;
case RIGHT_BRACE:
    resultado += "  <RIGHT_BRACE>\t" + lexer.lexeme + "\n";
    break;
case LEFT_BRACKET:
    resultado += "  <LEFT_BRACKET>\t" + lexer.lexeme + "\n";
    break;
case RIGHT_BRACKET:
    resultado += "  <RIGHT_BRACKET>\t" + lexer.lexeme + "\n";
    break;
case SEMICOLON:
    resultado += "  <SEMICOLON>\t" + lexer.lexeme + "\n";
    break;
case COLON:
    resultado += "  <COLON>\t" + lexer.lexeme + "\n";
    break;
case COMMA:
    resultado += "  <COMMA>\t" + lexer.lexeme + "\n";
    break;
case DOT:
    resultado += "  <DOT>\t\t" + lexer.lexeme + "\n";
    break;
case ARROW:
    resultado += "  <ARROW>\t\t" + lexer.lexeme + "\n";
    break;
//92 TOKENS

    

                    default:
                        
                            resultado += "Token: " + token + "\n";
                        
                        break;
                }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        T_Abrir = new javax.swing.JButton();
        btnLimpiarLex = new javax.swing.JButton();
        btnAnalizarLex = new javax.swing.JButton();
        btnAnalizarSin = new javax.swing.JButton();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        RECIENTES = new javax.swing.JTree();
        jScrollPane1 = new javax.swing.JScrollPane();
        areaTexto = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAnalizarSin = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAnalizarLex = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        B_Nuevo = new javax.swing.JMenuItem();
        B_Abrir = new javax.swing.JMenuItem();
        btnGuardar = new javax.swing.JMenuItem();
        btnGuardar_Como = new javax.swing.JMenuItem();
        Exportar = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(230, 240, 255));

        jLabel5.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N

        jToolBar1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.setRollover(true);

        T_Abrir.setIcon(new javax.swing.ImageIcon("C:\\Users\\ejrz0\\Documents\\Imagenes_Rice\\Abrirhoooo.png")); // NOI18N
        T_Abrir.setToolTipText("Abrir Archivo");
        T_Abrir.setBorderPainted(false);
        T_Abrir.setFocusPainted(false);
        T_Abrir.setFocusable(false);
        T_Abrir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        T_Abrir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        T_Abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                T_AbrirActionPerformed(evt);
            }
        });
        jToolBar1.add(T_Abrir);

        btnLimpiarLex.setFont(new java.awt.Font("Corbel", 1, 18)); // NOI18N
        btnLimpiarLex.setIcon(new javax.swing.ImageIcon("C:\\Users\\ejrz0\\Documents\\Imagenes_Rice\\Limpiar(1).png")); // NOI18N
        btnLimpiarLex.setToolTipText("Limpiar");
        btnLimpiarLex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarLexActionPerformed(evt);
            }
        });
        jToolBar1.add(btnLimpiarLex);

        btnAnalizarLex.setFont(new java.awt.Font("Corbel", 1, 18)); // NOI18N
        btnAnalizarLex.setIcon(new javax.swing.ImageIcon("C:\\Users\\ejrz0\\Documents\\Imagenes_Rice\\Lexico.png")); // NOI18N
        btnAnalizarLex.setToolTipText("Analizador Léxico");
        btnAnalizarLex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarLexActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAnalizarLex);

        btnAnalizarSin.setFont(new java.awt.Font("Corbel", 1, 18)); // NOI18N
        btnAnalizarSin.setIcon(new javax.swing.ImageIcon("C:\\Users\\ejrz0\\Documents\\Imagenes_Rice\\right (1).png")); // NOI18N
        btnAnalizarSin.setToolTipText("Analizador Sintáctico");
        btnAnalizarSin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalizarSinActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAnalizarSin);

        jSplitPane2.setDividerLocation(260);

        jScrollPane5.setViewportView(RECIENTES);

        jSplitPane2.setLeftComponent(jScrollPane5);

        areaTexto.setColumns(20);
        areaTexto.setRows(5);
        jScrollPane1.setViewportView(areaTexto);

        jSplitPane2.setRightComponent(jScrollPane1);

        txtAnalizarSin.setEditable(false);
        txtAnalizarSin.setColumns(20);
        txtAnalizarSin.setRows(5);
        jScrollPane3.setViewportView(txtAnalizarSin);

        txtAnalizarLex.setEditable(false);
        txtAnalizarLex.setColumns(20);
        txtAnalizarLex.setLineWrap(true);
        txtAnalizarLex.setRows(5);
        jScrollPane2.setViewportView(txtAnalizarLex);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 1296, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 943, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 791, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenuBar1.setBackground(new java.awt.Color(0, 0, 0));

        jMenu1.setText("File");

        B_Nuevo.setText("Nuevo");
        B_Nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_NuevoActionPerformed(evt);
            }
        });
        jMenu1.add(B_Nuevo);

        B_Abrir.setIcon(new javax.swing.ImageIcon("C:\\Users\\ejrz0\\Documents\\Imagenes_Rice\\1_abrir.png")); // NOI18N
        B_Abrir.setText("Abrir");
        B_Abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_AbrirActionPerformed(evt);
            }
        });
        jMenu1.add(B_Abrir);

        btnGuardar.setIcon(new javax.swing.ImageIcon("C:\\Users\\ejrz0\\Documents\\Imagenes_Rice\\1_Guardar_Como.png")); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jMenu1.add(btnGuardar);

        btnGuardar_Como.setIcon(new javax.swing.ImageIcon("C:\\Users\\ejrz0\\Documents\\Imagenes_Rice\\1_guardar.png")); // NOI18N
        btnGuardar_Como.setText("Guardar Como");
        btnGuardar_Como.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardar_ComoActionPerformed(evt);
            }
        });
        jMenu1.add(btnGuardar_Como);

        Exportar.setText("Exportar");
        Exportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportarActionPerformed(evt);
            }
        });
        jMenu1.add(Exportar);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem4.setText("Copiar");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem5.setText("Pegar");
        jMenu2.add(jMenuItem5);

        jMenuItem6.setText("Buscar");
        jMenu2.add(jMenuItem6);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("View");
        jMenuBar1.add(jMenu3);

        jMenu7.setText("Navigate");
        jMenuBar1.add(jMenu7);

        jMenu8.setText("Source");
        jMenuBar1.add(jMenu8);

        jMenu9.setText("Team");
        jMenuBar1.add(jMenu9);

        jMenu4.setText("Tools");
        jMenuBar1.add(jMenu4);

        jMenu5.setText("Window");
        jMenuBar1.add(jMenu5);

        jMenu6.setText("Help");
        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1098, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
   // ----- Manejo de archivos -----
    
    private void limpiarAnalisis() {
        txtAnalizarLex.setText("");
        txtAnalizarSin.setText("");
        limpiarResaltado();
    }
 private void limpiarResaltado() {
        Highlighter h = areaTexto.getHighlighter();
        h.removeAllHighlights();
    }
 
   private void actualizarNumerosLinea() {
        int lineCount = areaTexto.getLineCount();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= lineCount; i++) {
            sb.append(i).append(System.lineSeparator());
        }
        jTextArea1.setText(sb.toString());
    }

    private void resaltarLineaError(int linea){
        limpiarResaltado();
        Highlighter h = areaTexto.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255,200,200));
        try {
            int start = areaTexto.getLineStartOffset(linea-1);
            int end = areaTexto.getLineEndOffset(linea-1);
            h.addHighlight(start, end, painter);
        } catch (BadLocationException ex) {
            // ignore
        }
    }

    private void actualizarTitulo() {
        String nombre = (archivoActual != null) ? archivoActual.getName() : "Sin nombre";
        setTitle("Editor - " + nombre + (hayCambios ? " *" : ""));
    }

    private boolean confirmarGuardarCambios() {
        if (!hayCambios) {
            return true;
        }

        Object[] opciones = {"Guardar", "No guardar", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "El archivo actual tiene cambios sin guardar. ¿Desea guardarlos?",
                "Guardar cambios",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (opcion == JOptionPane.CANCEL_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
            return false;
        }
        if (opcion == JOptionPane.YES_OPTION) {
            guardar();
        }
        return true;
    }
    
    private void abrirArchivo() {
        if (!confirmarGuardarCambios()) {
            return;
        }
        
        JFileChooser chooser = new JFileChooser();
        int seleccion = chooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            archivoActual = chooser.getSelectedFile();
            try {
                String contenido = new String(Files.readAllBytes(archivoActual.toPath()));
                areaTexto.setText(contenido);
                actualizarNumerosLinea();
                hayCambios = false;
                limpiarAnalisis();
                actualizarTitulo();
                jLabel5.setText("Archivo abierto: " + archivoActual.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir el archivo: " + ex.getMessage());
            }
        }
    }

    private void guardarArchivo(File archivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write(areaTexto.getText());
            hayCambios = false;
            actualizarTitulo();
            jLabel5.setText("Archivo guardado: " + archivo.getName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage());
        }
    }

    private void guardar() {
        if (archivoActual != null) {
            guardarArchivo(archivoActual);
        } else {
            guardarComo();
        }
    }

    private void guardarComo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como...");
        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            if (!archivo.getName().toLowerCase().endsWith(".txt")) {
                archivo = new File(archivo.getAbsolutePath() + ".txt");
            }
            archivoActual = archivo;
            guardarArchivo(archivoActual);
            JOptionPane.showMessageDialog(this, "Archivo guardado correctamente.");
            jLabel5.setText("Archivo guardado: " + archivoActual.getName());
        }
    }

    private void exportarAnalisis() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar análisis");
        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            if (!archivo.getName().toLowerCase().endsWith(".txt")) {
                archivo = new File(archivo.getAbsolutePath() + ".txt");
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                bw.write("Código de entrada:\n");
                bw.write(areaTexto.getText());
                bw.write("\n\nAnálisis Léxico:\n");
                bw.write(txtAnalizarLex.getText());
                bw.write("\n\nAnálisis Sintáctico y Semántico:\n");
                bw.write(txtAnalizarSin.getText());
                JOptionPane.showMessageDialog(this, "Archivo exportado correctamente.");
                jLabel5.setText("Archivo exportado: " + archivo.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al exportar: " + ex.getMessage());
            }
        }
    }

    private void nuevoArchivo() {
        if (!confirmarGuardarCambios()) {
            return;
        }
        Object[] opciones = {"Crear", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Desea crear un nuevo archivo?",
                "Confirmar nuevo archivo",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]);

         if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        
        limpiarAnalisis();
        areaTexto.setText("");
        actualizarNumerosLinea();
        archivoActual = null;
        hayCambios = false;
        actualizarTitulo();
        jLabel5.setText("Nuevo archivo");
    }

    private void btnAnalizarSinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarSinActionPerformed
        // TODO add your handling code here:
        String ST = areaTexto.getText();
        Syntactic s = new Syntactic(new LexerCup(new StringReader(ST)));

        try {
            s.parse();
            String reporte = SemanticAnalyzer.analyze(ST);
            txtAnalizarSin.setText("An\u00e1lisis Sintáctico Realizado Correctamente\n" + reporte);
            if(reporte.contains("Errores Sem\u00e1nticos")) {
                txtAnalizarSin.setForeground(new Color(180,1,31)); 
            } else {
                txtAnalizarSin.setForeground(new Color(20, 71, 117)); //144775 rgb(20, 71, 117)
            }
            limpiarResaltado();
        } catch (Exception ex) {
            Symbol sym = s.getS();
            txtAnalizarSin.setText("Error de sintaxis en linea " + (sym.right + 1) +
                    " columna " + (sym.left + 1) + ". Texto inesperado: \"" + sym.value + "\"");
            txtAnalizarSin.setForeground(Color.red);
            resaltarLineaError(sym.right + 1);
        }
    }//GEN-LAST:event_btnAnalizarSinActionPerformed

    private void btnLimpiarLexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarLexActionPerformed
        // TODO add your handling code here:
        txtAnalizarLex.setText(null);
        txtAnalizarSin.setText(null);
        limpiarResaltado();
    }//GEN-LAST:event_btnLimpiarLexActionPerformed

    private void btnAnalizarLexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalizarLexActionPerformed
        try {
            analizarLexico();
        } catch (IOException ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAnalizarLexActionPerformed

    private void B_AbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_AbrirActionPerformed
        abrirArchivo();
    }//GEN-LAST:event_B_AbrirActionPerformed

    private void btnGuardar_ComoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardar_ComoActionPerformed
        guardarComo();

    }//GEN-LAST:event_btnGuardar_ComoActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void T_AbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_T_AbrirActionPerformed
        abrirArchivo();
    }//GEN-LAST:event_T_AbrirActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
         guardar();
        
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void B_NuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_NuevoActionPerformed
          nuevoArchivo();
    }//GEN-LAST:event_B_NuevoActionPerformed

    private void ExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportarActionPerformed
        exportarAnalisis();
    }//GEN-LAST:event_ExportarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmPrincipal().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem B_Abrir;
    private javax.swing.JMenuItem B_Nuevo;
    private javax.swing.JMenuItem Exportar;
    private javax.swing.JTree RECIENTES;
    private javax.swing.JButton T_Abrir;
    private javax.swing.JTextArea areaTexto;
    private javax.swing.JButton btnAnalizarLex;
    private javax.swing.JButton btnAnalizarSin;
    private javax.swing.JMenuItem btnGuardar;
    private javax.swing.JMenuItem btnGuardar_Como;
    private javax.swing.JButton btnLimpiarLex;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextArea txtAnalizarLex;
    private javax.swing.JTextArea txtAnalizarSin;
    // End of variables declaration//GEN-END:variables
}
