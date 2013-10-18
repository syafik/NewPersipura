package com.persipura.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class AppConstants {

	// INTENT_EXTRA CONSTATNS

	public static final String INTENT_EXTRA_HOME_TABNUMBER = "homepage.tab.number";
	public static final String INTENT_EXTRA_HOME_ISFROMTAB = "homepage.tab.isfromTab";

	// INTENT_EXTRA CONSTATNS

	// PREF CONSTATNS

	public static final String PREF_HOME_ISOPENHOMEPAGE = "PREFHOMEPAGE_ISOPEN";
	public static final String PREF_SNAP_ISNOTOPENSTARTPAGE = "PREFSNAPSTART_ISOPEN";

	// PREF CONSTATNS

	// DIALOG CONSTATNS
	public static final String TAG_APP = "RotiTAG ** ";
	public static final int DIALOG_ALERT = 101;
	public static final int DIALOG_PROGRESS = 105;
	public static String PROGRESS_MSG = null;
	public static String DIALOG_MSG = "";
	public static String CONNECTION_FAILURE = "A connection failure occurred";
	public static String ClaimRewardPageHeader = "Are you done?";
	public static String ClaimRewardPageMessage = "Do NOT exit this page until the cashier has seen your 3-digit code. \n\n"
			+ "Tapping \"Continue\" will return you to My Goddies. You will not be able to access this code again.";
	public static String TIME_OUT = "The request timed out";
	public static final String DEVICE_TYPE = "android";
	public static final String REGISTERTYPE = "1";
	public static final String REGISTERTYPEFB = "2";

	public static final String EMAILCONTACT_US = "roti@rlvt.net";
	public static final String EMAILFAQ_CONTACT_US = "roti@rlvt.net";// "zoes@rlvt.net";
	public static final String EMAILSUBJECT = "Roti promo code query";
	public static final String EMAILSUBJECTFAQ = "Roti query";
	public static String EMAILSUBJECT1 = "Roti";
	public static String EMAILBODY = "Roti";
	public static String EMAILSUBJECTFB = "Roti";
	public static String EMAILBODYFB = "Roti";
	public static String EMAILSUBJECTTWT = "Roti";
	public static String EMAILBODYTWT = "Roti";
	public static String CONSTANTTITLEMESSAGE = "Roti";

	public static final String ERROR401SERVICES = "Please login with different id";
	public static String ERROR401 = "";
	public static final String ERRORLOCATIONSERVICES = "Please Turn On Location Services (in Settings) to allow Roti to reward you!";
	public static final String ERRORNETWORKCONNECTION = "Could not connect to server, please check your network connection";
	public static final String ERRORGOALCOMPLETE = "Mark this goal as complete?";

	// PUSH PARAMS

	public static final String PUSH_NOTIFICATION_KEY = "389484218706";
	public static final String PUSH_NOTIFICATION_TAG = "Roti Message";
	public static final int PUSH_NOTIFICATION_ID = 1234;
	public static final String PUSH_NOTIFICATION_MESSAGE = "packageName.push.message";
	public static final String PUSH_NOTIFICATION_CLASS = "packageName.push.classname";

	// FACEBOOK PARAMS

	public static final String FACEBOOK_APPID = "469864033052889"; // ROTI
	public static final String[] FACEBOOK_PERMISSIONARR = new String[] {
			"read_stream", "email", "user_photos", "publish_checkins",
			"publish_stream", "offline_access", "photo_upload" };
	public static String FB_APPID = "407799879277636";
	public static final String POSTID = "postid";

	// FACEBOOK PARAMS

	// TWITTER KEY

	public static final String CONSUMER_KEY = "1uM05u2VNjHzRZNFvnuoQ";
	public static final String CONSUMER_SECRET = "VyUGZ1NDz0wmdWu9H3IIk4duvCVK4RQoPgFiMtW3p8";
	public static final String TWITTER_MEDIA_API_KEY = "0dcf25567ddfcf5a1491dd1d5e0cf4e8";
	public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";

	public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-twitter";
	public static final String OAUTH_CALLBACK_HOST = "callback";
	public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME
			+ "://" + OAUTH_CALLBACK_HOST;

	// TWITTER KEY

	public static String LOCALE_HEADER_VALUE = "en";
	/* WEB SERVICE URL CONSTANTS */

	// Live
	public static String APPKEY = "cGU8o7zrsXqBM3pv";
	public static final String ROOT_URL = "http://192.241.229.12";
	public static final String ROOT_URL_HTTPS = "http://192.241.229.12";
	public static String BASE_URL = ROOT_URL;
	public static String BASE_URL_HTTPS = ROOT_URL_HTTPS + "/api/v1"; // Live
	public static String URL_TERMS_OF_USE = ROOT_URL + "/terms_of_use";
	public static String URL_PRIVACY_URL = ROOT_URL + "/privacy_policy";
	public static String URL_FB_PAGE = ROOT_URL + "/url/7/roti-facebook";
	public static String URL_TWITTER_PAGE = ROOT_URL + "/url/7/roti-twitter";
	// Live

	public static String CALL_US_NOW_CELL = "CALL_US_NOW_CELL_IDENTIFIER";
	public static String OFFER_LIST_CELL = "OFFER_LIST_CELL_IDENTIFIER";
	public static String CALL_US_NOW_CUSTOM_LABEL_NAME = "1";
	public static String CALL_US_NOW_CUSTOM_LABEL_DESCRIPTION = "2";
	public static String CALL_US_NOW_CUSTOM_LABEL_DISTANCE = "3";
	public static String CALL_US_NOW_CUSTOM_BUTTON_MOBILE = "4";

	// PREFERENCE VARIABLE

	public static String PREFAUTH_TOKEN = "authtoken";
	public static String PREFPUSHREGISTRATIONID = "registrationId";
	public static String PREFLOGOUTBUTTONTAG = "loginbuttonTag";
	public static String PREFCATEGORYSELECTED = "categoryselected";
	public static String PREFLOGINID = "prefloginid";

	// METHODS

	public static boolean isNetworkAvailable(Context context) {
		boolean isNetworkEnable = false;
		try {
			ConnectivityManager connMgr = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
					.getState() == NetworkInfo.State.CONNECTED
					|| connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
							.getState() == NetworkInfo.State.CONNECTING) {
				isNetworkEnable = true;
			} else if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
					.getState() == NetworkInfo.State.CONNECTED
					|| connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
							.getState() == NetworkInfo.State.CONNECTING) {
				isNetworkEnable = true;
			} else
				isNetworkEnable = false;
		} catch (Exception e) {
			isNetworkEnable = false;
			e.printStackTrace();
		}
		return isNetworkEnable;
	}

	public static String getDeviceID(Context context) {
		String deviceId = "";
		try {
			final TelephonyManager tm = (TelephonyManager) context
					./* getBaseContext(). */getSystemService(Context.TELEPHONY_SERVICE);

			final String tmDevice, tmSerial, androidId;
			tmDevice = "" + tm.getDeviceId();
			tmSerial = "" + tm.getSimSerialNumber();
			androidId = ""
					+ android.provider.Settings.Secure.getString(
							context.getContentResolver(),
							android.provider.Settings.Secure.ANDROID_ID);
			UUID deviceUuid = new UUID(androidId.hashCode(),
					((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
			deviceId = deviceUuid.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceId;
	}

	public static boolean CheckEnableGPS(Context context) {
		boolean isGPSEnabled = false;
		try {
			String provider = Settings.Secure.getString(
					context.getContentResolver(),
					Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			if (!provider.equals("")) {
				isGPSEnabled = true;// GPS Enabled
			} else {
				isGPSEnabled = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isGPSEnabled;
	}

	private static boolean isConfirm = false;

	public static boolean showConfirmMsgDialog(String title,
			final String message, final String billid, Context context) {
		isConfirm = false;
		try {
			AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
			alt_bld.setMessage(message)
					.setCancelable(false)
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									isConfirm = false;
									dialog.cancel();
								}
							})
					.setPositiveButton("Confirm",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									isConfirm = true;
									dialog.cancel();
									// new
									// deleteBillFromServer().execute(billid);
								}
							});
			AlertDialog alert = alt_bld.create();
			// alert.setTitle(title);
			alert.setTitle(CONSTANTTITLEMESSAGE);
			alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
			alert.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isConfirm;
	}

	// public static void parseInput(String result, Activity mHomePage) {
	// if (result != null && !result.equals("")) {
	// try {
	// result = checkFor401Error(result);
	// if (result.equals(""))
	// return;
	// JSONObject resObject = new JSONObject(result);
	// String sucess = "";// resObject.getString("status");
	// String notice = "";
	// if (resObject.has("status"))
	// sucess = resObject.getString("status");
	// if (resObject.has("notice")) {
	// notice = resObject.getString("notice");
	// if (!notice.equals("")
	// && notice.equals("Unauthorized API request.")) {
	// // mHomePage.showLoginOptionPage(false);//TODO
	// return;
	// }
	// }
	// if (resObject.has("message"))
	// notice = resObject.getString("message");
	//
	// String errors = "";
	// String auth_token = "";
	// try {
	// if (resObject.has("auth_token")) {
	// auth_token = resObject.getString("auth_token");
	// SharedPreferences mPreference = PreferenceManager
	// .getDefaultSharedPreferences(mHomePage);
	// Editor prefsEditor = mPreference.edit();
	// prefsEditor.putString(AppConstants.PREFAUTH_TOKEN,
	// auth_token);
	// Log.d("auth_token", auth_token);
	// prefsEditor.commit();
	// }
	// } catch (Exception e) {
	// }
	// try {
	// if (resObject.has("errors")) {
	// errors = resObject.getString("errors");
	// }
	// } catch (Exception e) {
	// }
	// if (sucess != null && !sucess.equals("") && !notice.equals("")) {// false
	// showMsgDialog(CONSTANTTITLEMESSAGE, notice, mHomePage);
	// } else {
	// if (errors != null && (!errors.equals(""))) {
	// showMsgDialog(CONSTANTTITLEMESSAGE, errors + notice,
	// mHomePage);
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }

	// private static String checkFor401Error(String responseData) {
	// try {
	// String message = "";
	// JSONObject responseJson = new JSONObject(responseData);
	// if (AppConstants.ERROR401.equalsIgnoreCase("401")) {
	// Log.i("Response Json Failure:", "" + responseData.toString());
	// AppConstants.showMessageDialogWithNewIntent(
	// AppConstants.ERROR401SERVICES,
	// RotiHomeActivity.getInstance());// TODO
	// AppConstants.ERROR401 = "";
	// responseData = "";
	// } else if (responseJson.has("status")
	// && responseJson.getBoolean("status") == false) {
	// Log.i("Response Json Failure:", "" + responseJson);
	// if (responseJson.has("notice"))
	// message = responseJson.getString("notice");
	// if (message.equals("") && responseJson.has("message"))
	// message = responseJson.getString("message");
	// if (!message.equals("")
	// && message.equals("Unauthorized API request.")) {
	// AppConstants.showMessageDialogWithNewIntent(message,
	// RotiHomeActivity.getInstance());// TODO
	// responseData = "";
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// AppConstants.ERROR401 = "";
	// return responseData;
	// }

	static AlertDialog.Builder alertDialogBuilder;

	public static void showMsgDialog(String title, final String message,
			Context context) {
		try {
			if (alertDialogBuilder == null) {
				alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder
						.setMessage(message)
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										alertDialogBuilder = null;
										dialog.cancel();
									}
								});
				AlertDialog alert = alertDialogBuilder.create();
				if (title.equals("")) {
					alert.setTitle(CONSTANTTITLEMESSAGE);
					alert.setIcon(AlertDialog.BUTTON_NEGATIVE);
				} else {
					alert.setTitle(title);
					alert.setIcon(AlertDialog.BUTTON_NEUTRAL);
				}
				alert.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showMessageDialogWithNewIntent(String message,
			Context context) {
		if (alertDialogBuilder == null) {
			alertDialogBuilder = new AlertDialog.Builder(context);
			alertDialogBuilder
					.setMessage(message)
					.setCancelable(false)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int id) {
									alertDialogBuilder = null;
									dialog.cancel();
									// HomePage.getInstance().showLoginOptionPage(
									// false);//TODO
								}
							});
			AlertDialog alert = alertDialogBuilder.create();
			alert.show();
		}
	}

	// Fast Implementation
	public static StringBuilder inputStreamToString(InputStream is)
			throws IOException {
		String line = "";
		StringBuilder total = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}
		rd.close();
		return total;
	}

	public static Bitmap getBitmap(String url, /* String imageView, */
			String type) {
		Bitmap bm = null;
		try {
			URL aURL = new URL(url);
			URLConnection conn = aURL.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			bm = BitmapFactory.decodeStream(new FlushedInputStream(is));
			if (type.equals("0")/* == 0 */)
				saveImageToSdCard(url);
			bis.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return bm;
	}

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			try {
				while (totalBytesSkipped < n) {
					long bytesSkipped = in.skip(n - totalBytesSkipped);
					if (bytesSkipped == 0L) {
						int b = read();
						if (b < 0) {
							break; // we reached EOF
						} else {
							bytesSkipped = 1; // we read one byte
						}
					}
					totalBytesSkipped += bytesSkipped;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return totalBytesSkipped;
		}
	}

	private static void saveImageToSdCard(String imageView) {
		InputStream input = null;
		try {
			input = new URL((String) imageView).openStream();
			File storagePath = Environment.getExternalStorageDirectory();
			String path = storagePath.getAbsolutePath() + "/Roti";
			storagePath = new File(path);
			if (!storagePath.exists())
				storagePath.mkdir();
			File imageFile = new File(storagePath.getAbsolutePath()
					+ "/daily.png");
			if (imageFile.exists())
				imageFile.delete();
			OutputStream output = new FileOutputStream(new File(storagePath,
					"daily.png"));
			try {
				byte[] buffer = new byte[1024];
				int bytesRead = 0;
				while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
					output.write(buffer, 0, bytesRead);
				}
			} finally {
				output.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// public static void createImages(Bitmap bitmapOrg, ImageView imageView
	// /* ,int type */) {
	// try {
	// imageView.setImageBitmap(bitmapOrg);
	// int width = bitmapOrg.getWidth(); /* 140 */
	// int height = bitmapOrg.getHeight();
	// Display display = RotiHomeActivity.getInstance().getWindowManager()//
	// TODO
	// .getDefaultDisplay();
	//
	// int maxWidth = display.getWidth() - 0;
	// int maxHeight = display.getHeight() - 0;
	// double newWidth = maxWidth - 0;
	// double newHeight = maxHeight - 0;
	//
	// if (width <= newWidth && height <= newHeight) {
	// BitmapDrawable bmd = new BitmapDrawable(bitmapOrg);
	// imageView.setImageDrawable(bmd);
	// imageView.setScaleType(ScaleType.FIT_CENTER);
	// return;
	// }
	// double ratio = (double) width / height;
	// if (width > newWidth || height > maxWidth/* newHeight */) {
	// if (ratio > 1) {
	// newHeight = newWidth / ratio;
	// } else {
	// newWidth = newHeight * ratio;
	// }
	// }
	// float scaleWidth = ((float) newWidth) / width;
	// float scaleHeight = ((float) newHeight) / height;
	// Matrix matrix = new Matrix();
	//
	// matrix.postScale(scaleWidth, scaleHeight);
	// Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,
	// height, matrix, true);
	// imageView.setImageBitmap(resizedBitmap);
	// BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
	// imageView.setImageDrawable(bmd);
	// imageView.setScaleType(ScaleType.FIT_CENTER);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public static Date makeDate(String dateString) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.parse(dateString);
	}

	public static int DateDiff(Date date1, Date date2) {
		int diffDay = diff(date1, date2);
		System.out.println("Different Day : " + diffDay);
		return diffDay--;
	}

	private static int diff(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(date1);
		c2.setTime(date2);
		int diffDay = 0;

		if (c1.before(c2)) {
			diffDay = countDiffDay(c1, c2);
		} else {
			diffDay = countDiffDay(c2, c1);
		}

		return diffDay;
	}

	private static int countDiffDay(Calendar c1, Calendar c2) {
		int returnInt = 0;
		while (!c1.after(c2)) {
			c1.add(Calendar.DAY_OF_MONTH, 1);
			returnInt++;
		}
		if (returnInt > 0) {
			returnInt = returnInt - 1;
		}
		return (returnInt);
	}

	// METHODS

	// FONTS

	// private static String DINEngschriftStdfont = "DINEngschriftStd.otf";//

	public static final String COLORORANGETEXTRGB = "DF821D";
	public static final String COLORBROWNTEXTRGB = "3F2513";
	public static final String COLORDARKGRAYRGB = "666666";
	public static final String COLORWHITERGB = "FFFFFF";

	// public static void fontDINEngschriftStdTextView(TextView textView,
	// int size, String color, AssetManager assetManager) {
	// Typeface face = Typeface.createFromAsset(assetManager, ""
	// + DINEngschriftStdfont);
	// textView.setTextSize(size);
	// color = "#" + color;
	// textView.setTextColor(Color.parseColor(color));
	// textView.setTypeface(face/* ,Typeface.BOLD */);
	// }

	// public static void fontDINEngschriftStdBoldTextView(TextView textView,
	// int size, String color, AssetManager assetManager) {
	// Typeface face = Typeface.createFromAsset(assetManager, ""
	// + DINEngschriftStdfont);
	// textView.setTextSize(size);
	// color = "#" + color;
	// textView.setTextColor(Color.parseColor(color));
	// textView.setTypeface(face, Typeface.BOLD);
	// }

	public static void setTextViewAttribute(TextView textView, int size,
			String color, AssetManager assetManager/* , Typeface face */) {
		textView.setTextSize(size);
		color = "#" + color;
		textView.setTextColor(Color.parseColor(color));
	}

	public static void setTextViewAttributeBold(TextView textView, int size,
			String color, AssetManager assetManager/* , Typeface face */) {
		textView.setTextSize(size);
		color = "#" + color;
		textView.setTextColor(Color.parseColor(color));
		textView.setTypeface(null, Typeface.BOLD);
	}

	private static String ATSackersHeavyGothic = "ATSackersHeavyGothic.ttf";
	private static String roboto = "Roboto-Medium.ttf";
	private static String robotoLight = "Roboto-Regular.ttf";
	private static String bertholdcitybold = "berthold-city-bold.ttf";
	private static String dinbold = "din-bold.ttf";
	private static String DINEngschriftRegular = "DINEngschrift-Regular.ttf";
	private static String DINEngschriftStd = "DINEngschriftStd.ttf";
	private static String dinmedium = "din-medium.ttf";
	public static final String COLORKATHTHI = "5f3032";
	public static final String COLORLOCATIONKATHTHI = "5a2a2b";
	public static final String COLORABOUTRED = "902e26";
	public static final String COLORABOUTKATHTHI = "2d0a0a";

	public static void fontATSackersHeavyGothicTextView(TextView textView,
			int size, String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ ATSackersHeavyGothic);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontbertholdcityboldTextView(TextView textView,
			int size, String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ bertholdcitybold);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontdinboldTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + dinbold);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontDINEngschriftRegularTextView(TextView textView,
			int size, String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ DINEngschriftRegular);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontDINEngschriftStdTextView(TextView textView,
			int size, String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ DINEngschriftStd);
		setTextViewAttribute(textView, size, color, face);
	}

	public static void fontdinmediumTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + dinmedium);
		setTextViewAttribute(textView, size, color, face);
	}

	private static void setTextViewAttribute(TextView textView, int size,
			String color, Typeface face) {
		textView.setTextSize(size);
		color = "#" + color;
		textView.setTextColor(Color.parseColor(color));
		textView.setTypeface(face/* ,Typeface.BOLD */);
		// tv.setTextColor(Color.parseColor("#000000"))
	}

	public static void fontATSackersHeavyGothicTextViewBold(TextView textView,
			int size, String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, ""
				+ ATSackersHeavyGothic);
		setTextViewAttributeBold(textView, size, color, face);
	}

	public static void fontrobotoTextView(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + robotoLight);
		setTextViewAttribute(textView, size, color, face);
	}
	
	public static void fontrobotoTextViewBold(TextView textView, int size,
			String color, AssetManager assetManager) {
		Typeface face = Typeface.createFromAsset(assetManager, "" + roboto);
		setTextViewAttributeBold(textView, size, color, face);
	}
	
	
	private static void setTextViewAttributeBold(TextView textView, int size,
			String color, Typeface face) {
		textView.setTextSize(size);
		color = "#" + color;
		textView.setTextColor(Color.parseColor(color));
		textView.setTypeface(face, Typeface.BOLD);
		// tv.setTextColor(Color.parseColor("#000000"))
	}

	// FONTS
}

// FONTS

// public static void fontDINEngschriftStdTextView(TextView textView,
// int size, String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + DINEngschriftStdfont);
// // setTextViewAttribute(textView, size, color, face);
// textView.setTextSize(size);
// color = "#" + color;
// textView.setTextColor(Color.parseColor(color));
// textView.setTypeface(face/* ,Typeface.BOLD */);
// }
//
// public static void fontDINEngschriftStdBoldTextView(TextView textView,
// int size, String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + DINEngschriftStdfont);
// // setTextViewAttribute(textView, size, color, face);
// textView.setTextSize(size);
// color = "#" + color;
// textView.setTextColor(Color.parseColor(color));
// textView.setTypeface(face, Typeface.BOLD);
// }
//
// public static void setTextViewAttribute(TextView textView, int size,
// String color, AssetManager assetManager/* , Typeface face */) {
// textView.setTextSize(size);
// color = "#" + color;
// textView.setTextColor(Color.parseColor(color));
// // textView.setTypeface(face/* ,Typeface.BOLD */);
// // tv.setTextColor(Color.parseColor("#000000"))
// }
//
// public static void setTextViewAttributeBold(TextView textView, int size,
// String color, AssetManager assetManager/* , Typeface face */) {
// textView.setTextSize(size);
// color = "#" + color;
// textView.setTextColor(Color.parseColor(color));
// textView.setTypeface(null, Typeface.BOLD);
// // tv.setTextColor(Color.parseColor("#000000"))
// }

// private static String myriadPro_Boldfont = "MyriadPro-Bold.otf";
// private static String myriadPro_Semiboldfont = "MyriadPro-Semibold.otf";
// private static String helveticafont = "Helvetica.ttf";
// private static String helveticaBoldfont = "HelveticaBold.ttf";
// private static String helveticaNeueBoldItalicfont =
// "HelveticaNeueBoldItalic.ttf";
// private static String helveticaNeueItalicfont =
// "HelveticaNeueItalic.otf";
// private static String helveticaNeueRomanfont = "HelveticaNeueRoman.otf";
// private static String verdanaItalicfont = "verdanai.ttf";//
// VerdanaItalic.ttf
// private static String verdanafont = "VerdanaRef.ttf";//

// public static void fontMyriadPro_BoldTextView(TextView textView, int
// size,
// String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + myriadPro_Boldfont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontMyriadPro_SemiboldfontTextView(TextView textView,
// int size, String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + myriadPro_Semiboldfont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontVerdanaItalicTextView(TextView textView, int size,
// String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + verdanaItalicfont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontVerdanaTextView(TextView textView, int size,
// String color, AssetManager assetManager) {
// Typeface face = Typeface
// .createFromAsset(assetManager, "" + verdanafont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontHelveticaTextView(TextView textView, int size,
// String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + helveticafont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontHelveticaBoldTextView(TextView textView, int size,
// String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + helveticaBoldfont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontHelveticaNeueBoldItalicTextView(TextView textView,
// int size, String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + helveticaNeueBoldItalicfont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontHelveticaNeueItalicTextView(TextView textView,
// int size, String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + helveticaNeueItalicfont);
// setTextViewAttribute(textView, size, color, face);
// }
//
// public static void fontHelveticaNeueRomanTextView(TextView textView,
// int size, String color, AssetManager assetManager) {
// Typeface face = Typeface.createFromAsset(assetManager, ""
// + helveticaNeueRomanfont);
// setTextViewAttribute(textView, size, color, face);
// }
//

// private static void setTextViewAttribute(TextView textView, int size,
// String color, Typeface face) {
// textView.setTextSize(size);
// color = "#" + color;
// textView.setTextColor(Color.parseColor(color));
// textView.setTypeface(face/* ,Typeface.BOLD */);
// // tv.setTextColor(Color.parseColor("#000000"))
// }
// public static final String COLORORANGEHEADERRGB = "FF931B";// :
// 255,140,27
// public static final String COLORORANGETEXTRGB = "FF7F00";// : 255,127,0
// public static final String COLORDARKGRAYRGB = "555555";// : 85,85,85
// public static final String COLORBLACKRGB = "000000";// : 85,85,85

// FONTS