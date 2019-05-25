package pangolin.ax.plus.XWidget;

import android.app.*;
import android.content.*;
import android.support.v7.app.*;

import android.support.v7.app.AlertDialog;

public class XAlertDialog
{
	public static void ShowTraceDialog(final Context thiz, final String msg )
	{
		( (Activity)thiz ).runOnUiThread ( new Runnable ( ){

				@Override
				public void run ( )
				{
					AlertDialog.Builder b=new AlertDialog.Builder(thiz);
					b.setTitle("异常信息");
					b.setMessage(msg);
					b.setPositiveButton("确定",null);
					b.setNegativeButton("复制", new DialogInterface.OnClickListener(){

							@Override
							public void onClick(DialogInterface p1, int p2)
							{
								ClipboardManager cmb = (ClipboardManager) thiz.getSystemService(Context.CLIPBOARD_SERVICE);
								cmb.setPrimaryClip(ClipData.newPlainText(null, msg));
							}
						});
					b.show();
				}
			} );
	}
}
