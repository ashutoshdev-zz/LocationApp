package com.mydemoapp;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends Activity implements
		SwipeActionAdapter.SwipeActionListener {
	
	ListView lv;
	protected SwipeActionAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
       setContentView(R.layout.activity_main);
       
       Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		
		/*Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		 * int width = size.x; int height = size.y;
		 
		int displayWidth = size.x;
		int displayHeight = size.y;*/
		Log.e("size", width + "===" + height);
		
       lv = (ListView) findViewById(R.id.listView1);
		String[] content = new String[1];
		for (int i = 0; i < 1; i++)
			content[i] = "Row " + (i + 1);
		ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(this,
				R.layout.row_bg, R.id.text, new ArrayList<String>(
						Arrays.asList(content)));
		mAdapter = new SwipeActionAdapter(stringAdapter);
		mAdapter.setSwipeActionListener(this).setDimBackgrounds(true)
				.setListView(lv);
		lv.setAdapter(mAdapter);

		mAdapter.addBackground(SwipeDirections.DIRECTION_FAR_LEFT,
				R.layout.row_bg_left_far)
				.addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT,
						R.layout.row_bg_left)
				.addBackground(SwipeDirections.DIRECTION_FAR_RIGHT,
						R.layout.row_bg_right_far)
				.addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT,
						R.layout.row_bg_right);
	}
	

	@Override
	public boolean hasActions(int position) {
		return true;
	}

	@Override
	public boolean shouldDismiss(int position, int direction) {
		return direction == SwipeDirections.DIRECTION_FAR_LEFT || direction == SwipeDirections.DIRECTION_FAR_RIGHT;
	}

	@Override
	public void onSwipe(int[] positionList, int[] directionList) {
		for (int i = 0; i < positionList.length; i++) {
			int direction = directionList[i];
			int position = positionList[i];
			String dir = "";

			switch (direction) {
			case SwipeDirections.DIRECTION_FAR_LEFT:
				dir = "Far left";
				break;
			case SwipeDirections.DIRECTION_NORMAL_LEFT:
				dir = "Left";
				break;
			case SwipeDirections.DIRECTION_FAR_RIGHT:
				dir = "Far right";
				break;
			case SwipeDirections.DIRECTION_NORMAL_RIGHT:
				/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Test Dialog").setMessage("You swiped right")
						.create().show();*/
				dir = "Right";
				break;
			}
			
		if(dir.equals("Far left")||dir.equals("Far right")){
			Toast.makeText(
					this,
					dir + " swipe Action triggered on "
							+ mAdapter.getItem(position), Toast.LENGTH_SHORT)
					.show();
			lv.setAdapter(null);
			finish();
			//mAdapter.notifyDataSetChanged();
		}
		}
	}
}