package com.mycam;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cam.imagedatabase.DBHelper;
import com.image.effects.HorizontalListView;
import com.image.effects.ZakoopiImageRender;
import com.image.effects.zakoopiGLSurfaceView;
import com.zakoopi.R;
import com.zakoopi.helper.Variables;

/*
 * This will help to draw effects on images 
 */
public class ImageEffects extends Activity {

	public static final int FLIP_VERTICAL = 1;
	public static final int FLIP_HORIZONTAL = 2;
	private final int WAIT_TIME = 2500;
	TextView save;
	HorizontalListView list;
	private zakoopiGLSurfaceView imageBlurGLSurfaceView;
	// private Gallery gallery;
	// private Bitmap bitmap;
	private GalleryImageAdapter galleryImageAdapter;
	ZakoopiImageRender imageBlurRender;
	private List<Bitmap> bitmapPreview = new ArrayList<Bitmap>();
	ProgressDialog bar = null;
	byte[] path;
	ImageView next;
	private int THUMBSIZE = 100;
	Bitmap ThumbImage;
	public static Bitmap savebitmap;
	public static int width;
	public static int height;

	private SQLiteDatabase db;
	private SQLiteStatement stm;
	public static final String DBTABLE = "lookbook";
	byte imageInByte[];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_effects);

		save = (TextView) findViewById(R.id.textView1);
		list = (HorizontalListView) findViewById(R.id.listview);
		next = (ImageView) findViewById(R.id.imageView2);

		findView();

		ThumbImage = ThumbnailUtils.extractThumbnail(Variables.imgbitmap,
				THUMBSIZE, THUMBSIZE);

		imageBlurRender = new ZakoopiImageRender(ImageEffects.this,
				Variables.imgbitmap);
		try {
			imageBlurGLSurfaceView.setImageSource(Variables.imgbitmap);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		setImageBlurSource();

		((ImageView) findViewById(R.id.imageView2))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						Bitmap bb = getResizedBitmap(Variables.imgeffects,
								1080, 1080);
						bb.compress(Bitmap.CompressFormat.JPEG, 100, stream);
						imageInByte = stream.toByteArray();

						Variables.imgarr = imageInByte;
						if (!ImageDetail.addmoreimg.equals("addmore")) {
							DBHelper hp = new DBHelper(ImageEffects.this);
							db = hp.getWritableDatabase();
							db.delete(DBTABLE, null, null);
						}

						Intent mainIntent = new Intent(ImageEffects.this,
								ImageDetail.class);
						ImageEffects.this.startActivity(mainIntent);
						// ImageEffects.this.finish();

					}
				});
	}

	public void setImageBlurSource() {
		// imageBlurGLSurfaceView.setVisibility(View.VISIBLE);

		galleryImageAdapter = new GalleryImageAdapter(ImageEffects.this,
				ThumbImage);
		galleryImageAdapter.setBlurPreviewImage(bitmapPreview);

		imageBlurGLSurfaceView.setEffectAdapter(galleryImageAdapter);
		list.setAdapter(galleryImageAdapter);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// savebitmap = null;
				imageInByte = null;
				Variables.imgeffects = null;
				galleryImageAdapter.setSelectedItem(position);
				imageBlurGLSurfaceView.setCurrentEffectId(position);
				imageBlurGLSurfaceView.requestRender();

			}
		});
	}

	@Override
	protected void onResume() {
		imageBlurGLSurfaceView.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		imageBlurGLSurfaceView.onPause();
	}

	public void findView() {
		imageBlurGLSurfaceView = (zakoopiGLSurfaceView) findViewById(R.id.imageView1);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		finish();
	}

	public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);

		return resizedBitmap;
	}

}