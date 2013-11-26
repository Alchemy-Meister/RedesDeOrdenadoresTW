package connection;
import java.net.*;
import java.io.*;

/**
 *
 * <p>Title: Project ROTW 18</p>
 *
 * <p>Description: Hace transparente la funcionalidad basica de la clase Socket</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: ESIDE</p>
 *
 * @author Unai Hernandez Jayo & Cia
 * @version 1.0
 */

public class SocketManager extends Socket {
    private Socket mySocket;

    private DataOutputStream bufferEscritura;
    private BufferedReader bufferLectura;

    public SocketManager(Socket sock) throws IOException {
        this.mySocket = sock;
        InicializaStreams();
    }

    /**
     *
     * @param address InetAddress
     * @param port int numero de puerto
     * @throws IOException
     */
    public SocketManager(InetAddress address, int port) throws IOException {
        mySocket = new Socket();
        mySocket.connect(new InetSocketAddress(address, port), 100);
        mySocket.setSoTimeout(100);
        InicializaStreams();
    }

    /**
     *
     * @param host String nombre del servidor al que se conecta
     * @param port int puerto de conexion
     * @throws IOException
     */
    public SocketManager(String host, int port) throws IOException {
        mySocket = new Socket(host, port);
        InicializaStreams();
    }

    /**
     * Inicializacion de los bufferes de lectura y escritura del socket
     * @throws IOException
     */
    public void InicializaStreams() throws IOException {
        bufferEscritura = new DataOutputStream(mySocket.getOutputStream());
        bufferLectura = new BufferedReader(new InputStreamReader(mySocket.
                getInputStream()));
    }

    public void CerrarStreams() throws IOException {
        bufferEscritura.close();
        bufferLectura.close();
    }

    public void CerrarSocket() throws IOException {
        mySocket.close();
    }

    /**
     *
     * @return String
     * @throws IOException
     */
    public String Leer() throws IOException {
        return (bufferLectura.readLine());
    }

    public void Escribir(String contenido) throws IOException {
        bufferEscritura.writeBytes(contenido);
    }

    public void Escribir(byte[] buffer, int bytes) throws IOException {
        bufferEscritura.write(buffer, 0, bytes);
    }
}
