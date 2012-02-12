import javax.swing.*;
import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLClassLoader;

public class Main_RealLauncher
{

    public static Gui_MainFrame MainFrame = null;
    public static Applet minecraftInstance = null;

    public static String configFileDir = System_UserHomeDefiner.returnConfigDirectory();
    public static String homeDir = configFileDir;

    public static String officialAddress = "nicnl25@gmail.com";
    private static String configFileName = "Nick0's_Launcher.mconf";

    public static boolean PasswordNotDisplayed = false;
    private static String StoredPassword;

    public static void main(String[] args)
    {
        // Forcer le theme de l'OS hôte
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch ( Exception e ) { System_ErrorHandler.handleException(e, false); }
        
        File modsFolder = new File(getModsDirPath());
        if ( !modsFolder.exists() ) { modsFolder.mkdir(); }

        System.out.println("Nick0's Launcher - Initialisation de l'interface en cours...");

        // Définition des variables systèmes importantes
        homeDir = getHomeDir();
        System_DataStub.setParameter("latestVersion","0");
        Preferences_ConfigLoader.SYSTEM_LoadPreferences();

        // Création de la frame principale
        MainFrame = new Gui_MainFrame();

        // Chargement des identifiants de connexion
        String[] loadedTextFile = System_ConfigFileWriter.loadConfigFile();
        if ( loadedTextFile != null )
        {
            if ( !loadedTextFile[0].equals("") )
            {
                String loadedUsername = loadedTextFile[0];
                MainFrame.Field_UserName.setText(loadedUsername);
                MainFrame.Field_UserName.setCaretPosition(loadedUsername.length());
            }

            if ( !loadedTextFile[2].equals("") && !loadedTextFile[3].equals("") )
            {
                String decodedPassword = Encrypter_StringEncrypter.decodeString(loadedTextFile[2]);
                int recodedHashCode = Encrypter_StringEncrypter.encodeString(decodedPassword).hashCode();

                if ( recodedHashCode == Integer.parseInt(loadedTextFile[3]) )
                {
                    PasswordNotDisplayed = true;
                    StoredPassword = decodedPassword;

                    MainFrame.Field_Password.setText(Encrypter_StringEncrypter.stringRandomizer(StoredPassword));

                    MainFrame.Check_SaveLogin.setSelected(true);
                }
                else { System.out.println("Nick0's Launcher - Password decrypting fail !"); }
            }
        }

        System.out.println("Nick0's Launcher - Launcher fonctionnel !");
    }

    public static void startLogin(String username, String password)
    {
        if ( MainFrame.Field_UserName.getText().equals("") ) { return; }
        else if ( ( new String(MainFrame.Field_Password.getPassword()) ).equals("") && !MainFrame.Check_Offline.isSelected() ) { return; }

        if ( !MainFrame.Check_Offline.isSelected() )
        {
            try { System_MainTransaction.Main_ClientTransactions(username, password); }
            catch ( IOException e ) { System_ErrorHandler.handleException(e, true); }
        }
        else { System_MainTransaction.Main_OfflineLogin(username); }
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GetVariables
    
    private static String getHomeDir()
    {
        String[] loadedPreferences = System_ConfigFileWriter.loadConfigFile();
        return loadedPreferences[7].split("=").length == 2 ? loadedPreferences[7].split("=")[1] : Main_RealLauncher.configFileDir;
    }
    
    public static final String getStoredPassword()
    {
        String tempPass = StoredPassword;
        StoredPassword = null;
        return tempPass;
    }
    
    public static String getConfigFilePath() { return configFileDir + File.separator + configFileName; }
    public static String getBinDirPath() { return homeDir + File.separator + "bin"; }
    public static String getNativesDirPath() { return getBinDirPath() + File.separator + "natives"; }
    public static String getModsDirPath() { return getBinDirPath() + File.separator + "mods"; }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // System functions - DO NOT USE

    public static void startMinecraft()
    {
        String TempSelectedItem = Preferences_ConfigLoader.CONFIG_SaveLastJar ? (String)MainFrame.ComboBox_JarSelector.getSelectedItem() : null;
        Preferences_ConfigLoader.CONFIG_LastJarSaved = (TempSelectedItem == null) ? "" : TempSelectedItem;

        String passwordToWrite = "";
        if ( !MainFrame.Check_Offline.isSelected() ) { passwordToWrite = Encrypter_StringEncrypter.getLastPassword(); }
        
        System_ConfigFileWriter.writeConfigFile(passwordToWrite);

        System.out.println("Initialisation de minecraft !\n\n_____________________________________\n");

        MainFrame = MainFrame.closeWindow(); // Le return est null = vide la variable MainFrame

        try { minecraftInstance = System_MinecraftLoader.LoadMinecraft(getBinDirPath()); }
        catch ( SecurityException e ) { System_ErrorHandler.handleExceptionWithText(e, "Impossible d'initialiser les mods que vous avez installé.\n\nVeuillez supprimer le dossier META-INF de votre jeu.", true, false); }
        catch ( Exception e ) { System_ErrorHandler.handleException(e, true); }

        System_GameFrame baseFrame = new System_GameFrame(System_DataStub.static_getParameter("username"));
        baseFrame.add(minecraftInstance);

        baseFrame.setVisible(true);

        // Fait apparaitre le bouton "Quit Minecraft"
        System_DataStub.setParameter("stand-alone", "true");

        // Definition du stub de données
        minecraftInstance.setStub(new System_DataStub());
        minecraftInstance.setSize(baseFrame.getSize());

        // Démarrage de Minecraft
        try
        {
            minecraftInstance.init();
            minecraftInstance.start();
            minecraftInstance.validate();
        }
        catch ( SecurityException e ) { System_ErrorHandler.handleExceptionWithText(e, "Impossible d'initialiser les mods que vous avez installé.\n\nVeuillez supprimer le dossier META-INF de votre jeu.", true, false); }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
