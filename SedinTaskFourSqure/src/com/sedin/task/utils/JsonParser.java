package com.sedin.task.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.sedin.task.bussinessobjects.VenueDetails;

import android.util.Log;

public class JsonParser {
	public ArrayList<VenueDetails> parseJson(final String response) {

		ArrayList<VenueDetails> temp = new ArrayList<VenueDetails>();
		try {

			JSONObject jsonObject = new JSONObject(response);

			if (jsonObject.has("response")) {
				if (jsonObject.getJSONObject("response").has("venues")) {
					JSONArray jsonArray = jsonObject.getJSONObject("response")
							.getJSONArray("venues");

					for (int i = 0; i < jsonArray.length(); i++) {
						VenueDetails mDetails = new VenueDetails();
						if (jsonArray.getJSONObject(i).has("name")) {
							mDetails.setName(jsonArray.getJSONObject(i)
									.getString("name"));

							if (jsonArray.getJSONObject(i).has("location")) {
								if (jsonArray.getJSONObject(i)
										.getJSONObject("location")
										.has("address")) {
									mDetails.setAddress(jsonArray
											.getJSONObject(i)
											.getJSONObject("location")
											.getString("address"));
									if (jsonArray.getJSONObject(i)
											.getJSONObject("location")
											.has("city")) {
										mDetails.setCity(jsonArray
												.getJSONObject(i)
												.getJSONObject("location")
												.getString("city"));

										mDetails.setLat(jsonArray
												.getJSONObject(i)
												.getJSONObject("location")
												.getString("lat"));
										mDetails.setLang(jsonArray
												.getJSONObject(i)
												.getJSONObject("location")
												.getString("lng"));
										Log.v("get lat values", "========"
												+ mDetails.getLat());

									}
									if (jsonArray.getJSONObject(i).has(
											"categories")) {
										if (jsonArray.getJSONObject(i)
												.getJSONArray("categories")
												.length() > 0) {
											if (jsonArray.getJSONObject(i)
													.getJSONArray("categories")
													.getJSONObject(0)
													.has("icon")) {
												mDetails.setCategory(jsonArray
														.getJSONObject(i)
														.getJSONArray(
																"categories")
														.getJSONObject(0)
														.getString("name"));
											}
										}
									}
									temp.add(mDetails);
								}
							}
						}
						// temp.add(mDetails);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
		return temp;

	}

}
