package com.inscode.shadowpreviewer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.inscode.shadowpreviewer.ui.FontAdapter;

import java.util.Map;

public class FontSelectDialogFragment extends DialogFragment {

    interface Callback {
        void onSelectNewFont(Typeface typeface);
    }

    public static final String TAG = "FontSelectDialogFragment";

    private Callback mCallback;
    
    public static FontSelectDialogFragment newInstance() {
        
        Bundle args = new Bundle();
        
        FontSelectDialogFragment fragment = new FontSelectDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (Callback) context;
        } catch (ClassCastException ignore) {}
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Map<String, Typeface> fontPairs = Util.getSSystemFontMap();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (null != fontPairs) {
            final FontAdapter adapter = new FontAdapter(getActivity(),
                    android.R.layout.simple_list_item_1);
            for (String key : fontPairs.keySet()) {
                adapter.add(new FontAdapter.FontPair(key, fontPairs.get(key)));
            }
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (null != mCallback) {
                        if (null != adapter.getItem(which)) {
                            mCallback.onSelectNewFont(adapter.getItem(which).typeface);
                        }
                    }
                }
            });
        } else {
            TextView textView = new TextView(getActivity());
            textView.setText(getString(R.string.cannot_fint_fonts));
            builder.setView(textView);
        }
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
