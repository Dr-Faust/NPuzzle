package com.alex_podolian.npuzzle.model.callbacks;

import com.google.android.material.textfield.TextInputLayout;

public interface OnCreateMap {
	void onMapCreated(TextInputLayout[][] matrix, int textSize);
}
