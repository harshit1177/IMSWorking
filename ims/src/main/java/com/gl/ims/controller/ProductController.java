package com.gl.ims.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.gl.ims.dto.ProductDto;
import com.gl.ims.entity.Product;
import com.gl.ims.service.ProductService;

@Controller
public class ProductController {

	// inject service layer dependency
	ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	// mapping to fetch all the products in the database
	@GetMapping("/allProducts")
	public String products(Model model, String keyword) {

		List<ProductDto> products = productService.findAllProducts();
		model.addAttribute("productAttribute", products);
		return "allProducts";
	}

	// get a product form to enter the details.
	@GetMapping("/product/new")
	public String createProductForm(Model model) {
		Product product = new Product();
		model.addAttribute("productAttribute", product);
		return "createProduct";
	}

	// save the product details
	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute("productAttribute") Product product) {
		productService.saveProduct(product);
		return "redirect:/allProducts";
	}

	// mapping for delete functionality
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return "redirect:/allProducts";
	}

	// mapping for update returning the form
	@GetMapping("/products/edit/{id}")
	public String editProductForm(@PathVariable Long id, Model model) {
		model.addAttribute("productAttribute", productService.getProductById(id));
		return "updateProduct";
	}

	@PostMapping("/updateProduct/{id}")
	// 2 --> path variable, form data, model(to get data from vl to cl)
	public String updateProduct(@PathVariable Long id, @ModelAttribute("productAttribute") Product product) {

		// find the existing product
		Product existingProduct = productService.getProductById(id);
		existingProduct.setId(id);
		existingProduct.setProductName(product.getProductName());
		existingProduct.setProductDescription(product.getProductDescription());
		existingProduct.setCurrentQuantity(product.getCurrentQuantity());

		productService.saveProduct(existingProduct);
		return "redirect:/allProducts";

	}

}
