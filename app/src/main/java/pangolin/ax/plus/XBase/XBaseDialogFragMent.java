package pangolin.ax.plus.XBase;
import android.app.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v7.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public abstract class XBaseDialogFragMent extends DialogFragment
{
	protected abstract int XBindXml()
	protected abstract void XBindView()
	private View View;
	public String Tag;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	@Override
	public void show(android.support.v4.app.FragmentManager manager, String tag)
	{
		try
		{
			this.Tag=tag;
            manager.beginTransaction().remove(this).commit();
            super.show(manager, tag);
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		View = getActivity().getLayoutInflater().from(getActivity()).inflate(XBindXml(), null);
		builder.setView(View);
		XBindView();
		return builder.create();
	}
	public View $(int Id)
	{
		View v=View.findViewById(Id);
		if (v == null)
			throw new AndroidRuntimeException("Id Null");
		return v;
	}
	public void ShowToast(String Msg)
	{
		Toast.makeText(getActivity(), Msg, 0).show();
	}
}
