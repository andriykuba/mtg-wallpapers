package com.github.andriykuba.mtgwallpapers.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.andriykuba.mtgwallpapers.Dialog;
import com.github.andriykuba.mtgwallpapers.R;
import com.github.andriykuba.mtgwallpapers.Screen;
import com.github.andriykuba.mtgwallpapers.Toolbar;
import com.github.andriykuba.mtgwallpapers.download.ListDownloader;

public class GalleryActivity extends AppCompatActivity {
    private Screen screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        screen = new Screen(this);

        Toolbar.initChild(this);

        initElements();
    }

    private void initElements() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.galleryRecyclerView);

        if (recyclerView == null) {
            Dialog.show(this, getString(R.string.gallery_no_image_view));
        } else {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);

            final Activity context = this;
            final GalleryAdapter adapter = new GalleryAdapter(this);
            recyclerView.setAdapter(adapter);

            final EndlessRecyclerViewScrollListener scrollListener =
                    new EndlessRecyclerViewScrollListener(layoutManager) {
                        @Override
                        public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                            new ListDownloader(context, screen, adapter).execute();
                        }
                    };
            recyclerView.addOnScrollListener(scrollListener);

            new ListDownloader(this, screen, adapter).execute();

        }
    }
}
