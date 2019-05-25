package pangolin.ax.plus.XListener;

import android.content.*;
import java.io.*;
import pangolin.ax.plus.XAdapter.*;
import pangolin.ax.plus.XFragMent.*;
import java.util.*;
import android.widget.*;
import pangolin.ax.plus.XActivity.*;

public class XLongClickBindListener implements XDialogLongClick.CallBack
{
	private Context Base;
	private File mFile;
	private List<String> Selected=new ArrayList<String>();
	public XLongClickBindListener(Context Base,File mFile,XFileAdapter Adapter)
	{
		this.Base=Base;
		this.mFile=mFile;
		if(Adapter.isSelected.containsValue(true))
		{
			for(Integer i: Adapter.isSelected.keySet())
			{
				if(Adapter.isSelected.get(i))
				{
					Selected.add(Adapter.mValues.get(i));
				}
			}
		}
	}
	@Override
	public void Copy()
	{
		// TODO: Implement this method
	}

	@Override
	public void Del()
	{
		if(Selected.size()!=0)
		{
			for(String n:Selected)
			{
				new File(n).delete();
			}
		}else
		{
			if (!mFile.exists())
			{
				Toast.makeText(Base,"文件或文件夹不存在",0).show();
				return;
			}
			mFile.delete();
		}
		XMainActivity.Refresh();
	}

	@Override
	public void Move()
	{
		// TODO: Implement this method
	}

	@Override
	public void Renaming()
	{
		// TODO: Implement this method
	}

	@Override
	public void Compress()
	{
		// TODO: Implement this method
	}

	@Override
	public void Attribute()
	{
		// TODO: Implement this method
	}

	@Override
	public void Share()
	{
		// TODO: Implement this method
	}

	@Override
	public void openType()
	{
		// TODO: Implement this method
	}

	@Override
	public void AddBook()
	{
		// TODO: Implement this method
	}

	@Override
	public void Encrypt()
	{
		// TODO: Implement this method
	}

	@Override
	public void Decompression()
	{
		// TODO: Implement this method
	}

}
