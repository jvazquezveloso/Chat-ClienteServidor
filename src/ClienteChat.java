import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClienteChat {

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private String nombreUsuario;

    public ClienteChat(Socket socket, String nombreUsuario){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.nombreUsuario = nombreUsuario;
        }catch (IOException e){
            cerrarTodo(socket, bufferedReader, bufferedWriter);
        }
    }

    public void mandarMensaje(){
        try {
            bufferedWriter.write(nombreUsuario);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner sc = new Scanner(System.in);
            while (socket.isConnected()){
                String mensaje = sc.nextLine();
                bufferedWriter.write(nombreUsuario+": "+mensaje);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch (IOException e){
            cerrarTodo(socket, bufferedReader, bufferedWriter);
        }
    }

    public void buscarMensaje(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String mensajeRecibido;
                while (socket.isConnected()){
                    try{
                        mensajeRecibido = bufferedReader.readLine();
                        System.out.println(mensajeRecibido);
                    }catch (IOException e){
                        cerrarTodo(socket,bufferedReader,bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void cerrarTodo(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
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

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce tu nombre de usuario: ");
        String nombreUsuario = sc.nextLine();
        Socket socket = new Socket("localhost",1000);
        ClienteChat cliente = new ClienteChat(socket, nombreUsuario);
        cliente.buscarMensaje();
        cliente.mandarMensaje();
    }
}
