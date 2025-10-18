package com.oms.service;

import com.oms.entity.Inventory;
import com.oms.entity.LineCharges;
import com.oms.entity.OrderLine;
import com.oms.entity.Product;
import com.oms.entity.Shipping;
import com.oms.repository.OrderLineRepository;
import com.oms.repository.ProductRepository;
import com.oms.util.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Service
@Transactional
public class ProductService {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	InventoryService inventorySerice;
	
	@Autowired
	OrderLineRepository orderLineRepository;
	
	@Autowired
	ShippingService shippingService; // to check dead code....

    @Autowired
    Logger logger;

    
    public Product registerProduct(Product product) {
        logger.log(this.getClass().getName());
        Product response = productRepository.save(product);
        return response;   	
    }

    
    public Product getProductById(String pid) {
        logger.log(this.getClass().getName());
    	Optional<Product> findById = productRepository.findById(pid);
    	return findById.orElse(null);
    }
    

    public Product getProductByName(String name) {
        logger.log(this.getClass().getName());
    	Optional<Product> findByName = productRepository.findByName(name);
    	return findByName.orElse(null);
    	
    }
    
    public List<Product> getAllProducts() {
        logger.log(this.getClass().getName());
        return productRepository.findAll();
    }
    
    public List<Product> getProductsDescribedWith(String text) {
        logger.log(this.getClass().getName());
        return productRepository.findByDescriptionContainingIgnoreCase(text);
    }
    
    public Inventory getProductInventory(String pid) {
        logger.log(this.getClass().getName());
    	return inventorySerice.fetchInventory(pid);
    }
    
    public List<Inventory> getInventoriesDescribedWith(String text) {
        logger.log(this.getClass().getName());
    	List<Inventory> inventoryList = new LinkedList<>();
    	
    	List<Product> products = getProductsDescribedWith(text); 
    	for (Product p : products) {
    		inventoryList.add(getProductInventory(p.getProductId()));
    		
    	}
    	return inventoryList;
    }
    
    
    public List<OrderLine> getOrderLinesForProduct(String pid) {
        logger.log(this.getClass().getName());
        return orderLineRepository.findByCustomerSKU(pid);
    }
    

    public Double getTotalChargesForProduct(String pid) {
        logger.log(this.getClass().getName());
        double res = 0;
    	
        List<OrderLine> orderLines = getOrderLinesForProduct(pid);
        for (OrderLine orderLine : orderLines) {
        	LineCharges charges = orderLine.getCharges();
        	res += charges.getTotalCharges();
        }
        
    	return res;
    }
    
    public Shipping getShippingCosts(String pid) { // dead code.... testing only....
    	return shippingService.fetchShippingCharges(pid);
    }
    
    public void setLogger(Logger logger) {
        this.logger = logger;
    }
    
}
