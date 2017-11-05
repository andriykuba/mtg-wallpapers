package com.github.andriykuba.mtgwallpapers.gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.andriykuba.mtgwallpapers.MetaData;
import com.github.andriykuba.mtgwallpapers.R;
import com.github.andriykuba.mtgwallpapers.Screen;
import com.github.andriykuba.mtgwallpapers.Wallpaper;
import com.github.andriykuba.mtgwallpapers.change.Changer;

import java.util.LinkedList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private final List<MetaData> metaDataList;
    private final Context context;
    private final Screen screen;
    private int page;

    public GalleryAdapter(final Context context) {
        this.context = context;
        this.metaDataList = new LinkedList<>();
        this.screen = new Screen(context);
    }

    public void appendData(final List<MetaData> data) {
        this.metaDataList.addAll(data);
        page++;
    }

    public int getPage() {
        return page;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.gallery_image, parent, false);
        return new ViewHolder(photoView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MetaData data = metaDataList.get(position);
        Wallpaper.show(context, holder.itemView, data);

        Button buttonSet = (Button) holder.itemView.findViewById(R.id.buttonSet);
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Changer(context, screen, data).execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (metaDataList.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final View itemView;

        ViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        @Override
        public void onClick(View view) {
        }
    }
}