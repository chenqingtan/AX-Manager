package pangolin.ax.plus.XActivity;
import android.os.*;
import android.widget.*;
import java.io.*;
import pangolin.ax.plus.*;
import pangolin.ax.plus.XBase.*;
import android.support.v7.widget.Toolbar;

public class XDebug extends XBaseActivity
{
	private TextView info;
	private FileInputStream log;
	private String debuginfo;
	private Toolbar Toolbar;
	@Override
	protected int XBindXmlId()
	{
		return R.layout.activity_debug;
	}

	@Override
	protected void XBindView()
	{
		try
		{
			Toolbar=(Toolbar) $(R.id.toolbar);
			Toolbar.setTitle("崩溃日志");
			log = new FileInputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/AXPlus/Debug.log"));
			if(log!=null)
			{
				byte[] b=new byte[log.available()];
				log.read(b);
				log.close();
				debuginfo=new String(b);
				info=(TextView) $(R.id.info);
				info.setText(debuginfo);
			}
		}
		catch (Exception e)
		{}
	}

	@Override
	protected void XBindListener()
	{
	}
}
