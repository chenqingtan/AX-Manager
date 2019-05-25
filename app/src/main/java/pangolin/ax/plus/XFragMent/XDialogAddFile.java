package pangolin.ax.plus.XFragMent;

import pangolin.ax.plus.*;
import pangolin.ax.plus.XBase.*;
import android.widget.*;
import android.view.View.*;
import android.view.*;

public class XDialogAddFile extends XBaseDialogFragMent
{
	private TextView Title;
	private EditText Name;
	private Button CanCel;
	private Button File;
	private Button Dir;
	private CallBack callback;
	public XDialogAddFile(CallBack callback)
	{
		this.callback=callback;
	}
	@Override
	protected int XBindXml()
	{
		return R.layout.dialog_addfile;
	}
	@Override
	protected void XBindView()
	{
		setCancelable(false);
		Title=(TextView) $(R.id.Title);
		Title.setText("新建");
		Name=(EditText) $(R.id.Name);
		CanCel=(Button) $(R.id.cancel);
		File=(Button) $(R.id.file);
		Dir=(Button) $(R.id.dir);
		CanCel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					dismiss();
				}
			});
		File.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String name=Name.getText().toString().trim();
					if(!name.isEmpty())
					callback.CallFile(name);
					
					dismiss();
				}
			});
		Dir.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					String name=Name.getText().toString().trim();
					if(!name.isEmpty())
						callback.CallDir(name);
						
					dismiss();
				}
			});
	}
	public interface CallBack
	{
		void CallFile(String Name)
		void CallDir(String Name)
	}
	
}
