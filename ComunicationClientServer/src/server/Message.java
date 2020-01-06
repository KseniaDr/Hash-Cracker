package server;

public class Message {

    char[] teamName = new char[32];
    char type;
    char[] hash = new char[40];
    char originalLength;
    char[] originalStringStart = new char[256];
    char[] originalStringEnd = new char[256];

    public Message(char[] teamName, char type, char[] hash, char originalLength, char[] originalStringStart, char[] originalStringEnd){
        this.teamName = teamName;
        this.type = type;
        this.hash = hash;
        this.originalLength = originalLength;
        this.originalStringStart = originalStringStart;
        this.originalStringEnd = originalStringEnd;
    }
}
