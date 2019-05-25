package pangolin.ax.plus.XAdapter;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.bumptech.glide.*;
import java.io.*;
import java.text.*;
import java.util.*;
import pangolin.ax.plus.R;
import pangolin.ax.plus.XUtil.*;
import java.util.zip.*;

public class XFileAdapter extends RecyclerView.Adapter<XFileAdapter.ViewHolder> implements XRecycleItemTouchHelper.ItemTouchHelperCallback
{
	public void setTree(XZipList.Tree tree)
	{
		this.Tree = tree;
	}
	public void setModeApk(boolean flag)
	{
		this.isApk = flag;
	}
	@Override
	public void onItemDelete(final int positon)
	{
		mOnListItemClickListener.onMove(positon);
	}
	private XZipList.Tree Tree;
	public boolean isApk=false;
	public List<String> mValues;
	private OnListItemClickListener mOnListItemClickListener;
	private Context thiz;
	public  HashMap<Integer, Boolean> isSelected;
	public void init()
	{
		if (isSelected != null)
		{
			isSelected = null;
		}
		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < mValues.size(); i++)
		{
			isSelected.put(i, false);
		}
	}
	public XFileAdapter(Context context, List<String> items)
	{
		thiz = context;
		mValues = items;
		init();
	}
	public void setmList(List<String> list)
	{
		mValues = list;
		init();
	}
	public void setOnListItemClickListener(OnListItemClickListener listener)
	{
		this.mOnListItemClickListener = listener;
	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.file_item, parent, false);
		return new ViewHolder(view);
	}
	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position)
	{
		if (isSelected.get(position))
			holder.mView.setBackgroundColor(Color.parseColor("#FF438FB8"));
		else
			holder.mView.setBackgroundColor(Color.TRANSPARENT);

		if (position == 0)
		{
			holder.mTime.setText("");
			holder.mFileName.setText("...");
			holder.mFile_Name = new File(mValues.get(position)).getAbsolutePath();
		}
		else if (isApk)
		{
			holder.mFile_Name = mValues.get(position);
			holder.mFileName.setText(mValues.get(position));
			ZipEntry entry=Tree.getZip().getEntry(Tree.getCurPath() + mValues.get(position));
			if (entry != null)
				if (entry.isDirectory())
					holder.mTime.setText(new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date(entry.getTime())));
				else
					holder.mTime.setText(new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date(entry.getTime())) + "  " + XFileSize.convertBytesLength(entry.getSize()));
			else
				holder.mTime.setText("");
		}
		else if (new File(mValues.get(position)).exists()
				 && new File(mValues.get(position)).canRead())
		{
			holder.mFileName.setText(new File(mValues.get(position)).getName());
			if (!new File(mValues.get(position)).isDirectory())
				holder.mTime.setText(new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date(new File(mValues.get(position)).lastModified())) + "  " + XFileSize.getAutoFileOrFileSize(mValues.get(position)));
			else
				holder.mTime.setText(new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date(new File(mValues.get(position)).lastModified())));
			holder.mFile_Name = new File(mValues.get(position)).getName();
		}
		//长按点击事件
		holder.mView.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View p1)
				{
					Runnable run=new Runnable(){

						@Override
						public void run()
						{
							mOnListItemClickListener.Focus();
							mOnListItemClickListener.onLongClicked(holder.mFile_Name , position);
						}
					};
					new Handler().postDelayed(run, 50);
					return true;
				}
			});
		//单击事件
		holder.mView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					Runnable run=new Runnable(){

						@Override
						public void run()
						{
							mOnListItemClickListener.Focus();
							mOnListItemClickListener.onClicked(holder.mFile_Name , position);
						}
					};
					new Handler().postDelayed(run, 50);
				}
			});


		final File a=new File(mValues.get(position));
		if (position == 0)
		//上层目录
			Glide.with(holder.mImageView.getContext())
				.load(R.mipmap.file_dir)
				.circleCrop()
				.into(holder.mImageView);
		else if (a.isDirectory() || mValues.get(position).endsWith("/"))
		//文件夹
			Glide.with(holder.mImageView.getContext())
				.load(R.mipmap.file_dir)
				.circleCrop()
				.into(holder.mImageView);
		else if (a.getName().toLowerCase().endsWith(".mp4")
				 || a.getName().toLowerCase().endsWith(".mov")
				 || a.getName().toLowerCase().endsWith(".rmvb")
				 || a.getName().toLowerCase().endsWith(".ts")
				 || a.getName().toLowerCase().endsWith(".wmv")
				 || a.getName().toLowerCase().endsWith(".3gp")
				 || a.getName().toLowerCase().endsWith(".avi")
				 || a.getName().toLowerCase().endsWith(".flv")
				 || a.getName().toLowerCase().endsWith(".mkv")
				 || a.getName().toLowerCase().endsWith(".swf")
				 || a.getName().toLowerCase().endsWith(".vob"))
		//影视文件
			Glide.with(holder.mImageView.getContext())
				.load(R.mipmap.file_video)
				.circleCrop()
				.into(holder.mImageView);
		else if (a.getName().toLowerCase().endsWith(".mp3")
				 || a.getName().toLowerCase().endsWith(".ogg")
				 || a.getName().toLowerCase().endsWith(".wav")
				 || a.getName().toLowerCase().endsWith(".xm")
				 || a.getName().toLowerCase().endsWith(".mod"))
		//音频文件
			Glide.with(holder.mImageView.getContext())
				.load(R.mipmap.file_audio)
				.circleCrop()
				.into(holder.mImageView);
		else if (a.getName().toLowerCase().endsWith(".txt")
				 || a.getName().toLowerCase().endsWith(".xml")
				 || a.getName().toLowerCase().endsWith(".c")
				 || a.getName().toLowerCase().endsWith(".cc")
				 || a.getName().toLowerCase().endsWith(".cpp")
				 || a.getName().toLowerCase().endsWith(".java")
				 || a.getName().toLowerCase().endsWith(".pro")
				 || a.getName().toLowerCase().endsWith(".bat")
				 || a.getName().toLowerCase().endsWith(".sh")
				 || a.getName().toLowerCase().endsWith(".js")
				 || a.getName().toLowerCase().endsWith(".php")
				 || a.getName().toLowerCase().endsWith(".html"))
		//文本文件
			Glide.with(holder.mImageView.getContext())
				.load(R.mipmap.file_txt)
				.circleCrop()
				.into(holder.mImageView);
		else if (a.getName().toLowerCase().endsWith(".apk"))
		//APK文件
			new Handler().post(new Runnable(){

					@Override
					public void run()
					{
						Drawable aa=XApkIcon.showApkIcon(thiz, a.getAbsolutePath());
						if (aa != null)
							holder.mImageView.setImageDrawable(aa);
						else
							Glide.with(holder.mImageView.getContext())
								.load(R.mipmap.file_byte)
								.circleCrop()
								.into(holder.mImageView);
					}
				});
		else if (a.getName().toLowerCase().endsWith(".zip") 
				 || a.getName().toLowerCase().endsWith(".jar")
				 || a.getName().toLowerCase().endsWith(".aar")
				 || a.getName().toLowerCase().endsWith(".rar"))
		//压缩包
			Glide.with(holder.mImageView.getContext())
				.load(R.mipmap.file_zip)
				.circleCrop()
				.into(holder.mImageView);
		else if (a.getName().toLowerCase().endsWith(".png")
				 || a.getName().toLowerCase().endsWith(".jpg")
				 || a.getName().toLowerCase().endsWith(".bmp")
				 || a.getName().toLowerCase().endsWith(".jpeg")
				 || a.getName().toLowerCase().endsWith(".webp")
				 || a.getName().toLowerCase().endsWith(".gif"))
		//图片文件
			if (isApk)
				Glide.with(holder.mImageView.getContext())
					.load(Tree.readEntryForZip(Tree.getCurPath() + mValues.get(position)))
					.circleCrop()
					.into(holder.mImageView);
			else
				Glide.with(holder.mImageView.getContext())
					.load(a)
					.circleCrop()
					.into(holder.mImageView);
		else
		//未知文件
			Glide.with(holder.mImageView.getContext())
				.load(R.mipmap.file_byte)
				.circleCrop()
				.into(holder.mImageView);
	}
	@Override
	public int getItemCount()
	{
		return mValues.size();
	}
	public static class ViewHolder extends RecyclerView.ViewHolder
	{
		public String mFile_Name;
		public View mView;
		public ImageView mImageView;
		public TextView mFileName;
		public TextView mTime;
		public ViewHolder(View view)
		{
			super(view);
			mView = view;
			mImageView = (ImageView) view.findViewById(R.id.avatar);
			mFileName = (TextView) view.findViewById(android.R.id.text1);
			mTime = (TextView) view.findViewById(R.id.time);
		}
	}

	public interface OnListItemClickListener
	{
		void Focus();
		void onMove(int pos);
		void onClicked(String fileName , int pos);
		void onLongClicked(String filename , int pos);
	}
}


