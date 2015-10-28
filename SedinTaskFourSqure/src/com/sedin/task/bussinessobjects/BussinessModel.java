package com.sedin.task.bussinessobjects;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

public class BussinessModel extends Application {




	private Activity ctx;
	private ArrayList<VenueDetails> mList;
	

	public Activity getCtx() {
		return ctx;
	}


	public void setCtx(Activity ctx) {
		this.ctx = ctx;
	}


	public ArrayList<VenueDetails> getmList() {
		return mList;
	}


	public void setmList(ArrayList<VenueDetails> nList) {
		ArrayList<VenueDetails> mDetails;	
		for (int i = 0; i < nList.size(); i++) {
			mList.add(nList.get(i));
		}
		;
	}


	public void setContext(Activity ctx) {
		this.ctx = ctx;
		mList = new ArrayList<>();
		// mDbUtil = new
	}

	
		
}
