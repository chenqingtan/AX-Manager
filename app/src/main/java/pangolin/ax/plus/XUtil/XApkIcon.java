package pangolin.ax.plus.XUtil;

import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.util.*;
import java.io.*;

public class XApkIcon
{
	public static Drawable showApkIcon(Context a,String apkPath)
	{
		PackageManager pm = a.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkPath,PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			appInfo.sourceDir = apkPath;
			appInfo.publicSourceDir = apkPath;
			try {
				return appInfo.loadIcon(pm);
			} catch (OutOfMemoryError e) {
			}
		}
		return null;
	}
}
