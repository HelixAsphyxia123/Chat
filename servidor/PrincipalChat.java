/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

/**
 *
 * @author valti
 */
import cliente.chatCliente;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public class PrincipalChat extends JFrame{
    public JTextField campoTexto;
    public JTextArea areaTexto;
    private static ServerSocket servidor;
    private static Socket conexion;
    private static String ip; //"127.0.0.1"
    public static PrincipalChat main;
    public static chatCliente mainCliente;
    private BufferedImage image;
    public String username;

    public PrincipalChat() throws IOException{
        super("Servidor");
        image = ImageIO.read(new File("src/fondo/Fondo.jpg"));
        
        campoTexto = new JTextField();
        campoTexto.setEditable(false);
        add(campoTexto, BorderLayout.NORTH);
        
        areaTexto = new JTextArea(){
            {setOpaque(false);}

            public void paintComponent (Graphics g)
            {
                g.drawImage(image, 0, 0, (int)getSize().getWidth(), (int)getSize().getHeight(), this);
                super.paintComponent(g);
            }
        };
        areaTexto.setFont(areaTexto.getFont().deriveFont(20f));
        areaTexto.setEditable(false);
        add(new JScrollPane(areaTexto), BorderLayout.CENTER);
        
        areaTexto.setForeground(Color.WHITE);
        campoTexto.setForeground(Color.BLACK);
       
        JMenu menuArchivo = new JMenu();
        JMenuItem salir = new JMenuItem("Salir");
        menuArchivo.add(salir);
       
        JMenuBar barra = new JMenuBar();
        setJMenuBar(barra);
        barra.add(menuArchivo);

        salir.addActionListener(new ActionListener() { //clase interna anonima
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
        });
        setVisible(true);
    }
   
    public void mostrarMensaje(String mensaje) {
        areaTexto.append(mensaje + "\n");
    }
    
    public void habilitarTexto(boolean editable) {
        campoTexto.setEditable(editable);
    }
    
    public String IP(){
                return ip =  JOptionPane.showInputDialog("Ingrese la direccion:");
    }
 
    public static void main(String[] args) throws IOException {
        PrincipalChat main = new PrincipalChat();
        main.IP();
        main.setLocationRelativeTo(null);
        main.setExtendedState(MAXIMIZED_BOTH);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ExecutorService executor = Executors.newCachedThreadPool();
 
        try {
            servidor = new ServerSocket(11111, 100);
            
 
            while (true){
                try {
                    conexion = servidor.accept();      
                    main.habilitarTexto(false);
                    executor.execute(new ThreadRecibe(conexion, main));
                } catch (IOException ex) {
                    Logger.getLogger(PrincipalChat.class.getName()).log(Level.SEVERE,null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PrincipalChat.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
        }
        executor.shutdown();
    }
}
