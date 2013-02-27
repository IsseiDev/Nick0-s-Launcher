import java.io.IOException;
import java.net.URLEncoder;

public class Web_MainTransaction
{

    public static String forceUsername = null;
    public static String forceSessionID = null;

    public static void Main_OnlineLogin(String username, String password) throws IOException
    {
        String serverArgs = "user=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + 1337; // For you, notch ;)
        String result = Web_ClientServerProtocol.connectToServerWithKey("https://login.minecraft.net/", serverArgs, DATA_MinecraftPublicKey.getPublicKey());

        if ( result == null || !result.contains(":") )
        {
            GuiForm_MainFrame.enableLoginWindow();

            if ( result == null ) { System_ErrorHandler.handleError("Impossible de se connecter à Minecraft.net !", false, false); }
            else if ( result.toLowerCase().trim().contains("bad login") )
            {
                System_ErrorHandler.handleError("Nom d'utilisateur et/ou mot de passe incorrect !", false, false);
                GuiForm_MainFrame.mainFrame.disableAntiDisplaying();
            }
            else if ( result.toLowerCase().trim().contains("old version") ) { System_ErrorHandler.handleError("Le launcher est périmé.", false, true); }
            else if ( result.toLowerCase().trim().contains("user not premium") ) { System_ErrorHandler.handleError("Paye ton jeu, boulet !", false, false); }
            else if ( result.toLowerCase().trim().contains("account migrated") ) { System_ErrorHandler.handleError("Votre compte Minecraft à été migré.\nC'est à présent un compte Mojang.\n\nVeuillez vous connecter avec votre addresse email.", false, false); }
            else { System_ErrorHandler.handleError("Une erreur inconnue s'est produite lors de la connexion : \"" + result + "\"", false, true); }

            return;
        }

        String[] serverResults = result.split(":");

        System_DataStub.setParameter("latestVersion", serverResults[0].trim());
        System_DataStub.setParameter("downloadTicket", serverResults[1].trim());
        System_DataStub.setParameter("username", ((forceUsername == null) ? serverResults[2].trim() : forceUsername));
        System_DataStub.setParameter("sessionid", serverResults[3].trim());

        // System_LogWriter.write("Minecraft SessionID : " + serverResults[3].trim());

        System_DataStub.setParameter("loginusr", username);

        System_StringEncrypter.A(password);
        Main_RealLauncher.d = (char)password.length();

        Web_MinecraftUpdater.mainMinecraftUpdater();
    }

    public static void Main_OfflineLogin(String username)
    {
        System_DataStub.setParameter("username", ((forceUsername == null) ? username : forceUsername));
        System_DataStub.setParameter("downloadTicket", (forceSessionID == null ? "" : forceSessionID));
        System_DataStub.setParameter("sessionid", "0");

        System_DataStub.setParameter("loginusr", username);

        if ( Preferences_ConfigLoader.CONFIG_AlternativeMinecraftUpdateServer )
        {
            // System_ErrorHandler.handleError("LOL BITE : " + System_DataStub.static_getParameter("AMUSVersion"), false, false);
            Web_AMUSUpdater.mainMinecraftUpdater();
        }
        else { Web_MinecraftUpdater.mainOfflineUpdater(); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
