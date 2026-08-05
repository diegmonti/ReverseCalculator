package org.frugo.reversecalculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.frugo.reversecalculator.databinding.GuideBottomSheetBinding;

public class GuideBottomSheet extends BottomSheetDialogFragment {
    private GuideBottomSheetBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = GuideBottomSheetBinding.inflate(inflater, container, false);
        binding.guideText.setText(HtmlCompat.fromHtml(
                getString(R.string.guide_text), HtmlCompat.FROM_HTML_MODE_LEGACY));
        ViewCompat.setAccessibilityHeading(binding.guideTitle, true);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
