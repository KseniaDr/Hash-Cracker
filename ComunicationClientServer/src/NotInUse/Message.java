package NotInUse;

public class Message {

    public char[] teamName;
    char type;
    char[] hash;
    char originalLength;
    char[] originalStringStart;
    char[] originalStringEnd;

    public Message(char[] teamName, char type, char[] hash, char originalLength, char[] originalStringStart, char[] originalStringEnd){
        this.teamName = teamName;
        this.type = type;
        this.hash = hash;
        this.originalLength = originalLength;
        this.originalStringStart = originalStringStart;
        this.originalStringEnd = originalStringEnd;
    }

    public char[] getTeamName() {
        return teamName;
    }
}
