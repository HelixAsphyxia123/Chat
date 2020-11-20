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
	 
	public class chatCliente extends JFrame{
	    public JTextField campoTexto;
	    public JTextArea areaTexto;
	    private static ServerSocket servidor;
	    private static Socket cliente;
	    public String Username, ip;
	    public static chatCliente main;
            private BufferedImage image; 
            
	    public chatCliente() throws MalformedURLException, IOException{
	        super("Cliente");
                image = ImageIO.read(new File("src/fondo/Fondo.jpg"));
                
	        campoTexto = new JTextField();
	        campoTexto.setEditable(false);
	        add(campoTexto, BorderLayout.NORTH);
     
	        areaTexto = new JTextArea();
	        areaTexto.setEditable(false);
	        add(new JScrollPane(areaTexto), BorderLayout.CENTER);
                
                areaTexto = new JTextArea(){
                    {setOpaque(false);}

                    public void paintComponent (Graphics g)
                    {
                        g.drawImage(image, 0, 0, (int)getSize().getWidth(), (int)getSize().getHeight(), this);
                        super.paintComponent(g);
                    }
                };
                
                areaTexto.setFont(areaTexto.getFont().deriveFont(20f));
                campoTexto.setFont(campoTexto.getFont().deriveFont(20f));
                areaTexto.setEditable(false);
                add(new JScrollPane(areaTexto), BorderLayout.CENTER);
                
	        areaTexto.setForeground(Color.WHITE);
	        campoTexto.setForeground(Color.BLACK);
                
	        JMenu menuArchivo = new JMenu("");
	        JMenuItem salir = new JMenuItem("Salir");
	        menuArchivo.add(salir);
	       
	        JMenuBar barra = new JMenuBar();
	        setJMenuBar(barra);
	        barra.add(menuArchivo);
	       
	        salir.addActionListener(new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    System.exit(0);
	                }
	        });
	       
                setSize(300, 300);
                setResizable(true);
	        setVisible(true);
	    }
	   
	    public void mostrarMensaje(String mensaje) {
	        areaTexto.append(mensaje + "\n");
	    }
            
	    public void habilitarTexto(boolean editable) {
	        campoTexto.setEditable(editable);
	    }
	   
            public String Username(){
                return Username =  JOptionPane.showInputDialog("Ingrese su nombre de usario:");
            }
            
            public String IP(){
                return ip =  JOptionPane.showInputDialog("Ingrese la direccion IP:");
            }
            
	    public static void main(String[] args) throws MalformedURLException, IOException {
	        chatCliente main = new chatCliente();
	        main.setLocationRelativeTo(null);
                main.setExtendedState(MAXIMIZED_BOTH);
	        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        ExecutorService executor = Executors.newCachedThreadPool();
	        try {
                    
                    try{
                        main.Username();
                            while(main.Username.isEmpty()){
                                JOptionPane.showMessageDialog(null, "El usuario esta vacío");
                                main.Username();
                            }
                            
                    }catch(NullPointerException exception){
                        while(main.Username == null){
                                JOptionPane.showMessageDialog(null, "El usuario esta vacío");
                                main.Username();
                        }
                    }
                        
                    main.IP();
                    try{
                            while(main.ip.isEmpty()){
                                JOptionPane.showMessageDialog(null, "La IP esta vacía");
                                main.IP();
                            }
                            
                    }catch(NullPointerException exception){
                        while(main.ip == null){
                                JOptionPane.showMessageDialog(null, "La IP esta vacía");
                                main.IP();
                        }
                    }
                    
                    main.mostrarMensaje(main.Username + " se ha conectado");
	            cliente = new Socket(InetAddress.getByName(main.ip), 11111);
	   
	            main.habilitarTexto(true);
	           
	            executor.execute(new ThreadRecibe(cliente, main));
	            executor.execute(new ThreadEnvia(cliente, main, main.Username));
	           
	        } catch (IOException ex) {
	            Logger.getLogger(chatCliente.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        finally {
	        }
	        executor.shutdown();
	    }
	}
