package com.mycam;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.naver.android.helloyako.imagecrop.view.ImageCropView;
import com.zakoopi.R;
import com.zakoopi.helper.CustomScrollView;
import com.zakoopi.helper.CustomViewPager;
import com.zakoopi.helper.ExpandableHeightGridView;
import com.zakoopi.helper.SquareImageView;
import com.zakoopi.helper.Variables;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GalleryFragment extends Fragment {

	ExpandableHeightGridView gridview;
	ImageCropView imgcrop;
	ImageView next;
	FrameLayout framely;
	ProgressBar progress;
	private long lastId;
	public ArrayList<ImageItem> images = new ArrayList<ImageItem>();
	public ImageAdapter imageAdapter;
	protected Bitmap b;
	CustomScrollView scroll;
	RelativeLayout rel_back;
	TextView txt_gallery;
	Typeface typeface_regular;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View gallry = inflater.inflate(R.layout.gallery, null);
		
		/**
		 * Find ID's
		 */
		gridview = (ExpandableHeightGridView) gallry.findViewById(R.id.PhoneImageGrid);
		gridview.setExpanded(true);
		rel_back = (RelativeLayout) gallry.findViewById(R.id.rel_back);
		txt_gallery = (TextView) gallry.findViewById(R.id.txt_gallery);
		imgcrop = (ImageCropView) gallry.findViewById(R.id.image);
		next = (ImageView) gallry.findViewById(R.id.imageView1);
		framely = (FrameLayout) gallry.findViewById(R.id.frm);
		progress = (ProgressBar) gallry.findViewById(R.id.progressBar1);
		scroll = (CustomScrollView) gallry.findViewById(R.id.scrollView1);
		
		new DownloadFilesTask().execute();

		/**
		 * Typeface
		 */
		typeface_regular = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/SourceSansPro-Regular.ttf");
		txt_gallery.setTypeface(typeface_regular);
		
		Point size = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(size);
		CameraFragment.displayWidth = size.x;
		CameraFragment.displayHeight = size.y;

		/**
		 * Touch Listener
		 */
		imgcrop.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				CustomViewPager.setSwipeable(false);
				scroll.setScrollingEnabled(false);
				return false;
			}
		});

		gridview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				CustomViewPager.setSwipeable(true);
				scroll.setScrollingEnabled(true);
				return false;
			}
		});

		/**
		 * Click Listener
		 */
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!imgcrop.isChangingScale()) {
					b = imgcrop.getCroppedImage();
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					Variables.imgbitmap = b;
					Intent in = new Intent(getActivity(), ImageEffects.class);
					startActivity(in);
					getActivity().finish();
				}
			}
		});
		
		rel_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});

		return gallry;
	}

	/**
	 * DownloadFilesTask class extends AsyncTask<Void, Void, Void>
	 * @author ZakoopiUser
	 *
	 */
	private class DownloadFilesTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progress.setVisibility(View.VISIBLE);
			scroll.setVisibility(View.GONE);
			//gridview.setVisibility(View.GONE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			getImageFromGallery();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			ImageItem item = images.get(0);
			try {
				final String[] columns = { MediaStore.Images.Media.DATA };
				@SuppressWarnings("deprecation")
				Cursor imagecursor = getActivity().managedQuery(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
						MediaStore.Images.Media._ID + " = " + item.id, null,
						MediaStore.Images.Media._ID);
				if (imagecursor != null && imagecursor.getCount() > 0) {
					imagecursor.moveToPosition(0);
					String path = imagecursor
							.getString(imagecursor
									.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
					// imagecursor.close();
					imgcrop.setImageFilePath(path);
					imgcrop.setAspectRatio(1, 1);

				}

				imageAdapter = new ImageAdapter(getActivity(), images);
				gridview.setAdapter(imageAdapter);

			} catch (Exception e) {
				e.printStackTrace();
			}

			progress.setVisibility(View.GONE);
			scroll.setVisibility(View.VISIBLE);
			//gridview.setVisibility(View.VISIBLE);
		}

	}

	/**
	 *{@code} Method getImageFromGallery()
	 */
	public void getImageFromGallery() {

		try {
			final String[] columns = { MediaStore.Images.Thumbnails._ID };
			final String orderBy = MediaStore.Images.Media._ID;
			@SuppressWarnings("deprecation")
			Cursor imagecursor = getActivity().managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
					null, null, orderBy);
			if (imagecursor != null) {
				int image_column_index = imagecursor
						.getColumnIndex(MediaStore.Images.Media._ID);
				int count = imagecursor.getCount();
				for (int i = 0; i < count; i++) {
					imagecursor.moveToPosition(i);
					int id = imagecursor.getInt(image_column_index);
					ImageItem imageItem = new ImageItem();
					imageItem.id = id;
					lastId = id;
					imageItem.img = MediaStore.Images.Thumbnails.getThumbnail(
							getActivity().getContentResolver(), id,
							MediaStore.Images.Thumbnails.MICRO_KIND, null);

					images.add(imageItem);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Class ImageAdapter extends BaseAdapter
	 * @author ZakoopiUser
	 *
	 */

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		ArrayList<ImageItem> images = new ArrayList<ImageItem>();

		public ImageAdapter(Context ctx, ArrayList<ImageItem> images) {

			this.images = images;
			mInflater = (LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
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

		public View getView(int position, View convertView, ViewGroup parent) {
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
			ImageItem item = images.get(position);

			holder.imageview.setId(position);

			holder.imageview.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					int id = v.getId();
					ImageItem item = images.get(id);

					final String[] columns = { MediaStore.Images.Media.DATA };
					@SuppressWarnings("deprecation")
					Cursor imagecursor = getActivity().managedQuery(
							MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
							columns,
							MediaStore.Images.Media._ID + " = " + item.id,
							null, MediaStore.Images.Media._ID);
					if (imagecursor != null && imagecursor.getCount() > 0) {
						imagecursor.moveToPosition(0);
						String path = imagecursor.getString(imagecursor
								.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

						imgcrop.setImageFilePath(path);
						imgcrop.setAspectRatio(1, 1);
						scroll.setScrollingEnabled(true);
						scroll.scrollTo(0, (int) imgcrop.getY());
					}
				}
			});
			holder.imageview.setImageBitmap(item.img);

			return convertView;
		}
	}

	/**
	 * ViewHolder Class
	 * @author ZakoopiUser
	 *
	 */
	class ViewHolder {
		SquareImageView imageview;

	}

	/**
	 * ImageItem Class
	 * @author ZakoopiUser
	 *
	 */
	class ImageItem {
		boolean selection;
		int id;
		Bitmap img;
	}
}
