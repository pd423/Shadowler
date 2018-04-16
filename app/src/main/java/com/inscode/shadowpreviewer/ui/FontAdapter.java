package com.inscode.shadowpreviewer.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FontAdapter extends ArrayAdapter<FontAdapter.FontPair>{

    private LayoutInflater mInflater;
    private int mResource;

    public FontAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mResource = resource;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (null == convertView) {
            convertView = mInflater.inflate(mResource, null);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        FontPair fontPair = getItem(position);
        if (null != fontPair) {
            textView.setText(!TextUtils.isEmpty(fontPair.fontName) ? fontPair.fontName : "");
            textView.setTypeface(fontPair.typeface);
        }

        return convertView;
    }

    public static class FontPair {

        public String fontName;
        public Typeface typeface;

        public FontPair(String fontName, Typeface typeface) {
            this.fontName = fontName;
            this.typeface = typeface;
        }
    }
}
