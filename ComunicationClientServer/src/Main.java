import server.HelperFunctions;

public class Main {
    public static void main(String[] args){
        HelperFunctions helperFunctions = new HelperFunctions();
        String[] strings=helperFunctions.divideToDomains(6,2);
        for(int i=0;i<strings.length;i++){
            System.out.println(strings[i]);
        }
    }
}
