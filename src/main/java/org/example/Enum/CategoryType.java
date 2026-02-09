package org.example.Enum;

public enum CategoryType {
    ENTERTAINMENT("ENTERTAINMENT"),
    SHOPPING("SHOPPING"),
    CAFE("CAFE"),
    RESTOURANT("RESTOURANT"),
    PRODUCTS("PRODUCTS"),
    OTHER("Другое");

    private final String category;

    CategoryType(String category){
        this.category = category;
    }

    public String getCategoryType(){
        return category;
    }
}
