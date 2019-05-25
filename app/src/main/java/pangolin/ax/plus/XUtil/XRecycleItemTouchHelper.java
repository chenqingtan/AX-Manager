package pangolin.ax.plus.XUtil;


import android.content.res.*;
import android.graphics.*;
import android.support.v7.widget.*;
import android.support.v7.widget.helper.*;
import android.util.*;
import android.view.*;


public class XRecycleItemTouchHelper extends ItemTouchHelper.Callback
{
	private final ItemTouchHelperCallback helperCallback;

	public XRecycleItemTouchHelper ( ItemTouchHelperCallback helperCallback )
	{
		this.helperCallback = helperCallback;
	}
	@Override
	public int getMovementFlags ( RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder )
	{
		return makeMovementFlags ( 0, ItemTouchHelper.START | ItemTouchHelper.END );
	}
	
	@Override
	public boolean isLongPressDragEnabled ( )
	{
		return false;
	}
	
	@Override
	public boolean isItemViewSwipeEnabled ( )
	{
		return super.isItemViewSwipeEnabled();
	}
	@Override
	public boolean onMove ( RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target )
	{
		return false;
	}
	
	@Override
	public void onSwiped ( RecyclerView.ViewHolder viewHolder, int direction )
	{
		helperCallback.onItemDelete ( viewHolder.getAdapterPosition ( ) );
	}
	@Override
	public void onSelectedChanged ( RecyclerView.ViewHolder viewHolder, int actionState )
	{
		super.onSelectedChanged ( viewHolder, actionState );
	}
	public interface ItemTouchHelperCallback
	{
		void onItemDelete ( int positon );
	}
	@Override
	public void clearView ( RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder )
	{
		super.clearView ( recyclerView, viewHolder );
		viewHolder.itemView.setScrollX ( 0 );
	}

	@Override
	public void onChildDraw ( Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive )
	{
		if(viewHolder.getAdapterPosition()==0)
		{
			return;
		}
		super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
	}
	public int getSlideLimitation ( RecyclerView.ViewHolder viewHolder )
	{
		ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
		return viewGroup.getChildAt ( 1 ).getLayoutParams ( ).width;
	}
}


