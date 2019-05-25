package pangolin.ax.plus.XFragMent;

import android.widget.*;
import pangolin.ax.plus.*;
import pangolin.ax.plus.XBase.*;
import android.view.View.*;
import android.view.*;

public class XDialogJarAndDex extends XBaseDialogFragMent
{
	private CallBack call;
	private TextView Title;
	private Button d2j;
	public void BindListener(CallBack call)
	{
		this.call = call;
	}
	@Override
	protected int XBindXml()
	{
		return R.layout.dialog_jaranddex;
	}
	@Override
	protected void XBindView()
	{
		Title=(TextView) $(R.id.Title);
		Title.setText("打开方式...");
		d2j=(Button) $(R.id.d2j);
		d2j.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					call.Dex2Jar();
					dismiss();
				}
			});
	}
	public interface CallBack{
		void DexPlusEditor()
		void DexEditor()
		void StringTranslate()
		void Dex2Jar()
	}
}
