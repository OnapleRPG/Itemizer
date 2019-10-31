package com.onaple.itemizer.utils;

import org.spongepowered.api.text.Text;

public interface ItemDataManager {
    Text showResume();
    Text showModifyButton();
    void apply();
    boolean supports();

}
