package com.fsq.pobop.entity.ingredient;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.fsq.pobop.data.DateConverter;

import java.time.LocalDate;

@Entity
public class Ingredient {

    @PrimaryKey
    @NonNull
    private String id = ObjectId.get().toString();

    @ColumnInfo(name = "name")
    private String productName;

    @ColumnInfo(name = "expiry_date")
    private LocalDate expiryDate;

    @ColumnInfo(name = "barcode")
    private String barcode;

    @ColumnInfo(name = "product_type")
    private String productType;

    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @ColumnInfo(name = "dirty")
    private int dirty;

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDirty() {
        return dirty;
    }

    public void setDirty(int dirty) {
        this.dirty = dirty;
    }

    public JSONObject toJson() {
        DateConverter dateConverter = new DateConverter();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getId());
            jsonObject.put("expiry_date", dateConverter.dateToString(getExpiryDate()));
            jsonObject.put("barcode", getBarcode());
            jsonObject.put("name", getProductName());
            jsonObject.put("product_type", getProductType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
