package message;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

public class Message implements Serializable {

     private char[] teamName;
     private char type;
     private char[] hash;
     private char originalLength;
     private char[] originalStringStart;
     private char[] originalStringEnd;
     public final int maxSizeOfTeamName = 32;
     public final int maxSizeOfHash = 40;
     public final int maxSizeOfOriginalStringStartNEnds = 256;

    public Message(char[] teamName, char type, char[] hash, char originalLength, char[] originalStringStart, char[] originalStringEnd){
        this.teamName = teamName;
        this.type = type;
        this.hash = hash;
        this.originalLength = originalLength;
        this.originalStringStart = originalStringStart;
        this.originalStringEnd = originalStringEnd;
    }

    public Message(byte[] bytesForMessage){
        String byteToString;
        try {
            byteToString = new String(bytesForMessage, "UTF-8");
            teamName = getCharArray(byteToString, maxSizeOfTeamName, false);
            type = getChar(byteToString);
            hash = getCharArray(byteToString, maxSizeOfHash, false);
            originalLength = getChar(byteToString);
            originalStringStart = getCharArray(byteToString,maxSizeOfOriginalStringStartNEnds,false);
            originalStringEnd = getCharArray(byteToString,maxSizeOfOriginalStringStartNEnds,true);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public char[] getTeamName(){
        return teamName;
    }

    public char getType(){
        return type;
    }

    public char[] getHash(){
        return hash;
    }

    public char getOriginalLength(){
        return originalLength;
    }

    public char[] getOriginalStringStart(){
        return originalStringStart;
    }

    public char[] getOriginalStringEnd(){
        return originalStringEnd;
    }

    private char getChar(String toCut){
        char answer = toCut.charAt(0);
        toCut.substring(1);
        return answer;
    }

    private char[] getCharArray(String toCut, int size, boolean lastArray){
        char[] answer = new char[size];
        for (int i=0; i < size ; i++)
            answer[i] = toCut.charAt(i);
        if (lastArray == false)
            toCut.substring(size+1);
        return answer;
    }

    public byte[] messageToByteArray(){
        byte[] answer = new byte[586];
        for (int i = 0; i < teamName.length; i++)
            answer[i] = (byte)teamName[i];
        answer[32] = (byte)type;
        for (int i = 0, j = 33; i < hash.length ; i++, j++ )
            answer[j] = (byte) hash[i];
        answer[74] = (byte)originalLength;
        for (int i = 0, j = 75; i < originalStringStart.length; i++, j++)
            answer[j] = (byte)originalStringStart[i];
        for (int i = 0, j = 331; i < originalStringEnd.length; i++, j++)
            answer[j] = (byte) originalStringEnd[i];
        return answer;
    }

}