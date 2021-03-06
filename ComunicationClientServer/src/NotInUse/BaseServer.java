package NotInUse;

import mutual.MessagingProtocol;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

public abstract class BaseServer<T> implements Closeable {

	private final int port;
    private final Supplier<MessagingProtocol<T>> protocolFactory;
    private final Supplier<MessageEncoderDecoder<T>> encdecFactory;
    private ServerSocket sock;
 
    public BaseServer(int port, Supplier<MessagingProtocol<T>> protocolFactory, Supplier<MessageEncoderDecoder<T>> encdecFactory) {
        this.port = port;
        this.protocolFactory = protocolFactory;
        this.encdecFactory = encdecFactory;
        this.sock = null;
    }
  

    public void serve() {
 
        try (ServerSocket serverSock = new ServerSocket(port)) {
 
            this.sock = serverSock; //just to be able to close
 
            while (!Thread.currentThread().isInterrupted()) {
 
                Socket clientSock = serverSock.accept();
 
                BlockingConnectionHandler<T> handler = new BlockingConnectionHandler<>(
                        clientSock,
                        encdecFactory.get(),
                        protocolFactory.get());
 
                execute(handler);
            }
        } catch (IOException ex) {
        }
 
        System.out.println("server closed!!!");
    }
 
    @Override
    public void close() throws IOException {
        if (sock != null)
            sock.close();
    }
 
    protected abstract void execute(BlockingConnectionHandler<T>  handler);
 
 
  //For thread per client implementation:
 
    public static <T> BaseServer<T> threadPerClient(
            int port,
            Supplier<MessagingProtocol<T>> protocolFactory,
            Supplier<MessageEncoderDecoder<T>> encoderDecoderFactory) {
 
        return new BaseServer<T>(port, protocolFactory, encoderDecoderFactory) {
            @Override
            protected void execute(BlockingConnectionHandler<T> handler) {
                new Thread(handler).start();
            }
        };
 
    }
	

}
