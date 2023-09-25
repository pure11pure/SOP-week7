package com.example.week7.view;


import com.example.week7.pojo.Product;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Route(value = "/view")
public class ProductView extends VerticalLayout {
    private ComboBox productList;
    private TextField productName;
    private NumberField productCost, productProfit, productPrice;
    private Button addProduct, updateProduct, deleteProduct, clearProduct;
    private Notification nf;

    public ProductView(){
        productList = new ComboBox();
        productList.setLabel("Product List");
        productList.setWidth("600px");
        productList.addValueChangeListener(event -> {
            System.out.println("productList ---- " + productList.getValue());
            if(productList.getValue() == null){
                return;
            }
            setProductDetail((Product) productList.getValue());
        });
        productList.addFocusListener(event -> {
            updateProductListCombo();
        });

        productName = new TextField("Product Name:");
        productName.setWidth("600px");

        productCost = new NumberField("Product Cost:");
        productCost.setWidthFull();
        productCost.setValue(0.0);
        productCost.setWidth("600px");
        productCost.addKeyDownListener(event -> {
//            System.out.println("ENTER ------ " + event.getCode());
            System.out.println("Cost_ENTER ------ " + (event.getCode().get().toString().equals("NumpadEnter") || event.getCode().get().toString().equals("Enter")));
            if(event.getCode().get().toString().equals("NumpadEnter") || event.getCode().get().toString().equals("Enter")){
                updateProductPriceField();
            }
        });

        productProfit = new NumberField("Product Profit:");
        productProfit.setWidthFull();
        productProfit.setValue(0.0);
        productProfit.setWidth("600px");
        productProfit.addKeyDownListener(event -> {
            System.out.println("Profit_ENTER ------ " + (event.getCode().get().toString().equals("NumpadEnter") || event.getCode().get().toString().equals("Enter")));
            if(event.getCode().get().toString().equals("NumpadEnter") || event.getCode().get().toString().equals("Enter")){
                updateProductPriceField();
            }
        });

        productPrice = new NumberField("Product Price:");
        productPrice.setWidthFull();
        productPrice.setEnabled(false);
        productPrice.setValue(0.0);
        productPrice.setWidth("600px");

        addProduct = new Button("Add Product");
        updateProduct = new Button("Update Product");
        deleteProduct = new Button("Delete Product");
        clearProduct = new Button("Clear Product");

        HorizontalLayout hButton = new HorizontalLayout();
        hButton.add(addProduct, updateProduct, deleteProduct, clearProduct);

        add(productList, productName, productCost, productProfit, productPrice, hButton);

        this.nf = new Notification();
        this.nf.setDuration(500); // 0.5 sec = 0.5 *1000 millisec

        addProduct.addClickListener((event) -> {
            Product newProduct = new Product(
                    null,
                    productName.getValue(),
                    productCost.getValue(),
                    productProfit.getValue(),
                    productPrice.getValue());

            System.out.println("addProduct --- " + newProduct);

            Boolean res = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addProduct")
                    .body(Mono.just(newProduct), Product.class)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            if (res) {
                updateProductListCombo();
                nf.setText("Product Add : " + productName.getValue());
                nf.open();
                productName.setValue("");
                productCost.setValue(0.0);
                productProfit.setValue(0.0);
                productPrice.setValue(0.0);
            }
        });

        updateProduct.addClickListener(event -> {
            Product updateProduct = (Product) productList.getValue();
            System.out.println("updateProduct ---- " + updateProduct);
            updateProduct.setProductName(productName.getValue());
            updateProduct.setProductCost(productCost.getValue());
            updateProduct.setProductProfit(productProfit.getValue());
            updateProduct.setProductPrice(productPrice.getValue());

            Boolean res = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateProduct")
                    .body(Mono.just(updateProduct), Product.class)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            if (res) {
                updateProductListCombo();
                nf.setText("Product Update : " + productName.getValue());
                nf.open();
            }
        });

        deleteProduct.addClickListener(event -> {
            Product deleteProduct = (Product) productList.getValue();
            System.out.println("deleteProduct ---- " + deleteProduct);
            Boolean res = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/deleteProduct")
                    .body(Mono.just(deleteProduct), Product.class)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();


            if(res){
                updateProductListCombo();
                nf.setText("Product Delete : " + productName.getValue());
                nf.open();
                productName.setValue("");
                productCost.setValue(0.0);
                productProfit.setValue(0.0);
                productPrice.setValue(0.0);
            }
        });

        clearProduct.addClickListener(event -> {
            productList.setValue(null);
            productName.setValue("");
            productCost.setValue(0.0);
            productProfit.setValue(0.0);
            productPrice.setValue(0.0);
        });
    }

    public void setProductDetail(Product p) {
        productName.setValue(p.getProductName());
        productCost.setValue(p.getProductCost());
        productProfit.setValue(p.getProductProfit());
    }

    public void updateProductListCombo() {
//        System.out.println("KEY : ");
        List<Product> products = WebClient.create()
                .get()
                .uri("http://localhost:8080/getAll")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ArrayList<Product>>() {
                })
                .block();
//        System.out.println("* updateProductListCombo * ---- " + products);
        productList.setItems(products);
    }

    public void updateProductPriceField(){
        if(productCost.isInvalid() || productProfit.isInvalid()){
            productPrice.setValue(0.0);
            return;
        }
        Double res = WebClient.create()
                .get()
                .uri("http://localhost:8080/getPrice/" + productCost.getValue() + "/" + productProfit.getValue())
                .retrieve()
                .bodyToMono(Double.class)
                .block();
        productPrice.setValue(res);
    }
}
