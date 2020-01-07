import mutual.HelperFunctions;
import server.Server;

public class Main {
    public static void main(String[] args){
        HelperFunctions helperFunctions = new HelperFunctions();
        String[] strings=helperFunctions.divideToDomains(6,2);
        for(int i=0;i<strings.length;i++){
            System.out.println(strings[i]);
        }
            Server server = new Server();
            Thread listener = new Thread(server); //for new discover requests
            listener.start();
            server.listen();


    }
}
