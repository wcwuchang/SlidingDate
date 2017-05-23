package main.com.slidingdate;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CalendarAdapter extends BaseAdapter {
	
	private List<Integer> mdaylist;
	private Context mcontext;
	private int mintClick;//记录选中标记

	public CalendarAdapter(Context context){
		this.mcontext=context;
	}
	
	public void setData(List<Integer> mdaylist, int click){
		this.mdaylist=mdaylist;
		this.mintClick=click;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mdaylist==null?0:mdaylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mdaylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=null;
		if(convertView==null){
			holder=new Holder();
			convertView= LayoutInflater.from(mcontext).inflate(R.layout.layout_item_calendar, null);
			holder.mdate=(TextView)convertView.findViewById(R.id.tv_adapter_date);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		
		holder.mdate.setText(String.valueOf(mdaylist.get(position)));
		if(getItemId(position)==mintClick){
			holder.mdate.setBackgroundResource(R.drawable.circle_message);
			holder.mdate.setTextColor(Color.WHITE);
		}else{
			holder.mdate.setBackgroundColor(0);
			holder.mdate.setTextColor(Color.BLACK);
		}
		
		return convertView;
	}
	
	class Holder{
		TextView mdate;
	}

	
}
