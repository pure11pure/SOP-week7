package com.example.week7.pojo;

import com.vaadin.flow.component.template.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data // สร้างต่าง ๆ อัตโนมัติเช่น getter, setter, toString, hashCode, และ equals เพื่อลดจำนวนโค้ดที่ต้องเขียนเอง
@Document("Product")
public class Product implements Serializable {
    @Id
    private String _id;
    private String productName;
    private double productCost;
    private double productProfit;
    private double productPrice;

    public Product(){};

    public Product(String _id, String productName, double productCost, double productProfit, double productPrice){
        this._id = _id;
        this.productName = productName;
        this.productCost = productCost;
        this.productProfit = productProfit;
        this.productPrice = productPrice;
    }
}
