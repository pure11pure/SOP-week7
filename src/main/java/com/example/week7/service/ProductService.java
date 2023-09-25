package com.example.week7.service;

import com.example.week7.pojo.Product;
import com.example.week7.repository.ProductRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @RabbitListener(queues = "AddProductQueue")
    public boolean addProduct(Product p){
        try {
            repository.save(p);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @CachePut(value = "products")
    //อัพเดตข้อมูลในแคช "products" หลังจากที่เมธอดทำการอัพเดตข้อมูลสินค้าในฐานข้อมูล MongoDB ผ่าน ProductRepository.
    @RabbitListener(queues = "UpdateProductQueue")
    public boolean updateProduct(Product p){
        try {
            repository.save(p);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @CacheEvict(value = "products")
    //จะลบข้อมูลแคช "products" หลังจากที่เมธอดทำการลบข้อมูลสินค้าในฐานข้อมูล MongoDB ผ่าน ProductRepository.
    @RabbitListener(queues = "DeleteProductQueue")
    public boolean deleteProduct(Product p){
        System.out.println("RabbitListen --- " + p);
        try {
            repository.delete(p);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    @Cacheable(value = "products")
    //จะถูกแคช (caching) ด้วยชื่อ "products".
    // หากข้อมูลสินค้าถูกค้นหาแล้วไม่มีในแคช,
    // เมธอด getAllProduct จะถูกเรียกเพื่อดึงข้อมูลสินค้าจากฐานข้อมูล MongoDB
    // ผ่าน ProductRepository และจะถูกเก็บในแคช.
    @RabbitListener(queues = "GetAllProductQueue")
    public ArrayList<Product> getAllProduct(){
        return new ArrayList<>(repository.findAll());
    }

    @RabbitListener(queues = "GetNameProductQueue")
    public Product getProductByName(Product p){
        return repository.findByName(p.getProductName());
    }

}
