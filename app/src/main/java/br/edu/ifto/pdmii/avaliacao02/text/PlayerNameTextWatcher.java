package br.edu.ifto.pdmii.avaliacao02.text;

import android.text.Editable;
import android.text.TextWatcher;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class PlayerNameTextWatcher implements TextWatcher {
    private final MaterialButton materialButton;
    private final SwitchMaterial switchMaterial;

    public PlayerNameTextWatcher(MaterialButton materialButton, SwitchMaterial switchMaterial) {
        this.materialButton = materialButton;
        this.switchMaterial = switchMaterial;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        materialButton.setEnabled(charSequence.length() > 0);
        switchMaterial.setEnabled(charSequence.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
