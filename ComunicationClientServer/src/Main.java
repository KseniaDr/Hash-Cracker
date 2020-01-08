import server.Server;

public class Main {
    public static void main(String[] args){
      // Client client = new Client(1111);
        //Thread client1 = new Thread(client);
           Server server = new Server();
           // Thread listener = new Thread(server); //for new discover requests
           // listener.start();
        //client1.start();
            server.listen();

      //  String hash=helperFunctions.hash("ab");
      //  System.out.println(hash);

    }
}
