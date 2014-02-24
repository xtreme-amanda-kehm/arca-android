package com.arca.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class SQLTable extends SQLDataset {
	
	@Override
	public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		final SQLiteDatabase database = getDatabase();
		if (database != null) {
			return database.update(getName(), values, selection, selectionArgs);
		} else {
			throw new IllegalStateException("Database is null.");
		}
	}

	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
		final SQLiteDatabase database = getDatabase();
		if (database != null) {
			return database.delete(getName(), selection, selectionArgs);
		} else {
			throw new IllegalStateException("Database is null.");
		}
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		final SQLiteDatabase database = getDatabase();
		if (database != null) {
			final long id = database.insert(getName(), null, values);
			return ContentUris.withAppendedId(uri, id);
		} else {
			throw new IllegalStateException("Database is null.");
		}
	}
	
	@Override
	public int bulkInsert(final Uri uri, final ContentValues[] values) {
		final SQLiteDatabase database = getDatabase();
		if (database != null) {
			return insertWithTransaction(database, getName(), values);
		} else {
			throw new IllegalStateException("Database is null.");
		}
	}

	private static int insertWithTransaction(final SQLiteDatabase database, final String name, final ContentValues[] values) {
		database.beginTransaction();
		try {
			final int inserted = insert(database, name, values);
			database.setTransactionSuccessful();
			return inserted;
		} finally {
			database.endTransaction();
		}
	}

	private static int insert(final SQLiteDatabase database, final String name, final ContentValues[] values) {
		int inserted = 0;
		for (final ContentValues value : values) {
			if (database.insert(name, null, value) > -1) {
				inserted++;
			}
		}
		return inserted;
	}
}