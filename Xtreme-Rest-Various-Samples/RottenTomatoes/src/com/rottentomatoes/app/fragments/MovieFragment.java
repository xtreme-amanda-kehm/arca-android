package com.rottentomatoes.app.fragments;

import java.util.Arrays;
import java.util.Collection;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.rottentomatoes.app.R;
import com.rottentomatoes.app.datasets.MovieTable;
import com.rottentomatoes.app.providers.RottenTomatoesContentProvider;
import com.xtreme.rest.adapters.ItemCursorAdapter;
import com.xtreme.rest.binders.Binding;
import com.xtreme.rest.binders.ViewBinder;
import com.xtreme.rest.dispatcher.Error;
import com.xtreme.rest.dispatcher.Query;
import com.xtreme.rest.dispatcher.QueryResult;
import com.xtreme.rest.fragments.RestItemSupportFragment;
import com.xtremelabs.imageutils.ImageLoader;

public class MovieFragment extends RestItemSupportFragment implements ViewBinder {
	
	private static final Collection<Binding> BINDINGS = Arrays.asList(new Binding[] { 
		new Binding(R.id.movie_title, MovieTable.Columns.TITLE.name),
		new Binding(R.id.movie_year, MovieTable.Columns.YEAR.name),
		new Binding(R.id.movie_mpaa_rating, MovieTable.Columns.MPAA_RATING.name),
		new Binding(R.id.movie_runtime, MovieTable.Columns.RUNTIME.name),
		new Binding(R.id.movie_critics_consensus, MovieTable.Columns.CRITICS_CONSENSUS.name),
		new Binding(R.id.movie_synopsis, MovieTable.Columns.SYNOPSIS.name),
		new Binding(R.id.movie_image, MovieTable.Columns.IMAGE_URL.name),
	});

	private String mId;
	private ImageLoader mImageLoader;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_movie, container, false);
		mImageLoader = ImageLoader.buildImageLoaderForSupportFragment(this);
		return view;
	}
	
	@Override
	public ItemCursorAdapter onCreateAdapter(final View view, final Bundle savedInstanceState) {
		final ItemCursorAdapter adapter = new ItemCursorAdapter(getView(), BINDINGS);
		adapter.setViewBinder(this);
		return adapter;
	}

	private void loadMovie(final String id) {
		final Uri baseUri = RottenTomatoesContentProvider.Uris.MOVIES_URI;
		final Uri contentUri = Uri.withAppendedPath(baseUri, id);
		final Query query = new Query(contentUri);
		
		execute(query);
	}
	
	public void setId(final String id) {
		mId = id;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		loadMovie(mId);
	}
	
	@Override
	public void onDestroyView() {
		mImageLoader.destroy();
		super.onDestroyView();
	}
	
	@Override
	protected void onContentError(final Error error) {
		final String message = error.getMessage();
		Toast.makeText(getActivity(), "ERROR: " + message, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	protected void onContentChanged(final QueryResult result) {
		final ItemCursorAdapter adapter = getItemAdapter();
		if (adapter.hasResults()) {
			showResults();
		} else if (!result.isRefreshing()) {
			hideLoading();
		}
	}
	
	private void showResults() {
		getView().findViewById(R.id.movie_container).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}
	
	private void hideLoading() {
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}
	
	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final Binding binding) {
		switch (view.getId()) {
		case R.id.movie_image:
		    final String url = cursor.getString(binding.getColumnIndex());
		    mImageLoader.loadImage((ImageView) view, url);
		    return true;

		default:
		    return false;
		}
	}
	
}