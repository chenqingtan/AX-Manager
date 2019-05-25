package pangolin.ax.plus.XWidget;

import android.app.*;
import android.content.*;
import android.os.*;

public class XProgressDialog
{
	private  ProgressDialog Loading;
	public static XProgressDialog mProgressDialog;
	public static XProgressDialog getInstance()
	{  
		if (mProgressDialog == null)
		{    
			mProgressDialog = new XProgressDialog();  
		}    
        return mProgressDialog;  
	}  
	public void SetProgress(Context thiz, final int i)
	{
		if (Loading == null)return;
		if (Looper.myLooper() != Looper.getMainLooper())
		{
			new Handler(thiz.getMainLooper()).post(new Runnable(){

					@Override
					public void run()
					{
						Loading.setProgress(i);
					}
				});
		}
		else
		{
			Loading.setProgress(i);
		}
	}
	public void ShowProgress(final Context thiz , final String Msg, final int max)
	{
		if (Loading != null)
			Loading.dismiss();
		if (Looper.myLooper() != Looper.getMainLooper())
		{
			new Handler(thiz.getMainLooper()).post(new Runnable(){

					@Override
					public void run()
					{
						Loading = new ProgressDialog(thiz, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
						Loading.setMessage(Msg);
						Loading.setMax(max);
						Loading.setProgress(0);
						Loading.setProgressStyle(android.app.ProgressDialog.STYLE_HORIZONTAL);
						Loading.setCancelable(false);
						Loading.show();
					}
				});
		}
		else
		{
			Loading = new ProgressDialog(thiz, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			Loading.setMessage(Msg);
			Loading.setCancelable(false);
		    Loading.setMax(max);
			Loading.setProgress(0);
			Loading.setProgressStyle(android.app.ProgressDialog.STYLE_HORIZONTAL);
			Loading.show();
		}
	}
	public void show(final Context thiz , final String Msg)
	{
		if (Loading != null)
			Loading.dismiss();
		if (Looper.myLooper() != Looper.getMainLooper())
		{
			new Handler(thiz.getMainLooper()).post(new Runnable(){

					@Override
					public void run()
					{
						Loading = new ProgressDialog(thiz, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
						Loading.setMessage(Msg);
						Loading.setCancelable(false);
						Loading.show();
					}
				});
		}
		else
		{
			Loading = new ProgressDialog(thiz, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
			Loading.setMessage(Msg);
			Loading.setCancelable(false);
			Loading.show();
		}
	}
	public void show(Context thiz)
	{
		if (Loading != null)
			hide();
		Loading = new ProgressDialog(thiz, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
		Loading.setMessage("通讯中........");
		Loading.setCancelable(false);
		Loading.show();
	}
	public void hide()
	{
		while (true)
		{
			if (Loading != null)
			{
				Loading.dismiss();
				break;
			}
		}
	}
}
