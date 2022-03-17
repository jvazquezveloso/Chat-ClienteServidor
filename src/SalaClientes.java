import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SalaClientes implements Runnable{

    public static ArrayList<SalaClientes> salasClientes = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String nombreUsuario;

    public SalaClientes(Socket socket){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.nombreUsuario = bufferedReader.readLine();
            salasClientes.add(this);
            mandarMensaje("Servidor: "+nombreUsuario+" se ha unido al chat.");
        }catch (IOException e){
            cerrarTodo(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        String mensajeCLiente;

        while (socket.isConnected()){
            try {
                mensajeCLiente = bufferedReader.readLine();
                mandarMensaje(mensajeCLiente);
            }catch (IOException r){
                cerrarTodo(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }


    public void mandarMensaje(String mensaje){
        for (SalaClientes salaClientes : salasClientes){
            try {
                if (!salaClientes.nombreUsuario.equals(nombreUsuario)){
                    salaClientes.bufferedWriter.write(mensaje);
                    salaClientes.bufferedWriter.newLine();
                    salaClientes.bufferedWriter.flush();
                }
            }catch (IOException e){
                cerrarTodo(socket, bufferedReader, bufferedWriter);
            }
        }
    }


    public void borrarSalaClientes(){
        salasClientes.remove(this);
        mandarMensaje(("Servidor: "+nombreUsuario+ " ha dejado la sala."));
    }

    public void cerrarTodo(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        borrarSalaClientes();
        try{
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
