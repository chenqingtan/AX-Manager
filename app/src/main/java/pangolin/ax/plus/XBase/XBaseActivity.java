package pangolin.ax.plus.XBase;
import android.os.*;
import android.support.v7.app.*;
import android.view.*;
import android.util.*;
import android.widget.*;

//AppCompatActivity基类
public abstract class XBaseActivity extends AppCompatActivity
{
	protected abstract int XBindXmlId()
	protected abstract void XBindView()
	protected abstract void XBindListener()
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//绑定视图id
		setContentView(XBindXmlId());
		//初始化函数
		XBindView();
		//绑定事件
		XBindListener();
	}
	//查找控件id
	public View $(int Id)
	{
		View View=findViewById(Id);
		if (View == null)
			throw new AndroidRuntimeException("Id Null");
		return View;
	}
	//显示toast
	public void ShowToast(String Msg)
	{
		Toast.makeText(this,Msg,0).show();
	}
}
