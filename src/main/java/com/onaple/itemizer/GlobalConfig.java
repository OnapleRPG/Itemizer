package com.onaple.itemizer;

public class GlobalConfig {
private boolean descriptionRewrite;

    public GlobalConfig(boolean descriptionRewrite) {
        this.descriptionRewrite = descriptionRewrite;
    }

    public boolean isDescriptionRewrite() {
        return descriptionRewrite;
    }

    public void setDescriptionRewrite(boolean descriptionRewrite) {
        this.descriptionRewrite = descriptionRewrite;
    }
}
