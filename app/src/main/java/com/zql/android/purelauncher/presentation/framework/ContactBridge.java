/*******************************************************************************
 *    Copyright 2017-present, PureLauncher Contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/

package com.zql.android.purelauncher.presentation.framework;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.zql.android.purelauncher.R;
import com.zql.android.purelauncher.adapter.model.Action.ContactAction;
import com.zql.android.purelauncher.adapter.model.processor.ContactProcessor;
import com.zql.android.purelauncher.presentation.LauncherApplication;
import com.zql.android.purelauncher.presentation.ui.customview.drawable.CircleDrawable;
import com.zqlite.android.logly.Logly;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qinglian.zhang, created on 2017/4/17.
 */
public class ContactBridge implements ContactProcessor.Bridge {


    private int size = 1024 * 1024 * 4;
    private LruCache<String, Bitmap> contactPhotoCache;

    public ContactBridge() {

        init();
    }

    @Override
    public void init() {
        contactPhotoCache = new LruCache<String, Bitmap>(size) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    @Override
    public List<ContactAction> getContactActions(String key) {

        List<ContactAction> contactActions = new ArrayList<>();

        ContentResolver contentResolver = LauncherApplication.own().getContentResolver();
        try {


            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, key);
            Logly.d("uri = " + uri.toString());
            Cursor cursor = contentResolver.query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String lookup = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        ContactAction contactAction = new ContactAction();
                        contactAction.displayName = name;
                        contactAction.contactId = contactId;
                        contactAction.lookupKey = lookup;
                        contactActions.add(contactAction);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contactActions;
    }

    @Override
    public void loadContactPhoto(final String id, final Object imageView) {
        AsyncTask<Void,Void,Bitmap> asyncTask = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap bitmap = contactPhotoCache.get(id);
                if (bitmap != null) return bitmap;

                ContentResolver contentResolver = LauncherApplication.own().getContentResolver();
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, id);
                Logly.d("photo uri = " + uri.toString());
                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, uri, true);
                bitmap = BitmapFactory.decodeStream(inputStream);

                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(LauncherApplication.own().getResources(), R.drawable.ic_seacher_thumb_person);
                }
                contactPhotoCache.put(id, bitmap);
                return bitmap;

            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if(((ImageView) imageView).getTag(R.id.search_asynctask) == this){
                    ((ImageView) imageView).setImageBitmap(bitmap);
                }
            }
        };
        ((ImageView) imageView).setTag(R.id.search_asynctask,asyncTask);
        asyncTask.execute();
    }

    @Override
    public void openContact(String lookupKey, String contactId) {
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey + "/" + contactId);

        Intent intent = new Intent(ContactsContract.QuickContact.ACTION_QUICK_CONTACT);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setSourceBounds(new Rect(0, 0, 0, 0));

        ComponentName componentName = intent.resolveActivity(LauncherApplication.own().getPackageManager());
        if (componentName != null) {
            LauncherApplication.own().startActivity(intent);
        }

        intent.setAction(Intent.ACTION_VIEW);
        componentName = intent.resolveActivity(LauncherApplication.own().getPackageManager());
        if (componentName != null) {
            LauncherApplication.own().startActivity(intent);
        }
    }
}
