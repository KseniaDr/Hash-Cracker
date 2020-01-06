package client;


import mutual.MessagingProtocol;

public class ClientProtocol implements MessagingProtocol {
    @Override
    public Object process(Object msg) {
        return null;
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
