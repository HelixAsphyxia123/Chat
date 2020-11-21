/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

/**
 *
 * @author valti
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;


public class PrincipalChat extends JFrame {

    public JTextField campoTexto; //Para mostrar mensajes de los usuarios
    public JTextArea areaTexto; //Para ingresar mensaje a enviar
    private static ServerSocket servidor;
    private static Socket cliente; //Socket para conectarse con el cliente
    private static String ip = "127.0.0.1"; //ip a la cual se conecta
    private BufferedImage fondo;

    public static PrincipalChat main;

    public PrincipalChat() throws MalformedURLException, IOException {
        super("Cliente"); //Establece titulo al Frame
        fondo = ImageIO.read(new File("src/fondo/fondo.jpg"));//fondo para el server

        campoTexto = new JTextField(); //crea el campo para texto
        campoTexto.setEditable(false); //No permite que sea editable el campo de texto
        add(campoTexto, BorderLayout.NORTH); //Coloca el campo de texto en la parte superior

        areaTexto = new JTextArea() { //Crear Area
            {
                setOpaque(false);
            }

            public void paintComponent(Graphics g) {
                g.drawImage(fondo, 0, 0, (int) getSize().getWidth(), (int) getSize().getHeight(), this);
                super.paintComponent(g);
            }
        };
        areaTexto.setFont(areaTexto.getFont().deriveFont(20f));
        areaTexto.setEditable(false);
        add(new JScrollPane(areaTexto), BorderLayout.CENTER);

        areaTexto.setForeground(Color.white); 
        campoTexto.setForeground(Color.BLACK); 
        //Crea menu Archivo y submenu Salir, ademas agrega el submenu al menu
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem salir = new JMenuItem("Salir");
        menuArchivo.add(salir); //Agrega el submenu Salir al menu menuArchivo

        JMenuBar barra = new JMenuBar(); //Crea la barra de menus
        setJMenuBar(barra); //Agrega barra de menus a la aplicacion
        barra.add(menuArchivo); //agrega menuArchivo a la barra de menus

        //Accion que se realiza cuando se presiona el submenu Salir
        salir.addActionListener(new ActionListener() { //clase interna anonima
            public void actionPerformed(ActionEvent e) {
                System.exit(0); //Sale de la aplicacion
            }
        });

        setSize(300, 320); //Establecer tamano a ventana
        setVisible(true); //Pone visible la ventana
    }

    //Para mostrar texto en displayArea
    public void mostrarMensaje(String mensaje) {
        areaTexto.append(mensaje + "\n");
    }

    public void habilitarTexto(boolean editable) {
        campoTexto.setEditable(editable);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException {
        String U = null;
        U = JOptionPane.showInputDialog("Ingrese su usuario:");
        if (U.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El usuario esta vacío");
            PrincipalChat main = new PrincipalChat(); //Instanciacion de la clase Principalchat
        } else {
            PrincipalChat main = new PrincipalChat(); //Instanciacion de la clase Principalchat
            main.setLocationRelativeTo(null);   //Centrar el JFrame
            main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //habilita cerrar la ventana
            ExecutorService executor = Executors.newCachedThreadPool(); //Para correr los threads

            try {
                main.mostrarMensaje("Esperando......");
                cliente = new Socket(InetAddress.getByName(ip), 11111); //comunicarme con el servidor
                main.mostrarMensaje("Conectado a :" + cliente.getInetAddress().getHostName());
                main.mostrarMensaje("Bienvenido " + U);

                main.habilitarTexto(true); //habilita el texto

                //Ejecucion de los Threads
                executor.execute(new ThreadRecibe(cliente, main));
                executor.execute(new ThreadEnvia(cliente, main, U));

            } catch (IOException ex) {
                Logger.getLogger(PrincipalChat.class.getName()).log(Level.SEVERE, null, ex);
            } //Fin del catch //Fin del catch
            finally {
            }
            executor.shutdown();
        }
    }
}