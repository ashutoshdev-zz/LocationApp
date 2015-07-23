package com.mycam;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.client.params.ClientPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cam.imagedatabase.DBHelper;
import com.cam.imagedatabase.PublishListAdapter;
import com.image.effects.HorizontalListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zakoopi.R;
import com.zakoopi.activity.MainActivity;
import com.zakoopi.helper.ProgressHUD;

public class Lookbookpublish extends FragmentActivity implements
		OnCancelListener {

	HorizontalListView imagelist;
	ArrayList<byte[]> imageArry = new ArrayList<byte[]>();
	PublishListAdapter adapter;
	Button publish;
	EditText search_title;
	// ToggleButton fb, tw, gp;
	JSONArray js;
	private SQLiteDatabase db;
	public static final String DBTABLE = "lookbook";
	long count;
	DBHelper hp;
	long idd;
	ArrayList<File> files = new ArrayList<File>();

	static AsyncHttpClient client = new AsyncHttpClient();
	final static int DEFAULT_TIMEOUT = 40 * 1000;
	ProgressDialog bar;
	// private static final String LOOKBOOK_URL =
	// "http://v3.zakoopi.com/api/lookbooks/add.json";
	private static final String LOOKBOOK_URL = "http://zakoopi.com/api/lookbooks/add.json";
	ProgressHUD mProgressHUD;
	String title;
	JSONObject obj;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lookbook_publish);

		imagelist = (HorizontalListView) findViewById(R.id.listview);
		publish = (Button) findViewById(R.id.imageView1);
		search_title = (EditText) findViewById(R.id.edt_title);
		/*
		 * fb = (ToggleButton) findViewById(R.id.ToggleButton02); tw =
		 * (ToggleButton) findViewById(R.id.ToggleButton01); gp = (ToggleButton)
		 * findViewById(R.id.toggleButton1);
		 */

		// Reading all contacts from database

		try {
			hp = new DBHelper(Lookbookpublish.this);
			db = hp.getWritableDatabase();
			Cursor c = db.rawQuery(" select id,photo,tag,desc from " + DBTABLE,
					null);
			count = c.getCount();
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						int id = c.getInt(c.getColumnIndex("id"));
						String st = c.getString(c.getColumnIndex("tag"));
						String st1 = c.getString(c.getColumnIndex("desc"));
						byte[] arr = c.getBlob(c.getColumnIndex("photo"));
						imageArry.add(arr);
					//	Log.e("id", id + ":::" + st + ":::" + st1);
					} while (c.moveToNext());
				}
			}
		} catch (SQLiteException s) {
			s.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		adapter = new PublishListAdapter(Lookbookpublish.this, imageArry);
		imagelist.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//Log.e("hhhhh", "here");
				title = search_title.getText().toString().trim();
				if (title.length() > 0) {

					obj = getData(title);
					upload(obj);
					DBHelper hp = new DBHelper(Lookbookpublish.this);
					db = hp.getWritableDatabase();
					// search_title.setText("");
					// db.delete(DBTABLE, null, null);
				} else {
					search_title.setError("Please write LookBook Title");
				}

			}
		});

		search_title.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub

				search_title.setError(null);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public JSONObject getData(String title) {

		JSONArray jsonArray = new JSONArray();
		JSONObject finalobject = new JSONObject();
		try {
			hp = new DBHelper(Lookbookpublish.this);
			db = hp.getWritableDatabase();
			Cursor c = db.rawQuery(" select id,photo,tag,desc,imagepath from "
					+ DBTABLE, null);
			count = c.getCount();

			if (c != null) {
				while (c.moveToNext()) {
					JSONObject obj = new JSONObject();
					int id = c.getInt(c.getColumnIndex("id"));
					String st = c.getString(c.getColumnIndex("tag"));
					String st1 = c.getString(c.getColumnIndex("desc"));
					String path = c.getString(c.getColumnIndex("imagepath"));
					byte[] arr = c.getBlob(c.getColumnIndex("photo"));
					files.add(new File(path));
					try {
						obj.put("id", id);
						obj.put("storetag", st);
						obj.put("description", st1);
						obj.put("photo_path", path);
						jsonArray.put(obj);
					//	Log.e("array", jsonArray.toString());

						// Log.e("hhhhhdd", obj + "");

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				try {

					JSONObject myobj = new JSONObject();
					myobj.put("title", title);
					myobj.put("user_id", "2879");
					myobj.put("cards", jsonArray);

					finalobject.put("lookbook", myobj);
					// Log.e("hhhh", finalobject.toString());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SQLiteException s) {
			s.printStackTrace();
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return finalobject;

	}

	public void upload(final JSONObject arr) {
		client.setBasicAuth("a.himanshu.verma@gmail.com", "dragonvrmxt2t");
		client.getHttpClient().getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
		client.setTimeout(DEFAULT_TIMEOUT);
		RequestParams params = new RequestParams();
		params.setForceMultipartEntityContentType(true);
		params.put("jsonData", arr);

		try {
			for (int i = 0; i < files.size(); i++) {
				params.put("files[" + files.get(i).getName() + "]",
						files.get(i));
			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		client.post(LOOKBOOK_URL, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				// called before request is started
				//Log.e("sss", "start");
				mProgressHUD = ProgressHUD.show(Lookbookpublish.this,
						"Processing...", true, true, Lookbookpublish.this);
				mProgressHUD.setCancelable(false);
				/*
				 * bar = new ProgressDialog(Lookbookpublish.this);
				 * bar.setMessage("Wait for some moments...");
				 * bar.setIndeterminate(true); bar.setCancelable(false);
				 * bar.show();
				 */
				// customDailog();
				// new MessagePooling().execute();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] response) {
				// called when response HTTP status is "200 OK"
				//Log.e("jjj", "success");
				customDailog();
				// new MessagePooling().execute();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] errorResponse, Throwable e) {
				// called when response HTTP status is "4XX" (eg. 401, 403, 404)
				//Log.e("falied", e.getMessage() + "");
				mProgressHUD.dismiss();
			}

			@Override
			public void onRetry(int retryNo) {
				// called when request is retried
			}
		});

	}

	public void customDailog() {

		final Dialog dd = new Dialog(Lookbookpublish.this);
		dd.setCancelable(false);
		dd.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dd.setCancelable(false);
		dd.setContentView(R.layout.custom_dialog);
		dd.show();
		 mProgressHUD.dismiss();
		final ImageView img1 = (ImageView) dd.findViewById(R.id.imageView1);
		final ImageView img2 = (ImageView) dd.findViewById(R.id.imageView2);

		img1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent in = new Intent(Lookbookpublish.this,
						LookBookTabsActivity.class);
				startActivity(in);
				dd.dismiss();
				finish();
			}
		});

		img2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent feed = new Intent(Lookbookpublish.this,
						MainActivity.class);
				feed.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(feed);
				dd.dismiss();
				finish();
			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		finish();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		mProgressHUD.dismiss();
	}

}
