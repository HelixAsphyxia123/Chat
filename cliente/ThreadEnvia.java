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
import servidor.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.SocketException;
       
public class ThreadEnvia implements Runnable {
    private final PrincipalChat main;
    private ObjectOutputStream salida;
    private String mensaje;
    private Socket conexion;
   
    public ThreadEnvia(Socket conexion, final PrincipalChat main, String name){
        this.conexion = conexion;
        this.main = main;
       
        //Evento que ocurre al escribir en el areaTexto
        main.campoTexto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                mensaje = event.getActionCommand();
                
                enviarDatos(mensaje, name); //se envia el mensaje
                main.campoTexto.setText(""); //borra el texto del enterfield
            } //Fin metodo actionPerformed
        }
        );//Fin llamada a addActionListener
    }
   
   //enviar objeto a cliente
   private void enviarDatos(String mensaje, String name){
      try {
         salida.writeObject(name +" : " + mensaje);
         salida.flush(); //flush salida a cliente
         main.mostrarMensaje(name + " : " + mensaje);
      } //Fin try
      catch (IOException ioException){
         main.mostrarMensaje("Error escribiendo Mensaje");
      } //Fin catch  
     
   } //Fin methodo enviarDatos
 
   //manipula areaPantalla en el hilo despachador de eventos
    public void mostrarMensaje(String mensaje) {
        main.areaTexto.append(mensaje);
    }
   
    public void run() {
         try {
            salida = new ObjectOutputStream(conexion.getOutputStream());
            salida.flush();
        } catch (SocketException ex) {
        } catch (IOException ioException) {
          ioException.printStackTrace();
        } catch (NullPointerException ex) {
        }
    }  
   
}