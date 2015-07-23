package com.mycam;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cam.imagedatabase.DBHelper;
import com.image.effects.HorizontalListView;
import com.zakoopi.R;
import com.zakoopi.helper.ProgressHUD;
import com.zakoopi.helper.Variables;

public class ImageDetail extends FragmentActivity {

	ImageView detail_image;
	Button addmore;
	HorizontalListView imagelist;
	ArrayList<byte[]> imgbyte = new ArrayList<byte[]>();
	ArrayList<String> myidd = new ArrayList<String>();
	ImageAdapter adapter;
	EditText tag_name;
	MultiAutoCompleteTextView textcomplete;
	private Bitmap bitmap, bitmap1;
	public static String addmoreimg = "noone";
	RelativeLayout rel_error;

	ArrayList<String> storenames = new ArrayList<String>();
	private SQLiteDatabase db;
	public static final String DBTABLE = "lookbook";
	public static final String DBTABLE1 = "Stores";
	private SQLiteStatement stm;
	long count;
	long idd;
	String imgpath;
	byte imageInByte[];
	

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_detail);

		/**
		 * Save Lookbook data in database
		 */
		DBHelper hp = new DBHelper(this);
		db = hp.getWritableDatabase();
		stm = db.compileStatement("insert into  " + DBTABLE
				+ " (photo,tag,desc,imagepath) values (?, ?, ?, ?)");

		rel_error = (RelativeLayout) findViewById(R.id.error);
		addmore = (Button) findViewById(R.id.button1);
		imagelist = (HorizontalListView) findViewById(R.id.listview);
		detail_image = (ImageView) findViewById(R.id.imageView2);
		tag_name = (EditText) findViewById(R.id.edt_description);
		textcomplete = (MultiAutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.storenames, R.id.textView1, storenames);
		textcomplete.setAdapter(adapter);
		textcomplete.setTextColor(Color.BLACK);
		textcomplete
				.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		textcomplete.setThreshold(3);

		new MessagePooling().execute();

		textcomplete.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				rel_error.setVisibility(View.GONE);
				textcomplete.setBackgroundResource(R.drawable.storename_box);
				addmoreimg = "none";
			}
		});

		tag_name.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				tag_name.setError(null);
				addmoreimg = "none";
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		((ImageView) findViewById(R.id.imageView1))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub

						if (count <= 0) {
							addmoreimg = "addmore";
							Intent in = new Intent(ImageDetail.this,
									LookBookTabsActivity.class);
							startActivity(in);
							finish();

						} else {
							String st1 = textcomplete.getText().toString()
									.trim();
							String st2 = tag_name.getText().toString();

							if (textcomplete.getText().toString().equals("")) {
								rel_error.setVisibility(View.VISIBLE);
								textcomplete
										.setBackgroundResource(R.drawable.error_drawable);
							} else {
								if (tag_name.getText().toString().equals("")) {
									tag_name.setError("Opps! seems like you forgot to write discription.");
								} else {

									if (!addmoreimg.equals("addmore")) {

										ContentValues cv = new ContentValues();
										cv.put("id", idd);
										cv.put("photo", imageInByte);
										cv.put("tag", st1);
										cv.put("desc", st2);
										cv.put("imagepath", imgpath);

										db.update(DBTABLE, cv, "id " + " = "
												+ idd, null);

									}
									Intent in = new Intent(ImageDetail.this,
											Lookbookpublish.class);

									startActivity(in);
								}
							}
						}
					}
				});

		// Reading all contacts from database

		addmore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			//	Log.e("coung", count + "");
				if (count <= 0) {
					addmoreimg = "addmore";
					Intent in = new Intent(ImageDetail.this,
							LookBookTabsActivity.class);
					startActivity(in);
					finish();

				} else {
					String st1 = textcomplete.getText().toString().trim();

					String st2 = tag_name.getText().toString();
					if (textcomplete.getText().toString().equals("")) {
						rel_error.setVisibility(View.VISIBLE);
						textcomplete
								.setBackgroundResource(R.drawable.error_drawable);
					} else {
						if (tag_name.getText().toString().equals("")) {
							tag_name.setError("Opps! seems like you forgot to write discription.");
						} else {

							ContentValues cv = new ContentValues();
							cv.put("id", idd);
							cv.put("photo", imageInByte);
							cv.put("tag", st1);
							cv.put("desc", st2);
							cv.put("imagepath", imgpath);

							db.update(DBTABLE, cv, "id " + " = " + idd, null);

							if (count < 6) {
								addmoreimg = "addmore";
								Intent in = new Intent(ImageDetail.this,
										LookBookTabsActivity.class);
								startActivity(in);
								finish();
							} else {

								Toast.makeText(ImageDetail.this,
										"Limit is compeleted", 5000).show();

							}
						}
					}
				}

			}
		});
	}

	public class BlurImagesTask extends AsyncTask<Bitmap, Void, Bitmap> {

		ImageView roundedImageView;

		public BlurImagesTask(ImageView imageView) {

			this.roundedImageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(Bitmap... param) {
			Bitmap bitmap = param[0];

			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);

			// roundedImageView.setImageBitmap(result);

			try {
				DBHelper hp = new DBHelper(ImageDetail.this);
				db = hp.getWritableDatabase();
				Cursor c = db.rawQuery(" select id,photo,tag,desc from "
						+ DBTABLE, null);
				count = c.getCount();
				if (c != null) {
					if (c.moveToFirst()) {
						do {
							int id = c.getInt(c.getColumnIndex("id"));
							byte[] arr = c.getBlob(c.getColumnIndex("photo"));
							imgbyte.add(arr);
							myidd.add(String.valueOf(id));
						//	Log.e("id", id + "");
						} while (c.moveToNext());
					}
				}
			} catch (SQLiteException s) {
				s.printStackTrace();
			}

			adapter = new ImageAdapter(ImageDetail.this, imgbyte, myidd);
			imagelist.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		}
	}

	public void storeData() {
		DBHelper hp = new DBHelper(this);
		db = hp.getWritableDatabase();
		storenames.clear();
		try {
			Cursor c = db.rawQuery(" select market from " + DBTABLE1, null);

			if (c != null) {
				if (c.moveToFirst()) {
					do {

						String data1 = c.getString(c.getColumnIndex("market"));

						storenames.add(data1);

					} while (c.moveToNext());
				}
			}
		} catch (SQLiteException s) {
			s.printStackTrace();
		}

	}

	public class ImageAdapter extends BaseAdapter {
		ArrayList<byte[]> imglist = new ArrayList<byte[]>();
		Context ctyx;
		LayoutInflater inf;
		ArrayList<String> id = new ArrayList<String>();
		private SQLiteDatabase db;
		public static final String DBTABLE = "lookbook";

		public ImageAdapter(Context ctx, ArrayList<byte[]> list,
				ArrayList<String> id) {
			this.ctyx = ctx;
			imglist = list;
			this.id = id;
			inf = (LayoutInflater) ctyx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imglist.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			View view = arg1;
			if (view == null) {
				holder = new ViewHolder();
				view = inf.inflate(R.layout.image_detail_list_item, null);
				holder.img = (ImageView) view.findViewById(R.id.imageView1);
				holder.del = (ImageView) view.findViewById(R.id.imageView2);
				view.setTag(holder);

			} else {

				holder = (ViewHolder) view.getTag();
			}

			holder.del.setTag(arg0);
			byte[] outImage = imglist.get(arg0);
			ByteArrayInputStream imageStream = new ByteArrayInputStream(
					outImage);
			final Bitmap theImage = BitmapFactory.decodeStream(imageStream);

			if (arg0 == imglist.size() - 1) {

				holder.img.setBackgroundColor(Color.parseColor("#f1c40f"));
				new BlurImagesTask(arg0, holder.img).execute(theImage);

			} else {

				holder.img.setBackgroundColor(Color.parseColor("#000000"));
				new BlurImagesTask(arg0, holder.img).execute(theImage);
			}

			holder.del.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Integer index = (Integer) arg0.getTag();
					if ((int) index == imglist.size() - 1) {

						detail_image.setImageDrawable(null);

					}

					imglist.remove(index.intValue());
					DBHelper hp = new DBHelper(ctyx);
					db = hp.getWritableDatabase();
					db.delete(DBTABLE, "id = " + id.get(index), null);
					count--;
					notifyDataSetChanged();
				}
			});

			return view;
		}

		class ViewHolder {

			ImageView img;
			ImageView del;
		}

		public class BlurImagesTask extends AsyncTask<Bitmap, Void, Bitmap> {

			private ImageView roundedImageView;
			int position;

			public BlurImagesTask(int pos, ImageView imageView) {
				this.position = pos;

				this.roundedImageView = imageView;
			}

			@Override
			protected Bitmap doInBackground(Bitmap... param) {
				Bitmap bitmap = param[0];

				return bitmap;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);

				roundedImageView.setImageBitmap(result);
				detail_image.setImageBitmap(result);
			}

		}

	}

	public class MessagePooling extends AsyncTask<Void, String, Void> implements OnCancelListener {

	//	ProgressDialog bar;
		ProgressHUD mProgressHUD;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgressHUD = ProgressHUD.show(ImageDetail.this,"Processing...", true,true,this);
			mProgressHUD.setCancelable(false);
			/*bar = new ProgressDialog(ImageDetail.this);
			bar.setMessage("Wait for some moments...");
			bar.setIndeterminate(true);
			bar.setCancelable(false);
			bar.show();*/

		}

		@Override
		protected Void doInBackground(Void... params) {

			FileOutputStream outStream = null;
			File filePath = Environment.getExternalStorageDirectory();
			File dir = new File(filePath.getAbsolutePath() + "/Zakoopi");
			dir.mkdirs();

			File outFile = new File(dir, "Pic" + System.currentTimeMillis()
					+ ".jpg");

			try {
				outStream = new FileOutputStream(outFile);
				outStream.write(Variables.imgarr);
				outStream.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// getting image path from sdcard
			imgpath = outFile.getAbsolutePath();

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			bitmap = ThumbnailUtils.extractThumbnail(BitmapFactory
					.decodeByteArray(Variables.imgarr, 0,
							Variables.imgarr.length), 100, 100, 0);
			bitmap1 = BitmapFactory.decodeByteArray(Variables.imgarr, 0,
					Variables.imgarr.length);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
			imageInByte = stream.toByteArray();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			stm.bindBlob(1, imageInByte);
			stm.bindString(2, "tag");
			stm.bindString(3, "desc");
			stm.bindString(4, imgpath);
			stm.executeInsert();

			new BlurImagesTask(detail_image).execute(bitmap);
			storeData();
			mProgressHUD.dismiss();
			//bar.dismiss(); 
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			this.cancel(true);
			mProgressHUD.dismiss();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		finish();
	}

}
