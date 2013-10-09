package com.persipura.match;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.persipura.bean.HasilBean;
import com.persipura.utils.Imageloader;
import com.persipura.utils.WebHTTPMethodClass;
import com.webileapps.navdrawer.R;

public class detailPertandingan extends SherlockFragment {

	private LayoutInflater mInflater;
	List<HasilBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout1, lifePageCellContainerLayout2;
	String nid;
	
	public static final String TAG = detailPertandingan.class.getSimpleName();

	public static detailPertandingan newInstance() {
		return new detailPertandingan();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle b = getArguments();
		nid = b.getString("myString");
		new fetchLocationFromServer().execute("");
		View rootView = inflater.inflate(R.layout.detail_match, container,
				false);
		mInflater = getLayoutInflater(savedInstanceState);

		lifePageCellContainerLayout1 = (LinearLayout) rootView
				.findViewById(R.id.location_linear1_parentview);
		lifePageCellContainerLayout2 = (LinearLayout) rootView
				.findViewById(R.id.location_linear2_parentview);
		return rootView;
	}

	private class fetchLocationFromServer extends
			AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {

			String result = WebHTTPMethodClass.httpGetService("/restapi/get/match_results","id=" + nid);
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
					thisWeekBean.setHgoalscorer(resObject
							.getString("h_goal_scorer"));
					thisWeekBean.setAgoalscorer(resObject
							.getString("a_goal_scorer"));
					thisWeekBean.setAgoalminute(resObject
							.getString("a_goal_minute"));
					thisWeekBean.setHgoalminute(resObject
							.getString("h_goal_minute"));
					listThisWeekBean.add(thisWeekBean);
				}
				if (listThisWeekBean != null && listThisWeekBean.size() > 0) {

					createSelectLocationListView(listThisWeekBean);
				}

			} catch (Exception e) {
				e.printStackTrace();
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
				ListTime.setText(thisWeekBean.getTime());
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

				String[] partsA = thisWeekBean.getHgoalscorer().split("\\|");
				String[] partsB = thisWeekBean.getAgoalscorer().split("\\|");
				String[] timesA = thisWeekBean.getHgoalminute().split("\\|");
				String[] timesB = thisWeekBean.getAgoalminute().split("\\|");

				for (int x = 0; x < partsA.length; x++) {
					View cellViewMainLayout = mInflater.inflate(
							R.layout.detail_match_list, null);
					TextView goalscorerA = (TextView) cellViewMainLayout
							.findViewById(R.id.findzoes_list_text_name);
					TextView timescorerA = (TextView) cellViewMainLayout
							.findViewById(R.id.findzoes_list_text_address);
					goalscorerA.setText("");
					goalscorerA.setText(partsA[x]);
					timescorerA.setText("");
					timescorerA.setText(timesA[x]);
					lifePageCellContainerLayout1.addView(cellViewMainLayout);
				}

				for (int x = 0; x < partsB.length; x++) {
					// Log.d("llllllllllllllllllllll", partsA[x]);
					Log.d("llllllllllllllllllllll", partsB[x]);

					View cellViewMainLayout2 = mInflater.inflate(
							R.layout.detail_match_list2, null);
					TextView timescorerB = (TextView) cellViewMainLayout2
							.findViewById(R.id.findzoes_list_text_address);
					TextView goalscorerB = (TextView) cellViewMainLayout2
							.findViewById(R.id.findzoes_list_text_name);

					goalscorerB.setText("");
					goalscorerB.setText(partsB[x]);
					timescorerB.setText("");
					timescorerB.setText(timesB[x]);
					lifePageCellContainerLayout2.addView(cellViewMainLayout2);

				}

			}
		}

	}
}
