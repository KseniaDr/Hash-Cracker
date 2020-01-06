package server;
import mutual.MessagingProtocol;

public class ServerProtocol implements MessagingProtocol<Message> {

    HelperFunctions helperFunctions;

    public ServerProtocol(){
        helperFunctions = new HelperFunctions();
    }

    @Override
    public Message process(Message msg) {
        switch (msg.type){
            case '1': //discover message
                return new Message(msg.teamName,'2', msg.hash,msg.originalLength, msg.originalStringStart, msg.originalStringEnd);
            case '3': //request message
                String foundHash = findHash(msg);
                if (foundHash != null)
                    return new Message(msg.teamName,'4',convertStringToArrayChar(foundHash),msg.originalLength,msg.originalStringStart,msg.originalStringEnd);
                else
                    return new Message(msg.teamName,'5',new char[0],msg.originalLength,msg.originalStringStart,msg.originalStringEnd);
        }
        return null;
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }


    private String findHash(Message msg){
        return helperFunctions.tryDeHash(new String(msg.originalStringEnd),new String(msg.originalStringEnd),new String(msg.hash));
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
}
