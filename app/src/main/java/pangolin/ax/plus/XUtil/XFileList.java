package pangolin.ax.plus.XUtil;

import java.io.*;
import java.util.*;
import pangolin.ax.plus.XAdapter.*;

public class XFileList
{
	public static void GetFile(File Path,List<String> mList,XFileAdapter adapter)
	{
		mList.clear();
		adapter.notifyDataSetChanged();
		mList.add(Path.getParent());
		for(File i:XFileOrder.orderByName(Path.listFiles()))
		{
			mList.add(i.getAbsolutePath());
		}
		adapter.setmList(mList);
		adapter.setModeApk(false);
		adapter.notifyDataSetChanged();
	}
}
