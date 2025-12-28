import view.MainView;
import controler.*;
import model.ConnectServer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;


public class Main {

    public static void main(String[] args)
    {
        Security.addProvider(new BouncyCastleProvider());
        MainView view = new MainView();
        ConnectServer connectServer = new ConnectServer("localhost", 6769);
        new MainController (view,connectServer);
    }
}