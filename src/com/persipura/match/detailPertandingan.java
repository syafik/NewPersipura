package com.persipura.match;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.persipura.bean.HasilBean;
import com.persipura.bean.matchResult;
import com.persipura.utils.AppConstants;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.MainActivity;
import com.webileapps.navdrawer.R;

public class detailPertandingan extends SherlockFragment {

	private LayoutInflater mInflater;
	List<HasilBean> listThisWeekBean;
	List<matchResult> listResult;
	LinearLayout lifePageCellContainerLayout1, lifePageCellContainerLayout2;
	String nid;
	private ProgressDialog progressDialog;
	// List<Map<String, String>> planetsList = new ArrayList<Map<String,
	// String>>();

	public static final String TAG = detailPertandingan.class.getSimpleName();

	public static detailPertandingan newInstance() {
		return new detailPertandingan();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle b = getArguments();
		nid = b.getString("myString");
		MainActivity.getInstance().HideOtherActivities();
		showProgressDialog();
		new fetchLocationFromServer().execute("");
		new fetchMatchResult().execute("");
		
		View rootView = inflater.inflate(R.layout.detail_match, container,
				false);
		mInflater = getLayoutInflater(savedInstanceState);

		lifePageCellContainerLayout1 = (LinearLayout) rootView
				.findViewById(R.id.location_linear1_parentview);
		lifePageCellContainerLayout2 = (LinearLayout) rootView
				.findViewById(R.id.location_linear2_parentview);
		
//		MainActivity.newInstance().HideOtherActivities();
		return rootView;
	}
	
	private void showProgressDialog() {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Loading...");
		final Handler h = new Handler();
		final Runnable r2 = new Runnable() {

			@Override
			public void run() {
				progressDialog.dismiss();
			}
		};

		Runnable r1 = new Runnable() {

			@Override
			public void run() {
				progressDialog.show();
				h.postDelayed(r2, 5000);
			}
		};

		h.postDelayed(r1, 500);

		progressDialog.show();
	}

	private class fetchMatchResult extends AsyncTask<String, Void, String> {
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {

			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/match_summary", "match_id=" + nid);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {

			try {
				JSONArray jsonArray = new JSONArray(result);

				listResult = new ArrayList<matchResult>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					matchResult thisWeekBean = new matchResult();
					thisWeekBean.setNid(resObject.getString("id"));
					thisWeekBean.setMinute(resObject.getString("minute"));
					thisWeekBean.setPlayer(resObject.getString("player"));
					thisWeekBean.setTeam(resObject.getString("team"));
					thisWeekBean.setType(resObject.getString("type"));

					listResult.add(thisWeekBean);
				}
				if (listResult != null && listResult.size() > 0) {

					createMatchResult(listResult);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity(),
						"Failed to retrieve data from server",
						Toast.LENGTH_LONG).show();
			}

		}

		// @SuppressWarnings("deprecation")
		private void createMatchResult(List<matchResult> listResults)
				throws IOException {
			for (int i = 0; i < listResults.size(); i++) {
				matchResult thisWeekBean = listResults.get(i);

				View cellViewMainLayout = mInflater.inflate(
						R.layout.detail_match_list, null);
				TextView goalscorerA = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_name);
				TextView timescorerA = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_address);
				ImageView imgIconA = (ImageView) cellViewMainLayout
						.findViewById(R.id.imageView1);

				View cellViewMainLayout2 = mInflater.inflate(
						R.layout.detail_match_list2, null);
				TextView timescorerB = (TextView) cellViewMainLayout2
						.findViewById(R.id.findzoes_list_text_address);
				TextView goalscorerB = (TextView) cellViewMainLayout2
						.findViewById(R.id.findzoes_list_text_name);
				ImageView imgIconB = (ImageView) cellViewMainLayout2
						.findViewById(R.id.imageView1);

				if (thisWeekBean.getTeam().equals("home")) {
					goalscorerA.setText("");
					timescorerA.setText("");
					goalscorerA.setText(thisWeekBean.getPlayer());
					timescorerA.setText(thisWeekBean.getMinute());

					if (thisWeekBean.getType().equals("goal")) {
						imgIconA.setBackgroundResource(R.drawable.soccerballicon);
					} else if (thisWeekBean.getType().equals("yellowcard")) {
						imgIconA.setBackgroundResource(R.drawable.yellow_card_icon);
					} else {
						imgIconA.setBackgroundResource(R.drawable.red_card_icon);
					}

					lifePageCellContainerLayout1.addView(cellViewMainLayout);
				} else {
					goalscorerB.setText("");
					timescorerB.setText("");
					goalscorerB.setText(thisWeekBean.getPlayer());
					timescorerB.setText(thisWeekBean.getMinute());
					
					if (thisWeekBean.getType().equals("goal")) {
						imgIconB.setBackgroundResource(R.drawable.soccerballicon);
					} else if (thisWeekBean.getType().equals("yellowcard")) {
						imgIconB.setBackgroundResource(R.drawable.yellow_card_icon);
					} else {
						imgIconB.setBackgroundResource(R.drawable.red_card_icon);
					}
					
					lifePageCellContainerLayout2.addView(cellViewMainLayout2);
				}

			}
		}
	}

	private class fetchLocationFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {

			String result = WebHTTPMethodClass.httpGetService(
					"/restapi/get/match_results", "id=" + nid);
			return result;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(String result) {

			try {
				JSONArray jsonArray = new JSONArray(result);

				listThisWeekBean = new ArrayList<HasilBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject resObject = jsonArray.getJSONObject(i);
					HasilBean thisWeekBean = new HasilBean();
					thisWeekBean.setId(resObject.getString("id"));
					thisWeekBean.setTime(resObject.getString("time"));
					thisWeekBean.setDate(resObject.getString("date"));
					thisWeekBean.setHlogo(resObject.getString("h_logo"));
					thisWeekBean.setAlogo(resObject.getString("a_logo"));
					thisWeekBean.setHteam(resObject.getString("h_team"));
					thisWeekBean.setAteam(resObject.getString("a_team"));
					thisWeekBean.setHgoal(resObject.getString("h_goal"));
					thisWeekBean.setAgoal(resObject.getString("a_goal"));
					thisWeekBean.setHgoalkick(resObject
							.getString("h_goal_kick"));
					thisWeekBean.setAgoalkick(resObject
							.getString("a_goal_kick"));
					thisWeekBean.setHcornerkick(resObject
							.getString("h_corner_kick"));
					thisWeekBean.setAcornerkick(resObject
							.getString("a_corner_kick"));
					thisWeekBean.setHyellowcard(resObject
							.getString("h_yellow_card"));
					thisWeekBean.setAyellowcard(resObject
							.getString("a_yellow_card"));
					thisWeekBean.setAredcard(resObject.getString("a_red_card"));
					thisWeekBean.setHredcard(resObject.getString("h_red_card"));
					thisWeekBean.setApercentage(resObject
							.getString("a_percentage"));
					thisWeekBean.setHpercentage(resObject
							.getString("h_percentage"));
//					thisWeekBean.setHgoalscorer(resObject
//							.getString("h_goal_scorer"));
//					thisWeekBean.setAgoalscorer(resObject
//							.getString("a_goal_scorer"));
//					thisWeekBean.setAgoalminute(resObject
//							.getString("a_goal_minute"));
//					thisWeekBean.setHgoalminute(resObject
//							.getString("h_goal_minute"));
					listThisWeekBean.add(thisWeekBean);
				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {

					createSelectLocationListView(listThisWeekBean);
				}

			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getActivity(),
						"Failed to retrieve data from server",
						Toast.LENGTH_LONG).show();
			}

		}

		 @SuppressWarnings("deprecation")
		private void createSelectLocationListView(
				List<HasilBean> listThisWeekBean) {
			for (int i = 0; i < listThisWeekBean.size(); i++) {
				HasilBean thisWeekBean = listThisWeekBean.get(i);

				TextView ListDate = (TextView) getView().findViewById(
						R.id.datetext);
				TextView ListTime = (TextView) getView().findViewById(
						R.id.timetext);
				TextView cellnumTextView = (TextView) getView().findViewById(
						R.id.findzoes_list_text_cellnum);
				TextView NameTeamA = (TextView) getView().findViewById(
						R.id.nameteamA);
				TextView NameTeamB = (TextView) getView().findViewById(
						R.id.nameteamB);
				TextView ScoreTeamA = (TextView) getView().findViewById(
						R.id.scoreA);
				TextView ScoreTeamB = (TextView) getView().findViewById(
						R.id.scoreB);
				ImageView imgTeamA = (ImageView) getView().findViewById(
						R.id.imageView1);
				ImageView imgTeamB = (ImageView) getView().findViewById(
						R.id.imageView2);
				TextView goalkickA = (TextView) getView().findViewById(
						R.id.textView10);
				TextView goalkickB = (TextView) getView().findViewById(
						R.id.textView15);
				TextView cornerkickA = (TextView) getView().findViewById(
						R.id.textView11);
				TextView cornerkickB = (TextView) getView().findViewById(
						R.id.textView16);
				TextView yellowA = (TextView) getView().findViewById(
						R.id.textView12);
				TextView yellowB = (TextView) getView().findViewById(
						R.id.textView17);
				TextView redA = (TextView) getView().findViewById(
						R.id.textView13);
				TextView redB = (TextView) getView().findViewById(
						R.id.textView18);
				TextView percentageA = (TextView) getView().findViewById(
						R.id.textView14);
				TextView percentageB = (TextView) getView().findViewById(
						R.id.textView19);

				ListDate.setText("");
				ListTime.setText("");
				NameTeamA.setText("");
				NameTeamB.setText("");
				ScoreTeamA.setText("");
				ScoreTeamB.setText("");

				goalkickA.setText("");
				goalkickB.setText("");
				cornerkickA.setText("");
				cornerkickB.setText("");
				yellowA.setText("");
				yellowB.setText("");
				redA.setText("");
				redB.setText("");
				percentageA.setText("");
				percentageB.setText("");

				// cellnumTextView.setText("");

				ListDate.setText(thisWeekBean.getDate());
				ListTime.setText(thisWeekBean.getTime() + " WIT");
				NameTeamA.setText(thisWeekBean.getHteam());
				NameTeamB.setText(thisWeekBean.getAteam());
				ScoreTeamA.setText(thisWeekBean.getHgoal());
				ScoreTeamB.setText(thisWeekBean.getAgoal());

				goalkickA.setText(thisWeekBean.getHgoalkick());
				goalkickB.setText(thisWeekBean.getAgoalkick());
				cornerkickA.setText(thisWeekBean.getHcornerkick());
				cornerkickB.setText(thisWeekBean.getAcornerkick());
				yellowA.setText(thisWeekBean.getHyellowcard());
				yellowB.setText(thisWeekBean.getAyellowcard());
				redA.setText(thisWeekBean.getHredcard());
				redB.setText(thisWeekBean.getAredcard());
				percentageA.setText(thisWeekBean.getHpercentage());
				percentageB.setText(thisWeekBean.getApercentage());

				// Log.d("--------------------", thisWeekBean.getDate());
				Imageloader imageLoader = new Imageloader(getSherlockActivity()
						.getApplicationContext());
				imgTeamA.setTag(thisWeekBean.getHlogo());
				imageLoader.DisplayImage(thisWeekBean.getHlogo(),
						getActivity(), imgTeamA);

				imgTeamB.setTag(thisWeekBean.getAlogo());
				imageLoader.DisplayImage(thisWeekBean.getAlogo(),
						getActivity(), imgTeamB);
				
				
				AppConstants.fontrobotoTextViewBold(NameTeamA, 12, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(NameTeamB, 12, "ffffff", getActivity().getApplicationContext().getAssets());
				
				AppConstants.fontrobotoTextViewBold(ScoreTeamA, 18, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(ScoreTeamB, 18, "ffffff", getActivity().getApplicationContext().getAssets());
				
				AppConstants.fontrobotoTextView(ListTime, 11, "A6A5A2", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextView(ListDate, 11, "A6A5A2", getActivity().getApplicationContext().getAssets());
				
				AppConstants.fontrobotoTextViewBold(goalkickA, 11, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(goalkickB, 11, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(cornerkickB, 11, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(cornerkickA, 11, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(redA, 11, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(redB, 11, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(yellowA, 11, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(yellowB, 11, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(percentageA, 11, "ffffff", getActivity().getApplicationContext().getAssets());
				AppConstants.fontrobotoTextViewBold(percentageB, 11, "ffffff", getActivity().getApplicationContext().getAssets());

				// String[] partsA =
				// thisWeekBean.getHgoalscorer().split("\\|");
				// String[] partsB =
				// thisWeekBean.getAgoalscorer().split("\\|");
				// String[] timesA =
				// thisWeekBean.getHgoalminute().split("\\|");
				// String[] timesB =
				// thisWeekBean.getAgoalminute().split("\\|");

				// Map<String, String> map = new HashMap<String,String>();
				// map.put("iOS", "100");
				// map.put("iOS", "101");
				// map.put("iOS", "102");
				// map.put("iOS", "103");
				//
				// Set keys = map.keySet();
				//
				// for (Map.Entry<String,String> entry : map.entrySet()) {
				// String key = entry.getKey();
				// String value = entry.getValue();
				// Log.d("------------***********", key + value);
				// // do stuff
				// }

				// Map<String, List<String>> hm = new HashMap<String,
				// List<String>>();
				// List<String> values = new ArrayList<String>();
				// values.add("Value 1");
				// values.add("Value 2");
				// hm.put("Key1", values);

				// for (Map.Entry<String,String> entry : map.entrySet()) {
				// String key = entry.getKey();
				// String value = entry.getValue();
				// Log.d("------------***********", key + value);
				// // do stuff
				// }

				// for (int x = 0; x < partsA.length; x++) {
				// View cellViewMainLayout = mInflater.inflate(
				// R.layout.detail_match_list, null);
				// TextView goalscorerA = (TextView) cellViewMainLayout
				// .findViewById(R.id.findzoes_list_text_name);
				// TextView timescorerA = (TextView) cellViewMainLayout
				// .findViewById(R.id.findzoes_list_text_address);
				// goalscorerA.setText("");
				// goalscorerA.setText(partsA[x]);
				// timescorerA.setText("");
				// timescorerA.setText(timesA[x]);
				// lifePageCellContainerLayout1.addView(cellViewMainLayout);
				// }

				// for (int x = 0; x < partsB.length; x++) {
				// // Log.d("llllllllllllllllllllll", partsA[x]);
				// Log.d("llllllllllllllllllllll", partsB[x]);
				//
				// View cellViewMainLayout2 = mInflater.inflate(
				// R.layout.detail_match_list2, null);
				// TextView timescorerB = (TextView) cellViewMainLayout2
				// .findViewById(R.id.findzoes_list_text_address);
				// TextView goalscorerB = (TextView) cellViewMainLayout2
				// .findViewById(R.id.findzoes_list_text_name);
				//
				// goalscorerB.setText("");
				// goalscorerB.setText(partsB[x]);
				// timescorerB.setText("");
				// timescorerB.setText(timesB[x]);
				// lifePageCellContainerLayout2.addView(cellViewMainLayout2);
				//
				// }

			}
		}

	}
}
