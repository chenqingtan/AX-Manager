package pangolin.ax.plus.XUtil;

import android.support.v7.widget.*;
import android.view.*;
import pangolin.ax.plus.XAdapter.*;
public class XRecycleViewSub
{
	//获取recycleview高度
	public static int[] getRecyclerViewLastPosition1 ( RecyclerView mRv, LinearLayoutManager layoutManager,XFileAdapter mFileListAdapter)
	{
		int[] pos = new int[2];
		pos [ 0 ] = layoutManager.findFirstCompletelyVisibleItemPosition ( );
		OrientationHelper orientationHelper = OrientationHelper.createOrientationHelper ( layoutManager, OrientationHelper.VERTICAL );
		int fromIndex = 0;
		int toIndex = mFileListAdapter.getItemCount ( );
		final int start = orientationHelper.getStartAfterPadding ( );
		final int end = orientationHelper.getEndAfterPadding ( );
		final int next = toIndex > fromIndex ?1 : -1;
		for ( int i = fromIndex; i != toIndex; i += next )
		{
			final View child = mRv.getChildAt ( i );
			final int childStart = orientationHelper.getDecoratedStart ( child );
			final int childEnd = orientationHelper.getDecoratedEnd ( child );
			if ( childStart < end && childEnd > start )
			{
				if ( childStart >= start && childEnd <= end )
				{
					pos [ 1 ] = childStart;
					return pos;
				}
			}
		}
		return pos;
	}
}

