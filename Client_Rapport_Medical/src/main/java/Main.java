import view.MainView;
import controler.*;
import model.ConnectServer;



public class Main {

    public static void main(String[] args)
    {
        MainView view = new MainView();
        ConnectServer connectServer = new ConnectServer("localhost", 6769);
        new MainController (view,connectServer);
    }
}