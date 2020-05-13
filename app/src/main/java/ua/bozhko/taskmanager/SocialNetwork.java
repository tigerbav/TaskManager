package ua.bozhko.taskmanager;

public class SocialNetwork {
    private DataBaseFirebase dataBaseFirebase;
    static SocialNetwork socialNetwork;
    private SocialNetwork(){}

    static SocialNetwork createOrReturn(){
        if(socialNetwork == null)
            socialNetwork = new SocialNetwork();
        return socialNetwork;
    }

    public void signInWithFacebook(){

    }

    public void signInWithTwitter(){

    }

    public void signInWithGoogle(){

    }
}
