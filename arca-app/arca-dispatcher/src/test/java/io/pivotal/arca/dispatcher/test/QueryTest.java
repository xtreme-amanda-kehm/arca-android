/* 
 * Copyright (C) 2014 Pivotal Software, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.arca.dispatcher.test;

import android.net.Uri;
import android.os.Parcel;
import android.test.AndroidTestCase;

import io.pivotal.arca.dispatcher.Query;

import java.util.Arrays;

public class QueryTest extends AndroidTestCase {

	public void testQueryParcelableDescribeContents() {
		final Uri uri = Uri.parse("content://empty");
		final Query request = new Query(uri);
		assertEquals(0, request.describeContents());
	}

	public void testQueryParcelableCreatorArray() {
		final Query[] request = Query.CREATOR.newArray(1);
		assertEquals(1, request.length);
	}

	public void testQueryParcelableCreator() {
		final Uri uri = Uri.parse("content://empty");
		final String[] projection = { "_id" };
		final String where = "test = ?";
		final String[] whereArgs = { "true" };
		final String sortOrder = "_id desc";
		final boolean forceUpdate = true;

		final Query request = new Query(uri);
		request.setProjection(projection);
		request.setWhere(where, whereArgs);
		request.setSortOrder(sortOrder);
		request.setForceUpdate(forceUpdate);

		final Parcel parcel = Parcel.obtain();
		request.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		final Query parceled = Query.CREATOR.createFromParcel(parcel);
		assertEquals(uri, parceled.getUri());
		assertTrue(Arrays.deepEquals(projection, parceled.getProjection()));
		assertEquals(where, parceled.getWhereClause());
		assertTrue(Arrays.deepEquals(whereArgs, parceled.getWhereArgs()));
		assertEquals(sortOrder, parceled.getSortOrder());
		assertEquals(forceUpdate, parceled.shouldForceUpdate());

		parcel.recycle();
	}
}
