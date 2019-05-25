package pangolin.ax.plus.XFragMent;

import pangolin.ax.plus.*;
import pangolin.ax.plus.XBase.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;

public class XDialogLongClick extends XBaseDialogFragMent
{
	private CallBack call;
	private Button Copy;
	private Button Move;
	private Button Delete;
	private Button Compress;
	private String Suffixes;
	private boolean Decompression=false;
	public void BindListener(CallBack call)
	{
		this.call = call;
	}
	@Override
	protected int XBindXml()
	{
		return R.layout.dialog_longclick;
	}
	public void Suffixes(String type)
	{
		this.Suffixes = type;
	}
	@Override
	protected void XBindView()
	{
		Copy = (Button) $(R.id.copy);
		Move = (Button) $(R.id.move);
		Delete = (Button) $(R.id.delete);
		Compress = (Button) $(R.id.Compress);
		if (Suffixes != null)
		{
			if (Suffixes.endsWith(".apk")
				|| Suffixes.endsWith(".zip")
				|| Suffixes.endsWith(".aar")
				|| Suffixes.endsWith(".rar"))
				Compress.setText("解压");
			Decompression = true;
		}
		if (Tag.equals("XLongClick1"))
		{
			Copy.setText("复制->");
			Move.setText("移动->");
		}
		else if (Tag.equals("XLongClick2"))
		{
			Copy.setText("<-复制");
			Move.setText("<-移动");
		}
		Copy.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					call.Copy();
					dismiss();
				}
			});
		Move.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					call.Move();
					dismiss();
				}
			});
		Delete.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					call.Del();
					dismiss();
				}
			});
		Compress.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (Decompression == true)
						call.Decompression();
					else
						call.Compress();
					dismiss();
				}
			});
	}
	public interface CallBack
	{
		void Copy()
		void Del()
		void Move()
		void Renaming()
		void Compress()
		void Attribute()
		void Share()
		void openType()
		void AddBook()
		void Encrypt()
		void Decompression()
	}
}
