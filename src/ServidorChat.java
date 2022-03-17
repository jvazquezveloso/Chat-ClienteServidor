import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorChat {

    private ServerSocket serverSocket;

    public ServidorChat(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    public void startServer(){
        try {
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("Nueva conexión.");
                SalaClientes salaclientes = new SalaClientes(socket);

                Thread hilo = new Thread(salaclientes);
                hilo.start();
            }
        }catch (IOException e ){
        }
    }

    public void cerrarServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1000);
        ServidorChat servidor = new ServidorChat(serverSocket);
        servidor.startServer();

    }
}
