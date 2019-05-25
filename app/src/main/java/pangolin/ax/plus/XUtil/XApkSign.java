package pangolin.ax.plus.XUtil;
import android.content.*;
import com.android.apksig.*;
import java.io.*;

public class XApkSign
{
	public static void Sign(
		Context Base,
		String Jks,
		String Alias,
		String AliasPass,
		String Pass,
		final File OutApk,
		final File InApk,
		ApkSign.LoadKeyCallback call) throws Exception
	{
		com.android.apksig.ApkSign.loadKey(Base,
										   Jks,
										   Alias,
										   AliasPass, 
										   Pass, 
										   call);
	}
}
