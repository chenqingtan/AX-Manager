package pangolin.ax.plus;
import android.content.*;
import android.content.pm.*;
import android.os.*;
import android.support.multidex.*;
import com.google.android.gms.ads.*;
import java.io.*;
import java.security.*;
import java.util.*;
import pangolin.ax.plus.XActivity.*;
import sun1.security.provider.*;

public class XApp extends MultiDexApplication implements Thread.UncaughtExceptionHandler
{
	private  final String       PATH             = Environment.getExternalStorageDirectory().getAbsolutePath();
	private  final String       FILE_NAME_SUFFIX = ".log";
	private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
	@Override
	public void uncaughtException(Thread p1, Throwable p2)
	{
		exportExceptionToSDCard(p2);
		Intent debug=new Intent();
		debug.setClass(this, XDebug.class);
		debug.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(debug);
		System.exit(0);
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		MobileAds.initialize(this,"ca-app-pub-9413497149279615~5070567232");
		Security.addProvider(new JavaProvider());
	}

	private void exportExceptionToSDCard(Throwable e)
	{
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return;
		File dks=new File(PATH + File.separator + "AXPlus");
		if (!dks.exists())dks.mkdirs();
		File file = new File(PATH + File.separator + "AXPlus/Debug" + FILE_NAME_SUFFIX);
		try
		{
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
			pw.println(appendPhoneInfo());
			e.printStackTrace(pw);
			pw.close();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		catch (PackageManager.NameNotFoundException e1)
		{
			e1.printStackTrace();
		}
	}

	private String appendPhoneInfo() throws PackageManager.NameNotFoundException
	{
		PackageManager pm = this.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
		StringBuilder sb = new StringBuilder();
		//App版本
		sb.append("App Version: ");
		sb.append(pi.versionName);
		sb.append("_");
		sb.append(pi.versionCode + "\n");

		//Android版本号
		sb.append("OS Version: ");
		sb.append(Build.VERSION.RELEASE);
		sb.append("_");
		sb.append(Build.VERSION.SDK_INT + "\n");

		//手机制造商
		sb.append("Vendor: ");
		sb.append(Build.MANUFACTURER + "\n");

		//手机型号
		sb.append("Model: ");
		sb.append(Build.MODEL + "\n");

		//CPU架构
		sb.append("CPU: ");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			sb.append(Arrays.toString(Build.SUPPORTED_ABIS));
		}
		else
		{
			sb.append(Build.CPU_ABI);
		}
		return sb.toString();
	}
}
