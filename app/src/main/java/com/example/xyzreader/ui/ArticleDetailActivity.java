package com.example.xyzreader.ui;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.example.xyzreader.util.Utils;

/**
 * An activity representing a single Article detail screen, letting you swipe between articles.
 */
public class ArticleDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private Cursor mCursor;
    private long mSelectedItemId;

    private ImageView mDetailsImage;
    private TextView mTitle;
    private TextView mAuthor;
    private TextView mDate;
    private TextView mBodyText;
    private FloatingActionButton mShareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Toolbar toolbar = findViewById(R.id.details_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDetailsImage = findViewById(R.id.details_image);
        mTitle = findViewById(R.id.details_title);
        mAuthor = findViewById(R.id.details_author);
        mDate = findViewById(R.id.details_date);
        mBodyText = findViewById(R.id.details_text);
        mShareButton = findViewById(R.id.share_fab);

        if (getIntent() != null && getIntent().getData() != null) {
            mSelectedItemId = ItemsContract.Items.getItemId(getIntent().getData());
        }

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return ArticleLoader.newInstanceForItemId(this, mSelectedItemId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mCursor = cursor;

        if (cursor != null && cursor.moveToFirst()) {
            Glide.with(this).load(cursor.getString(ArticleLoader.Query.PHOTO_URL)).into(mDetailsImage);

            mTitle.setText(cursor.getString(ArticleLoader.Query.TITLE));
            mAuthor.setText(getString(R.string.by_author, cursor.getString(ArticleLoader.Query.AUTHOR)));
            mDate.setText(Utils.getFormattedDate(cursor.getString(ArticleLoader.Query.PUBLISHED_DATE)));
            final String bodyText = mCursor.getString(ArticleLoader.Query.BODY).replaceAll("(\r\n|\n)", "<br />");
            mBodyText.setText(Html.fromHtml(bodyText));
            mShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareText(bodyText);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mCursor = null;
    }

    private void shareText(String text) {
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(text)
                .getIntent(), getString(R.string.action_share)));
    }
}
