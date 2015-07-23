package com.mycam;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import com.cam.imagedatabase.DBHelper;
import com.cam.instagram.ApplicationData;
import com.cam.instagram.InstagramApp;
import com.cam.instagram.InstagramSession;
import com.cam.instagram.InstagramApp.OAuthAuthenticationListener;
import com.squareup.picasso.Picasso;
import com.zakoopi.R;
import com.zakoopi.helper.DynamicImageView;
import com.zakoopi.helper.GridViewWithHeaderAndFooter;
import com.zakoopi.helper.SquareImageView;
import com.zakoopi.helper.Variables;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InstagramFragment extends Fragment {

	public ImageAdapter imageAdapter;
	public GridViewWithHeaderAndFooter imagegrid;
	private InstagramApp mApp;
	// ImageView full_img;
	ArrayList<String> images = new ArrayList<String>();
	ArrayList<String> images1 = new ArrayList<String>();
	int displayWidth;
	ImageView next;
	private static final String API_URL = "https://api.instagram.com/v1";
	private InstagramSession mSession;
	private DynamicImageView full_img;
	Button login;
	RelativeLayout rel,rel1;
	ProgressBar pppp;
	private SQLiteDatabase db;
	private SQLiteStatement stm;
	public static final String DBTABLE = "lookbook";
	String url;
	RelativeLayout rel_back;
	TextView txt_insta;
	Typeface typeface_regular;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View instagram = inflater.inflate(R.layout.instagram, null);
		
		typeface_regular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SourceSansPro-Regular.ttf");
		
		/**
		 * Database
		 */
		DBHelper hp = new DBHelper(getActivity());
		db = hp.getWritableDatabase();
		stm = db.compileStatement("insert into  " + DBTABLE
				+ " (photo,tag,desc,imagepath) values (?, ?, ?, ?)");
		
		mApp = new InstagramApp(getActivity(), ApplicationData.CLIENT_ID,
				ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
		mApp.setListener(listener);
		mSession = new InstagramSession(getActivity());

		Point size = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(size);
		displayWidth = size.x;
		int height = displayWidth;

		/**
		 * Find ID's
		 */
		next = (ImageView) instagram.findViewById(R.id.imageView1);
		login = (Button) instagram.findViewById(R.id.btn_img_insta);
		rel = (RelativeLayout) instagram.findViewById(R.id.rel);
		rel1 = (RelativeLayout) instagram.findViewById(R.id.relll);
		pppp=(ProgressBar) instagram.findViewById(R.id.progressBar1);
		imagegrid = (GridViewWithHeaderAndFooter) instagram
				.findViewById(R.id.gridview);
		txt_insta = (TextView) instagram.findViewById(R.id.txt_insta);
		rel_back = (RelativeLayout) instagram.findViewById(R.id.rel_back);
		txt_insta.setTypeface(typeface_regular);

		rel_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
				
			}
		});
		
		
		/**
		 * LayoutInflater
		 */
		LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
		View headerView = layoutInflater
				.inflate(R.layout.instagram_items, null);
		full_img = (DynamicImageView) headerView.findViewById(R.id.imageView1);
		imagegrid.addHeaderView(headerView);

		if (!mApp.hasAccessToken()) {

			rel.setVisibility(View.VISIBLE);
			rel1.setVisibility(View.GONE);

		} else {

			rel.setVisibility(View.GONE);
			rel1.setVisibility(View.VISIBLE);
			new getImageFromGallery().execute();
		}

		/**
		 * Click Listener
		 */
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (!ImageDetail.addmoreimg.equals("addmore")) {
					db.delete(DBTABLE, null, null);
				}
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				Bitmap bb =getBitmapFromURL(url);
				bb.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				byte imageInByte[] = stream.toByteArray();

				FileOutputStream outStream = null;
				File filePath = Environment
						.getExternalStorageDirectory();
				File dir = new File(filePath.getAbsolutePath()
						+ "/Zakoopi");
				dir.mkdirs();

				File outFile = new File(dir, "Pic"
						+ System.currentTimeMillis() + ".jpg");

				try {
					outStream = new FileOutputStream(outFile);
					outStream.write(imageInByte);
					outStream.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Variables.imgarr = imageInByte;
				String imgPath = outFile.getAbsolutePath();
				// insert images in database
				stm.bindBlob(1, imageInByte);
				stm.bindString(2, "tag");
				stm.bindString(3, "desc");
				stm.bindString(4, imgPath);
				stm.executeInsert();

				Intent in = new Intent(getActivity(),
						ImageDetail.class);
				startActivity(in);
				getActivity().finish();

			}
		});

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!mApp.hasAccessToken()) {

					final AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setMessage("Login to Instagram")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
											mApp.authorize();
											new getImageFromGallery().execute();
										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
										}
									});
					final AlertDialog alert = builder.create();
					alert.show();

				}

			}

		});

		return instagram;
	}

	/**
	 * getting Images from gallery
	 * class getImageFromGallery extends AsyncTask<Void, Void, Void>
	 * @author ZakoopiUser
	 *
	 */
	
	private class getImageFromGallery extends AsyncTask<Void, Void, Void> {

		//ProgressDialog bar = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			imagegrid.setVisibility(View.GONE);
			rel1.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				if (mSession.getId() != null
						|| mSession.getAccessToken() != null) {
					String urlString = API_URL + "/users/" + mSession.getId()
							+ "/media/recent/?access_token="
							+ mSession.getAccessToken();
					URL url = new URL(urlString);
					InputStream inputStream = url.openConnection()
							.getInputStream();
					String response = streamToString(inputStream);

					JSONObject jsonObject = (JSONObject) new JSONTokener(
							response).nextValue();
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					for (int i = 0; i < jsonArray.length(); i++) {

						JSONObject mainImageJsonObject = jsonArray
								.getJSONObject(i).getJSONObject("images")
								.getJSONObject("low_resolution");
						String imageUrlString = mainImageJsonObject
								.getString("url");

						images.add(imageUrlString);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void args) {
			
			imagegrid.setVisibility(View.VISIBLE);
			rel1.setVisibility(View.GONE);
			
			imageAdapter = new ImageAdapter(images);
			imagegrid.setAdapter(imageAdapter);

			try {
				
				Picasso.with(getActivity()).load(images.get(0)).into(full_img);
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			
		}

	}

	OAuthAuthenticationListener listener = new OAuthAuthenticationListener() {

		@Override
		public void onSuccess() {
			// tvSummary.setText("Connected as " + mApp.getUserName());
			// btnConnect.setText("Disconnect");
		}

		@Override
		public void onFail(String error) {
		//	Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
		}
	};

	/**
	 * streamToString
	 * @InputStream is
	 * @return str
	 * @throws IOException
	 */
	private String streamToString(InputStream is) throws IOException {
		String str = "";

		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			str = sb.toString();
		}

		return str;
	}

	/**
	 * Class ImageAdapter extends BaseAdapter
	 * @author ZakoopiUser
	 *
	 */
	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		ArrayList<String> images = new ArrayList<String>();

		public ImageAdapter(ArrayList<String> images) {

		try{
			this.images = images;
			mInflater = (LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
		}catch(Exception e){
			e.printStackTrace();
		}
		}

		public int getCount() {
			return images.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		@SuppressWarnings("deprecation")
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.galleryitem, null);
				holder.imageview = (SquareImageView) convertView
						.findViewById(R.id.thumbImage);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.imageview.setPadding(8, 8, 8, 8);

			Picasso.with(getActivity()).load(images.get(position))
					.placeholder(R.drawable.nine_patch_icn)
					.into(holder.imageview);

			holder.imageview.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Picasso.with(getActivity()).load(images.get(position))
							.into(full_img);
					url=images.get(position);
					imagegrid.scrollTo(0, (int) full_img.getY());
				}
			});
			return convertView;
		}
	}

	/**
	 * getBitmapFromURL()
	 * @String src
	 * @return
	 */
	public  Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	class ViewHolder {

		SquareImageView imageview;

	}
}
