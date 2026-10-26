package com.example.menu;

public abstract class ShoppingDisplayItem {
    protected String store;

    public String getStore() { return store; }

    public static class Header extends ShoppingDisplayItem {
        private String title;

        public Header(String title, String store) {
            this.title = title;
            this.store = store;
        }

        public String getTitle() { return title; }
    }

    public static class Product extends ShoppingDisplayItem {
        private String displayText;
        private boolean isChecked;

        public Product(String displayText, String store, boolean isChecked) {
            this.displayText = displayText;
            this.store = store;
            this.isChecked = isChecked;
        }

        public String getDisplayText() { return displayText; }
        public boolean isChecked() { return isChecked; }
        public void setChecked(boolean checked) { isChecked = checked; }
    }
}