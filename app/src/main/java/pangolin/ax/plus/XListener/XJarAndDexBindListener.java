package pangolin.ax.plus.XListener;
import android.content.*;
import android.util.*;
import java.io.*;
import pangolin.ax.plus.XActivity.*;
import pangolin.ax.plus.XFragMent.*;
import pangolin.ax.plus.XUtil.*;
import pangolin.ax.plus.XWidget.*;
import android.widget.*;

public class XJarAndDexBindListener implements XDialogJarAndDex.CallBack
{
	private Context Base;
	private File mFile;
	public XJarAndDexBindListener(Context Base,File mFile)
	{
		this.Base=Base;
		this.mFile=mFile;
	}
	@Override
	public void DexPlusEditor()
	{
		
	}

	@Override
	public void DexEditor()
	{

	}

	@Override
	public void StringTranslate()
	{

	}

	@Override
	public void Dex2Jar()
	{
		XProgressDialog.getInstance().show(Base,"转换中.......");
		new Thread(new Runnable(){

				@Override
				public void run()
				{
					try
					{
						XDex2Jar.from(mFile, new File(mFile.getAbsolutePath().toLowerCase().replace(".dex", ".jar")));
					}
					catch (IOException e)
					{
						XAlertDialog.ShowTraceDialog(Base,Log.getStackTraceString(e));
					}
					finally{
						XProgressDialog.getInstance().hide();
						XMainActivity.Refresh();
					}
				}
			}).start();
	}

}
