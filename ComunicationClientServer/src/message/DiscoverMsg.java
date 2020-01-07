package message;

public class DiscoverMsg extends Message {

    public DiscoverMsg(char[] teamName, char type, char[] hash, char originalLength, char[] originalStringStart, char[] originalStringEnd) {
        super(teamName, type, hash, originalLength, originalStringStart, originalStringEnd);
    }
}
