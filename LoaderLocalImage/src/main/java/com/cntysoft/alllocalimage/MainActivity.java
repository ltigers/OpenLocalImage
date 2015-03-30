package com.cntysoft.alllocalimage;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;


public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private GridView gridView= null;
    private SimpleCursorAdapter simpleCursorAdapter = null;
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView =(GridView)findViewById(R.id.gridView1);

        simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.griditem_addpic,
                null,
                STORE_IMAGES,
                new int[] {R.id.imageView1},
                0
                );
        simpleCursorAdapter.setViewBinder(new ImageBinder());
        gridView.setAdapter(simpleCursorAdapter);
        getSupportLoaderManager().initLoader(0,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(
                MainActivity.this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                STORE_IMAGES,
                null,
                null,
                null
                );
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }

    private class ImageBinder implements SimpleCursorAdapter.ViewBinder{

        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            String pathImage = cursor.getString(columnIndex);
            Bitmap bitmap= BitmapFactory.decodeFile(pathImage);
            ((ImageView)view).setImageBitmap(bitmap);
            return true;
        }
    }
}
