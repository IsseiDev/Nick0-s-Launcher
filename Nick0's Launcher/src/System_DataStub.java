import java.applet.Applet;
import java.applet.AppletStub;
import java.net.MalformedURLException;
import java.net.URL;

public class System_DataStub extends Applet implements AppletStub
{
    
    public static String[] MCParameters_Index = new String[] { "username", "sessionid", "fullscreen", "mppass", "server", "port", "stand-alone", "latestVersion", "downloadTicket" };
    public static String[] MCParameters_Values = new String[MCParameters_Index.length];

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Applet's Stub Functions - Used by Minecraft.jar
    
    public String getParameter(String parameter) { return static_getParameter(parameter); }

    public URL getDocumentBase()
    {
      try { return new URL("http://www.minecraft.net/"); }
      catch ( MalformedURLException e ) { System_ErrorHandler.handleException(e, false); }
      return null;
    }

    public boolean isActive() { return true; }
    public void appletResize(int width, int height) { }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Function - Array
    
    private static int getIndexValue(String index)
    {
        int value = -1;
        for(int i=0;i<MCParameters_Index.length;i++)
        {
            if ( MCParameters_Index[i].equals(index) )
            {
                value = i;
                break;
            }
        }
        return value;
    }
    
    public static void setParameter(String index, String parameter)
    {
        int indexOfParameter = getIndexValue(index);
        MCParameters_Values[indexOfParameter] = parameter;
    }

    public static String static_getParameter(String parameter)
    {
        int indexOfParameter = getIndexValue(parameter);
        return MCParameters_Values[indexOfParameter];
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
