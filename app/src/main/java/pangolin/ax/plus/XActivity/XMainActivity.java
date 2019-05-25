package pangolin.ax.plus.XActivity;

import android.content.pm.*;
import android.graphics.*;
import android.support.design.widget.*;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.support.v4.widget.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.support.v7.widget.helper.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.reward.*;
import java.io.*;
import java.util.*;
import pangolin.ax.plus.*;
import pangolin.ax.plus.XAdapter.*;
import pangolin.ax.plus.XBase.*;
import pangolin.ax.plus.XFragMent.*;
import pangolin.ax.plus.XUtil.*;
import pangolin.ax.plus.XWidget.*;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import pangolin.ax.plus.R;
import android.util.*;
import pangolin.ax.plus.XListener.*;
import android.os.*;
import java.util.zip.*;

public class XMainActivity extends XBaseActivity
{
	private Toolbar Toolbar;
	private DrawerLayout drawer;
	private NavigationView NavigationView;
	private CoordinatorLayout root;
	private RewardedVideoAd mRewardedVideoAd;
	private XDialogAddFile Add;
	private XDialogLongClick LongClick;
	private XDialogJarAndDex JarAndDex;
	private Type Type;
	private enum Type
	{
		CardView1,
		CardView2
		}
	private File CurrentFile1;
	private CardView CardView1;
	private RecyclerView FileRecyClerView1;
	private XFileAdapter XFileAdapter1;
	private List<String> mList1=new ArrayList<String>();
	private SwipeRefreshLayout swipe1;
	private XZipList.Tree Tree1;
	private int[] CurrPos1;



	private File CurrentFile2;
	private CardView CardView2;
	private RecyclerView FileRecyClerView2;
	private XFileAdapter XFileAdapter2;
	private List<String> mList2=new ArrayList<String>();
	private SwipeRefreshLayout swipe2;
	private XZipList.Tree Tree2;
	private int[] CurrPos2;



	private static ImageView Refresh;
	private ImageView Synchronize;
	private ImageView AddFile;
	@Override
	protected int XBindXmlId()
	{
		return R.layout.activity_main;
	}
	@Override
	protected void XBindView()
	{
		ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
		Toolbar = (Toolbar) $(R.id.toolbar);
		Toolbar.setTitle(getString(R.string.app_name));
		setSupportActionBar(Toolbar);
		drawer = (DrawerLayout) $(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, Toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        NavigationView  = (NavigationView) $(R.id.nav_view1);
		root = (CoordinatorLayout) $(R.id.root);

		//初始化第一个文件列表
		CardView1 = (CardView) $(R.id.CardView1);
		FileRecyClerView1 = (RecyclerView) $(R.id.recyclerview1);
		FileRecyClerView1.setHasFixedSize(true);
		FileRecyClerView1.setLayoutManager(new LinearLayoutManager(FileRecyClerView1.getContext()));
		FileRecyClerView1.addItemDecoration(new  XRecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
		XFileAdapter1 = new XFileAdapter(this, mList1);
		FileRecyClerView1.setAdapter(XFileAdapter1);
		new ItemTouchHelper(new XRecycleItemTouchHelper(XFileAdapter1)).attachToRecyclerView(FileRecyClerView1);
		swipe1 = (SwipeRefreshLayout) $(R.id.refresh1);

		//初始化第二个文件列表
		CardView2 = (CardView) $(R.id.CardView2);
		FileRecyClerView2 = (RecyclerView) $(R.id.recyclerview2);
		FileRecyClerView2.setHasFixedSize(true);
		FileRecyClerView2.setLayoutManager(new LinearLayoutManager(FileRecyClerView2.getContext()));
		FileRecyClerView2.addItemDecoration(new  XRecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));
		XFileAdapter2 = new XFileAdapter(this, mList2);
		FileRecyClerView2.setAdapter(XFileAdapter2);
		new ItemTouchHelper(new XRecycleItemTouchHelper(XFileAdapter2)).attachToRecyclerView(FileRecyClerView2);
		swipe2 = (SwipeRefreshLayout) $(R.id.refresh2);

		Refresh = (ImageView) $(R.id.E);
		Synchronize = (ImageView) $(R.id.D);
		AddFile = (ImageView) $(R.id.C);

		//Google Ads初始化
		mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener(){

				@Override
				public void onRewardedVideoAdLoaded()
				{
					if (mRewardedVideoAd.isLoaded())
					{
						mRewardedVideoAd.show();
					}
				}

				@Override
				public void onRewardedVideoAdOpened()
				{
				}

				@Override
				public void onRewardedVideoStarted()
				{
				}

				@Override
				public void onRewardedVideoAdClosed()
				{
					loadRewardedVideoAd();
				}

				@Override
				public void onRewarded(RewardItem p1)
				{
				}

				@Override
				public void onRewardedVideoAdLeftApplication()
				{
				}

				@Override
				public void onRewardedVideoAdFailedToLoad(int p1)
				{
					ShowToast("Ads error:" + p1);
				}
			});
		loadRewardedVideoAd();

	}
	private void loadRewardedVideoAd()
	{
		mRewardedVideoAd.loadAd("ca-app-pub-9413497149279615/3643781404",
								new AdRequest.Builder().build());
	}
	public static void Refresh()
	{
		if (Looper.myLooper() == Looper.getMainLooper())
			Refresh.performClick();
		else
		{
			new Handler(Looper.getMainLooper()).post(new Runnable(){

					@Override
					public void run()
					{
						Refresh.performClick();
					}
				});
		}
	}
	@Override
	protected void XBindListener()
	{
		swipe1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

				@Override
				public void onRefresh()
				{
					Refresh.performClick();
					swipe1.setRefreshing(false);
				}
			});
		swipe2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

				@Override
				public void onRefresh()
				{
					Refresh.performClick();
					swipe2.setRefreshing(false);
				}
			});
		//添加文件/文件夹
		AddFile.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Add = new XDialogAddFile(new XDialogAddFile.CallBack(){

							@Override
							public void CallFile(String Name)
							{
								if (Type == Type.CardView1)
								{
									File newfile= new File(CurrentFile1.getAbsolutePath() + File.separator + Name);
									if (newfile.exists())
									{
										ShowToast("文件已经存在");
										return;
									}
									else
									{
										boolean flag=false;
										try
										{
											flag = newfile.createNewFile();
										}
										catch (IOException e)
										{}
										if (!flag)
											ShowToast("文件创建失败");
									}
								}
								if (Type == Type.CardView2)
								{
									File newfile= new File(CurrentFile2.getAbsolutePath() + File.separator + Name);
									if (newfile.exists())
									{
										ShowToast("文件已经存在");
										return;
									}
									else
									{
										boolean flag=false;
										try
										{
											flag = newfile.createNewFile();
										}
										catch (IOException e)
										{}
										if (!flag)
											ShowToast("文件创建失败");
									}
								}
								Refresh.performClick();
							}

							@Override
							public void CallDir(String Name)
							{
								if (Type == Type.CardView1)
								{
									File newfile= new File(CurrentFile1.getAbsolutePath() + File.separator + Name);
									if (newfile.exists())
									{
										ShowToast("文件夹已经存在");
										return;
									}
									else
									{
										boolean flag=newfile.mkdirs();
										if (!flag)
											ShowToast("文件夹创建失败");
									}
								}
								if (Type == Type.CardView2)
								{
									File newfile= new File(CurrentFile2.getAbsolutePath() + File.separator + Name);
									if (newfile.exists())
									{
										ShowToast("文件夹已经存在");
										return;
									}
									else
									{
										boolean flag=newfile.mkdirs();
										if (!flag)
											ShowToast("文件夹创建失败");
									}
								}
								Refresh.performClick();
							}
						});
					Add.show(getSupportFragmentManager(), "XAddFile");
				}
			});
		//同步
		Synchronize.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					if (Type == Type.CardView1)
					{
						CurrentFile2 = CurrentFile1;
						XFileList.GetFile(CurrentFile1, mList2, XFileAdapter2);
						int[] pos=XRecycleViewSub.getRecyclerViewLastPosition1(FileRecyClerView1, (LinearLayoutManager)FileRecyClerView1.getLayoutManager(), XFileAdapter1);
						((LinearLayoutManager)FileRecyClerView2.getLayoutManager()).scrollToPositionWithOffset(pos[0], pos[1]);
					}
					if (Type == Type.CardView2)
					{
						CurrentFile1 = CurrentFile2;
						XFileList.GetFile(CurrentFile2, mList1, XFileAdapter1);
						int[] pos=XRecycleViewSub.getRecyclerViewLastPosition1(FileRecyClerView2, (LinearLayoutManager)FileRecyClerView2.getLayoutManager(), XFileAdapter2);
						((LinearLayoutManager)FileRecyClerView1.getLayoutManager()).scrollToPositionWithOffset(pos[0], pos[1]);
					}
				}
			});
		//刷新
		Refresh.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					if (Type == Type.CardView1)
					{
						XFileList.GetFile(CurrentFile1, mList1, XFileAdapter1);
					}
					if (Type == Type.CardView2)
					{
						XFileList.GetFile(CurrentFile2, mList2, XFileAdapter2);
					}
				}
			});
		FileRecyClerView1.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View p1, MotionEvent p2)
				{
					CardView1.setCardElevation(100);
					CardView2.setCardElevation(0);
					Type = Type.CardView1;
					return false;
				}
			});
		FileRecyClerView2.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View p1, MotionEvent p2)
				{
					CardView2.setCardElevation(100);
					CardView1.setCardElevation(0);
					Type = Type.CardView2;
					return false;
				}
			});
		XFileAdapter1.setOnListItemClickListener(new XFileAdapter.OnListItemClickListener(){

				@Override
				public void Focus()
				{
					CardView1.setCardElevation(100);
					CardView2.setCardElevation(0);
					Type = Type.CardView1;
				}

				@Override
				public void onMove(int pos)
				{
					if (!XFileAdapter1.isSelected.get(pos))
					{
						XFileAdapter1.isSelected.put(pos, true);
					}
					else
					{
						XFileAdapter1.isSelected.put(pos, false);
					}
					XFileAdapter1.notifyItemChanged(pos);
				}

				@Override
				public void onClicked(String fileName, int pos)
				{
					//判断是否返回上一层
					if (pos == 0)
					{
						if (XFileAdapter1.isApk)
						{
							if (Tree1.pop() == null)
							{
								XFileList.GetFile(CurrentFile1, mList1, XFileAdapter1);
								return;
							}
							XZipList.Pop(mList1, Tree1, XFileAdapter1);
							return;
						}
						if (!mList1.get(0).equals("/"))
						{
							CurrentFile1 = new File(mList1.get(0));
							XFileList.GetFile(CurrentFile1, mList1, XFileAdapter1);
						}
						if (CurrPos1 != null)
							((LinearLayoutManager)FileRecyClerView1.getLayoutManager()).scrollToPositionWithOffset(CurrPos1[0], CurrPos1[1]);
					}
					else
					{
						//判断是否多选
						if (XFileAdapter1.isSelected.containsValue(true))
						{
							if (!XFileAdapter1.isSelected.get(pos))
								XFileAdapter1.isSelected.put(pos, true);
							else
								XFileAdapter1.isSelected.put(pos, false);
							XFileAdapter1.notifyItemChanged(pos);
							return;
						}
						//判断是否是文件夹
						else if (new File(mList1.get(pos)).isDirectory() || (XFileAdapter1.isApk && mList1.get(pos).endsWith("/")))
						{
							CurrPos1 = XRecycleViewSub.getRecyclerViewLastPosition1(FileRecyClerView1, (LinearLayoutManager)FileRecyClerView1.getLayoutManager(), XFileAdapter1);
							if (XFileAdapter1.isApk)
							{
								XZipList.Push(mList1, fileName, Tree1, XFileAdapter1);
								return;
							}
							CurrentFile1 = new File(mList1.get(pos));
							XFileList.GetFile(CurrentFile1, mList1, XFileAdapter1);
						}
						//判断是否是dex文件
						else if (mList1.get(pos).toLowerCase().endsWith(".dex"))
						{
							File Dex=new File(mList1.get(pos));
							JarAndDex = new XDialogJarAndDex();
							JarAndDex.show(getSupportFragmentManager(), "JarAndDex");
							JarAndDex.BindListener(new XJarAndDexBindListener(XMainActivity.this, Dex));
						}
						//判断是否是apk
						else if (mList1.get(pos).toLowerCase().endsWith(".apk")
								 || mList1.get(pos).toLowerCase().endsWith(".zip"))
						{
							try
							{
								Tree1 = XZipList.GetList(mList1, mList1.get(pos), XFileAdapter1);
							}
							catch (IOException e)
							{
								XAlertDialog.ShowTraceDialog(XMainActivity.this, Log.getStackTraceString(e));
								XFileAdapter1.setModeApk(false);
								Refresh.performClick();
							}
						}
					}
				}

				@Override
				public void onLongClicked(String filename, final int pos)
				{
					if (pos == 0)return;
					LongClick = new XDialogLongClick();
					LongClick.show(getSupportFragmentManager(), "XLongClick1");
					LongClick.Suffixes(filename);
					LongClick.BindListener(new XLongClickBindListener(XMainActivity.this, new File(mList1.get(pos)), XFileAdapter1));
				}
			});
		XFileAdapter2.setOnListItemClickListener(new XFileAdapter.OnListItemClickListener(){

				@Override
				public void Focus()
				{
					CardView2.setCardElevation(100);
					CardView1.setCardElevation(0);
					Type = Type.CardView2;
				}

				@Override
				public void onMove(int pos)
				{
					if (!XFileAdapter2.isSelected.get(pos))
					{
						XFileAdapter2.isSelected.put(pos, true);
					}
					else
					{
						XFileAdapter2.isSelected.put(pos, false);
					}
					XFileAdapter2.notifyItemChanged(pos);
				}

				@Override
				public void onClicked(String fileName, int pos)
				{
					if (pos == 0)
					{
						if (XFileAdapter2.isApk)
						{
							if (Tree2.pop() == null)
							{
								XFileList.GetFile(CurrentFile2, mList2, XFileAdapter2);
								return;
							}
							XZipList.Pop(mList2, Tree2, XFileAdapter2);
							return;
						}
						if (!mList2.get(0).equals("/"))
						{
							CurrentFile2 = new File(mList2.get(0));
							XFileList.GetFile(CurrentFile2, mList2, XFileAdapter2);
						}
						if (CurrPos2 != null)
							((LinearLayoutManager)FileRecyClerView2.getLayoutManager()).scrollToPositionWithOffset(CurrPos2[0], CurrPos2[1]);
					}
					else
					{
						if (XFileAdapter2.isSelected.containsValue(true))
						{
							if (!XFileAdapter2.isSelected.get(pos))
								XFileAdapter2.isSelected.put(pos, true);
							else
								XFileAdapter2.isSelected.put(pos, false);
							XFileAdapter2.notifyItemChanged(pos);
						}
						else if (new File(mList2.get(pos)).isDirectory()|| (XFileAdapter2.isApk && mList2.get(pos).endsWith("/")))
						{
							CurrPos2 = XRecycleViewSub.getRecyclerViewLastPosition1(FileRecyClerView2, (LinearLayoutManager)FileRecyClerView2.getLayoutManager(), XFileAdapter2);
							if (XFileAdapter2.isApk)
							{
								XZipList.Push(mList2, fileName, Tree2, XFileAdapter2);
								return;
							}
							CurrentFile2 = new File(mList2.get(pos));
							XFileList.GetFile(CurrentFile2, mList2, XFileAdapter2);
						}
						else if (mList2.get(pos).toLowerCase().endsWith(".dex"))
						{
							File Dex=new File(mList1.get(pos));
							JarAndDex = new XDialogJarAndDex();
							JarAndDex.show(getSupportFragmentManager(), "JarAndDex");
							JarAndDex.BindListener(new XJarAndDexBindListener(XMainActivity.this, Dex));
						}
						else if (mList2.get(pos).toLowerCase().endsWith(".apk")
								 || mList2.get(pos).toLowerCase().endsWith(".zip"))
						{
							try
							{
								Tree2 = XZipList.GetList(mList2, mList2.get(pos), XFileAdapter2);
							}
							catch (IOException e)
							{
								XAlertDialog.ShowTraceDialog(XMainActivity.this, Log.getStackTraceString(e));
								XFileAdapter2.setModeApk(false);
								Refresh.performClick();
							}
						}
					}
				}

				@Override
				public void onLongClicked(String filename, final int pos)
				{
					if (pos == 0)return;
					LongClick = new XDialogLongClick();
					LongClick.show(getSupportFragmentManager(), "XLongClick2");
					LongClick.Suffixes(filename);
					LongClick.BindListener(new XLongClickBindListener(XMainActivity.this, new File(mList2.get(pos)), XFileAdapter2));
				}
			});
	}

	@Override
	public void onBackPressed()
	{
		if (drawer.isDrawerOpen(GravityCompat.START))
		{
            drawer.closeDrawer(GravityCompat.START);
			return;
        }
		else if (drawer.isDrawerOpen(GravityCompat.END))
		{
            drawer.closeDrawer(GravityCompat.END);
			return;
        }
		if (Type == Type.CardView1)
		{
			if (XFileAdapter1.isApk)
			{
				if (Tree1.pop() == null)
				{
					XFileList.GetFile(CurrentFile1, mList1, XFileAdapter1);
					return;
				}
				XZipList.Pop(mList1,Tree1,XFileAdapter1);
				if (CurrPos1 != null)
					((LinearLayoutManager)FileRecyClerView1.getLayoutManager()).scrollToPositionWithOffset(CurrPos1[0], CurrPos1[1]);
				return;
			}
			if (!mList1.get(0).equals("/"))
			{
				CurrentFile1 = new File(mList1.get(0));
				XFileList.GetFile(CurrentFile1, mList1, XFileAdapter1);
				if (CurrPos1 != null)
					((LinearLayoutManager)FileRecyClerView1.getLayoutManager()).scrollToPositionWithOffset(CurrPos1[0], CurrPos1[1]);
				return;
			}
		}
		if (Type == Type.CardView2)
		{
			if (XFileAdapter2.isApk)
			{
				if (Tree2.pop() == null)
				{
					XFileList.GetFile(CurrentFile2, mList2, XFileAdapter2);
					return;
				}
				XZipList.Pop(mList2,Tree2,XFileAdapter2);
				if (CurrPos2 != null)
					((LinearLayoutManager)FileRecyClerView2.getLayoutManager()).scrollToPositionWithOffset(CurrPos2[0], CurrPos2[1]);
				return;
			}
			if (!mList2.get(0).equals("/"))
			{
				CurrentFile2 = new File(mList2.get(0));
				XFileList.GetFile(CurrentFile2, mList2, XFileAdapter2);
				if (CurrPos2 != null)
					((LinearLayoutManager)FileRecyClerView2.getLayoutManager()).scrollToPositionWithOffset(CurrPos2[0], CurrPos2[1]);
				return;
			}
		}
		Snackbar.make(root, "退出应用?", Snackbar.LENGTH_LONG).setAction("Yes", new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					finish();
					System.exit(0);
				}
			}).show();
	}
	//权限请求回调
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 100)
		{
			if (grantResults.length > 0)
			{
                List<String> deniedPermissions = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++)
				{
                    int result = grantResults[i];
                    if (result != PackageManager.PERMISSION_GRANTED)
					{
                        String permission = permissions[i];
                        deniedPermissions.add(permission);
                    }
					if (!deniedPermissions.isEmpty())
					{
						ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
					}
					else
					{
						CurrentFile1 = new File("/sdcard/");
						CurrentFile2 = new File("/sdcard/");
						XFileList.GetFile(CurrentFile1, mList1, XFileAdapter1);
						XFileList.GetFile(CurrentFile2, mList2, XFileAdapter2);
					}
                }
			}
		}
	}
	@Override
	public void onResume()
	{
		Refresh.performClick();
		mRewardedVideoAd.resume(this);
		super.onResume();
	}

	@Override
	public void onPause()
	{
		mRewardedVideoAd.pause(this);
		super.onPause();
	}

	@Override
	public void onDestroy()
	{
		mRewardedVideoAd.destroy(this);
		super.onDestroy();
	}
}
