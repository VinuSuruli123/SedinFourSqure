package com.sedin.task.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.sedin.task.bussinessobjects.VenueDetails;

public class SharedPreference {
	public static final String PREFS_NAME = "VenueDetails_APP";
	public static final String FAVORITES = "VenueDetails_Favorite";
	
	public SharedPreference() {
		super();
	}

	// This four methods are used for maintaining favorites.
	public void saveFavorites(Context context, List<VenueDetails> favorites) {
		SharedPreferences settings;
		Editor editor;

		settings = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		editor = settings.edit();

		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(favorites);

		editor.putString(FAVORITES, jsonFavorites);

		editor.commit();
	}

	public void addFavorite(Context context, VenueDetails VenueDetails) {
		List<VenueDetails> favorites = getFavorites(context);
		if (favorites == null)
			favorites = new ArrayList<VenueDetails>();
		favorites.add(VenueDetails);
		saveFavorites(context, favorites);
	}

	public void removeFavorite(Context context, VenueDetails VenueDetails) {
		ArrayList<VenueDetails> favorites = getFavorites(context);
		if (favorites != null) {
			favorites.remove(VenueDetails);
			saveFavorites(context, favorites);
		}
	}

	public ArrayList<VenueDetails> getFavorites(Context context) {
		SharedPreferences settings;
		List<VenueDetails> favorites;

		settings = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);

		if (settings.contains(FAVORITES)) {
			String jsonFavorites = settings.getString(FAVORITES, null);
			Gson gson = new Gson();
			VenueDetails[] favoriteItems = gson.fromJson(jsonFavorites,
					VenueDetails[].class);

			favorites = Arrays.asList(favoriteItems);
			favorites = new ArrayList<VenueDetails>(favorites);
		} else
			return null;

		return (ArrayList<VenueDetails>) favorites;
	}
}
