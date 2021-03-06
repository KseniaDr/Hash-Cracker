package server;
import message.Message;
import mutual.HelperFunctions;
import mutual.MessagingProtocol;

public class ServerProtocol implements MessagingProtocol<Message>, Runnable {

    HelperFunctions helperFunctions;
    Server server;
    Message reqMessage;
    private String destIPAddress, destPort;

    public ServerProtocol(Server server, Message message, String destIPAddress, String destPort){
        helperFunctions = new HelperFunctions();
        this.server = server;
        reqMessage = message;
        this.destIPAddress = destIPAddress;
        this.destPort = destPort;
    }

    public ServerProtocol(Server server){
        helperFunctions = new HelperFunctions();
        this.server = server;
    }

    @Override
    public Message process(Message msg) {
        switch (msg.getType()){
            case 1: //discover message
                return new Message(msg.getTeamName(),(char)2, new char[0],'0', new char[0], new char[0]);
            case 3: //request message
                String foundHash = findHash(msg);
                if (foundHash != null)
                    return new Message(msg.getTeamName(),(char)4,msg.getHash(),msg.getOriginalLength(),convertStringToArrayChar(foundHash),msg.getOriginalStringEnd());
                else
                    return new Message(msg.getTeamName(),(char)5,new char[0],msg.getOriginalLength(),new char[0],new char[0]);
        }
        return null;
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }


    private String findHash(Message msg){
        return helperFunctions.tryDeHash(buildString(msg.getOriginalStringStart()),buildString(msg.getOriginalStringEnd()),buildString(msg.getHash()));
    }
    private String buildString(char[] array){
        String ans = "";
        for (int i=0; i<array.length; i++){
            if(array[i] == 0)
                break;
            ans = ans + array[i];
        }
        return ans;
    }

    /**
     * converts a string to an array of chars.
     * @param toConvert - the string that needs to be converted
     * @return the array of chars of the original string
     */
    private char[] convertStringToArrayChar(String toConvert){
        char[] answer = new char[toConvert.length()];
        for (int i = 0; i < toConvert.length(); i++)
            answer[i] = toConvert.charAt(i);
        return answer;
    }

    @Override
    public void run() {
        if(reqMessage.getType() == 1)
            server.sendOffer(process(reqMessage),destIPAddress, destPort);
        else
            server.send(process(reqMessage),destIPAddress, destPort);
    }
}
