package com.example.week7.controller;

import com.example.week7.pojo.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class ProductController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public boolean serviceAddProduct(@RequestBody Product p){
        return (boolean) rabbitTemplate.convertSendAndReceive("ProductExchange", "add", p);
    }
    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public boolean serviceUpdateProduct(@RequestBody Product p){
        return (boolean) rabbitTemplate.convertSendAndReceive("ProductExchange", "update", p);
    }
    @RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
    public boolean serviceDeleteProduct(@RequestBody Product p){
        System.out.println("/deleteProduct --- " + p);
        return (boolean) rabbitTemplate.convertSendAndReceive("ProductExchange", "delete", p);
    }
    @RequestMapping(value = "/getProductName", method = RequestMethod.POST)
    public Product serviceGetProductName(@RequestBody Product p){
        return (Product) rabbitTemplate.convertSendAndReceive("MyDirectExchange", "getname", p);
    }
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public List<Product> serviceGetAllProduct(){
            return (List<Product>) rabbitTemplate.convertSendAndReceive("ProductExchange", "getall", "");
    }


}
