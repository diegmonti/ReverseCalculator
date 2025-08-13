package org.frugo.reversecalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;

import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class GuideBottomSheet extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.guide_bottom_sheet, container, false);
        TextView guideText = view.findViewById(R.id.guideText);
        guideText.setText(HtmlCompat.fromHtml(getString(R.string.guide_text), HtmlCompat.FROM_HTML_MODE_LEGACY));
        return view;
    }
}