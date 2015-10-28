package com.sedin.task.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sedin.task.bussinessobjects.VenueDetails;
import com.sedin.task.cinterface.CallBack;
import com.sedin.task.view.R;

public class VenuAdapter extends BaseAdapter {
	public Context context;

	private LayoutInflater minflates;
	private ArrayList<VenueDetails> mList;
	private CallBack mBack;
	boolean isfavorite;

	public VenuAdapter(Context mContext, ArrayList<VenueDetails> mList,
			CallBack mBack, boolean isfavorite) {
		minflates = LayoutInflater.from(mContext);
		this.mList = mList;
		this.mBack = mBack;
		this.isfavorite = isfavorite;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int pos, View v, ViewGroup arg2) {
		VenueDetails mVenue = mList.get(pos);
		v = minflates.inflate(R.layout.row_layout, null);
		((TextView) v.findViewById(R.id.venuName)).setText(mVenue.getName());
		((TextView) v.findViewById(R.id.location)).setText(mVenue.getAddress()
				+ "," + mVenue.getCity());
		final ImageView mView = (ImageView) v.findViewById(R.id.favorateIcon);
		
		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if ( !isfavorite) {
				
				if (mList.get(pos).isFavorate()) {
					mView.setImageResource(R.drawable.favorites_pressed);
					mList.get(pos).setFavorate(false);
					mBack.setListion(mList.get(pos).getName(), false);
				} else {
					mView.setImageResource(R.drawable.favorites_normal);
					mList.get(pos).setFavorate(true);
					mBack.setListion(mList.get(pos).getName(), true);
				}
				}
				//mView.setImageResource(R.drawable.favorites_pressed);

			}
		});
		if ( isfavorite) {
			mView.setImageResource(R.drawable.favorites_pressed);
		}else{
		if (mVenue.isFavorate()) {
			mView.setImageResource(R.drawable.favorites_pressed);
		} else {
			mView.setImageResource(R.drawable.favorites_normal);
		}
		}
		return v;
	}

	interface Callback {
		public void setClick(int i);
	}
}
