package com.persipura.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.kobjects.base64.Base64;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

public class Utility 
{
	private static String TAG = "Utility";
	public static boolean isNetworkAvailable(Context context) 
	{
	    ConnectivityManager connMgr = (ConnectivityManager) context
	        .getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED
	        || connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTING)
	    {
	    	return true;
	    }
	    
	    else if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
	        || connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTING) 
	    {
	    	return true;
	    } 
	    else
	    	return false;
	}
	
	public static Bitmap decodeFile(File f)
	{
	    try 
	    {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f),null,o);
	        final int REQUIRED_SIZE=70;
	        int width_tmp=o.outWidth, height_tmp=o.outHeight;
	        int scale=1;
	        while(true)
	        {
	            if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
	                break;
	            width_tmp/=2;
	            height_tmp/=2;
	            scale*=2;
	        }
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize=scale;
	        o2.inPurgeable=true;
	        
	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	    } 
	    catch (FileNotFoundException e) 
	    {
	    	e.printStackTrace();
	    }
	    return null;
	}
	
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){
        	
        	Log.e("Exception", ex+"");
			
        }
    }
	
	public static String encodeBase64(Bitmap image)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		String imageString=null;
		if(image!=null)
		{
			try
			{
			image.compress(Bitmap.CompressFormat.PNG, 70,stream); //compress to which format you want.
			byte [] byte_arr = stream.toByteArray();
			imageString = Base64.encode(byte_arr);//Base64.encodeBytes(byte_arr);
			}
			catch (OutOfMemoryError e) {
	            Log.e(TAG, "Out of memory error", e);
	        }
			catch (Exception e) {
				Log.e(TAG, "Exception", e);
			}
		}
		return imageString;
	}
	
	
	public static Bitmap makeImageBitmap(Context context,String imageString)
	{
		Bitmap imageDefault = null;//BitmapFactory.decodeResource(context.getResources(), R.drawable.noimage1);
		Bitmap image=null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		if(imageString!=null && imageString!="")
		{
	        try 
	        {
				byte[] decodeByte=Base64.decode(imageString);
				image=BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
				image.compress(Bitmap.CompressFormat.PNG, 70,stream);
				image=Bitmap.createScaledBitmap(image, 40, 40, false);
			} 
	        catch (IllegalArgumentException e) 
			{
				Log.e("Utility.class-Exception",String.valueOf(e.getMessage()));
				image=imageDefault;
			}
			catch (NullPointerException e) 
			{
				Log.e("Utility.class-Exception",String.valueOf(e.getMessage()));
				image=imageDefault;
				
			}
			catch (OutOfMemoryError e) {
	            Log.e(TAG, "Out of memory error", e);
	        }
			catch (Exception e) {
			}
		}
		else
		{
			image=imageDefault;
		}
		return image;
	}
	
	public static Bitmap downloadImage(Context context,String url)
	{
		if(url.trim().equals(""))return null;
		InputStream is = null;
        BufferedInputStream bis = null;
        Bitmap bmp = null;
        Log.e("Utility->ImageUrl",String.valueOf(url));
        try 
        {
        	URL url1=new URL(url);
            URLConnection conn = url1.openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is);
            bmp = BitmapFactory.decodeStream(bis);
        	
        } catch (MalformedURLException e) {
            Log.e(TAG, "Bad ad URL");
        } catch (IOException e) {
            Log.e(TAG, "Could not get remote ad image");
        }
        catch (NullPointerException e) {
            Log.e(TAG, "Could not get remote ad image");
        }
        catch (OutOfMemoryError e) {
            Log.e(TAG, "Out of memory error", e);
        }
        finally 
        {
            try {
                if( is != null )
                    is.close();
                if( bis != null )
                    bis.close();
            } catch (IOException e) {
                Log.w(TAG, "Error closing stream.");
            }
        }
        Log.e("Bitmap Returned from Utility:",String.valueOf(bmp));
        return bmp;
	}

	@SuppressWarnings("unused")
	public static byte[] bitmapOption(String imgpath1)
	{
		Bitmap bm = null; 
		byte[] b = null;
	      Options options = new Options();
	    if(bm!=null){
	     bm.recycle();
	     options = new Options();
	     options.inJustDecodeBounds = true;
	     BitmapFactory.decodeFile(imgpath1, options);
	     if(options.outHeight * options.outWidth * 2 >= 400000){
	      if((options.outHeight<=900 || options.outWidth<=900)){
	       options.inSampleSize = 2;
	      }else{
	       options.inSampleSize = 4;
	      }
	     }
	     // Do the actual decoding
	     options.inJustDecodeBounds = false;
	     options.inTempStorage = new byte[16*1024];
	     bm = BitmapFactory.decodeFile(imgpath1,options);
	     
	     ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	     bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
	     bm.compress(Bitmap.CompressFormat.PNG, 100, baos); 
	     b = baos.toByteArray();
	    }else{
	     options.inJustDecodeBounds = true;
	     BitmapFactory.decodeFile(imgpath1, options);
	     if(options.outHeight * options.outWidth * 2 >= 400000){
	      if((options.outHeight<=900 || options.outWidth<=900)){
	       options.inSampleSize = 2;
	      }else{
	       options.inSampleSize = 4;
	      }
	     }
	     options.inJustDecodeBounds = false;
	     options.inTempStorage = new byte[16*1024];
	     bm = BitmapFactory.decodeFile(imgpath1,options);
	     ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	     bm = Bitmap.createScaledBitmap(bm, 70, 70, true);
	     bm.compress(Bitmap.CompressFormat.PNG, 100, baos);   
	     b = baos.toByteArray();
	    }
	    
	    if(bm!=null)
	    {
	     bm=null;
	    }
	    return b;
	}

	
	public static File getImageFileName(Context context,int filename) 
	{

		  String newFolder = "/NearBuy";
		  String extStorageDirectory = Environment.getExternalStorageDirectory()
		    .toString();
		  File path = new File(extStorageDirectory + newFolder);
		  if (!path.exists())
		  {
		   path.mkdir();
		  }
		  File subdirectory = new File(path, "images");
		  if (!subdirectory.exists()) {
		   subdirectory.mkdirs(); // makes all subdir levels you need
		  }
		  String imageName = filename + ".png";
		  File file = new File(subdirectory, imageName);

		  return file;

	}
	public static String  saveImageToSD(File file,Bitmap bm)
	{
		if(bm==null);
		else
		{
		try {
			   FileOutputStream outStream = new FileOutputStream(file);
			   bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			   outStream.flush();
			   outStream.close();

			   Log.i("Utility", "OK, Image Saved to SD");
			   Log.i("Utility", "height = "+ bm.getHeight() + ", width = " + bm.getWidth());
			   
			  } catch (FileNotFoundException e){
			   e.printStackTrace();
			   Log.i("Hub", "FileNotFoundException: "+ e.toString());
			   return null;
			  } catch (IOException e) {
			   e.printStackTrace();
			   Log.i("Hub", "IOException: "+ e.toString());
			   return null;
			  }
		}
		return 
		file.getAbsolutePath();
	}
	
	public static boolean removeAppDataDir(File dir)
	{
		if (dir.isDirectory())
		{
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++)
	        {
	            new File(dir, children[i]).delete();
	        }
	        return true;
	    }
		return false;
	}
	
	public static void showDialogOkOnly(final Context context, String message) {
		new AlertDialog.Builder(context)
				.setMessage(message)
				.setNegativeButton("OK",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								dialog.dismiss();
							}
						}).show();
	}

}

