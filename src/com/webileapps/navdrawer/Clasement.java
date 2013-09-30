package com.webileapps.navdrawer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.persipura.bean.clasementBean;
import com.persipura.utils.WebHTTPMethodClass;



public class Clasement extends SherlockFragment {

	private LayoutInflater mInflater;
	List<clasementBean> listThisWeekBean;
	LinearLayout lifePageCellContainerLayout;
	
	public static final String TAG = Clasement.class
	.getSimpleName();

	public static Clasement newInstance() {
		return new Clasement();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		new fetchLocationFromServer().execute("");
		View rootView = inflater.inflate(R.layout.klasemen, container,
				false);
		 mInflater = getLayoutInflater(savedInstanceState);

		lifePageCellContainerLayout = (LinearLayout) rootView
				.findViewById(R.id.location_linear_parentview);
	
		return rootView;
	}
	
	private class fetchLocationFromServer extends
	AsyncTask<String, Void, String> {

	@Override
	protected void onPreExecute() {
		
	}

	@Override
	protected String doInBackground(String... params) {
		String result = WebHTTPMethodClass.httpGetServiceWithoutparam("/restapi/get/classement");
		return result;
	}

	@Override
	protected void onProgressUpdate(Void... values) {

	}

	@Override
	protected void onPostExecute(String result) {
		
			try {

	            JSONArray jsonArray = new JSONArray(result);
	            
	            listThisWeekBean = new ArrayList<clasementBean>();
				for (int i = 0; i < jsonArray.length(); i++) {
	                JSONObject resObject = jsonArray.getJSONObject(i);
	                clasementBean thisWeekBean = new clasementBean();
	                thisWeekBean.setTeamname(resObject.getString("team_name"));
	                thisWeekBean.setP(resObject.getString("p"));
	                thisWeekBean.setW(resObject.getString("w"));
	                thisWeekBean.setD(resObject.getString("d"));
	                thisWeekBean.setL(resObject.getString("l"));
	                thisWeekBean.setGD(resObject.getString("gd"));
	                thisWeekBean.setPts(resObject.getString("pts"));
	                thisWeekBean.setPosition(resObject.getString("position"));
	                
					listThisWeekBean.add(thisWeekBean);
	            }
				if (listThisWeekBean != null
						&& listThisWeekBean.size() > 0) {

					createSelectLocationListView(listThisWeekBean);
				}

				
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

	@SuppressWarnings("deprecation")
	private void createSelectLocationListView(
			List<clasementBean> listThisWeekBean) {
		for (int i = 0; i < listThisWeekBean.size(); i++) {
			clasementBean thisWeekBean = listThisWeekBean.get(i);
			
				View cellViewMainLayout = mInflater
					.inflate(R.layout.klasemen_list, null);
				TextView Position = (TextView) cellViewMainLayout
						.findViewById(R.id.pos);
				TextView nameTeam = (TextView) cellViewMainLayout
						.findViewById(R.id.name);
				TextView cellnumTextView = (TextView) cellViewMainLayout
						.findViewById(R.id.findzoes_list_text_cellnum);
				TextView P = (TextView) cellViewMainLayout
						.findViewById(R.id.p);
				TextView W = (TextView) cellViewMainLayout
						.findViewById(R.id.w);
				TextView D = (TextView) cellViewMainLayout
						.findViewById(R.id.d);
				TextView L = (TextView) cellViewMainLayout
						.findViewById(R.id.l);
				TextView GD = (TextView) cellViewMainLayout
						.findViewById(R.id.gd);
				TextView Pts = (TextView) cellViewMainLayout
						.findViewById(R.id.pts);
				
				Position.setText("");
				nameTeam.setText("");
				P.setText("");
				W.setText("");
				D.setText("");
				L.setText("");
				D.setText("");
				GD.setText("");
				D.setText("");
				Pts.setText("");
				cellnumTextView.setText("");
				
				Position.setText(thisWeekBean.getPosition());
				nameTeam.setText(thisWeekBean.getTeamname());
				P.setText(thisWeekBean.getP());
				W.setText(thisWeekBean.getW());
				D.setText(thisWeekBean.getD());
				L.setText(thisWeekBean.getL());
				GD.setText(thisWeekBean.getGD());
				Pts.setText(thisWeekBean.getPts());
		     			
				lifePageCellContainerLayout.addView(cellViewMainLayout);		
		}
	 }

   }
	
}
