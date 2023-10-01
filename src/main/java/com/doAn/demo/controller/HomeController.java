package com.doAn.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.doAn.demo.entity.Category;
import com.doAn.demo.entity.Customers;
import com.doAn.demo.entity.Product;
import com.doAn.demo.service.CartService;
import com.doAn.demo.service.CategoryService;
import com.doAn.demo.service.CookieService;
import com.doAn.demo.service.CustomerService;
import com.doAn.demo.service.FileUploadService;
import com.doAn.demo.service.ProductService;

import jakarta.mail.Session;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private CategoryService cateService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CookieService cookieService;
	@Autowired
	private HttpSession sesion;
	@Autowired
	ServletContext context;
	@Autowired 
	private CartService cartService;
	@Autowired
	CustomerService customerService;
	@Autowired
	EmailSenderServices emailService;
	@Autowired
	FileUploadService fileUploadService;
	public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";

	@GetMapping("/")
	public String indexPage(Model model) {	
		//best sell
		List<Product> bestSell = new ArrayList<>();
		for(int i=0; i< 5; i++) {
			if(i == productService.getBestSell().size() -1) break;
			Product p = productService.getBestSell().get(i);
			bestSell.add(p);
		}
		model.addAttribute("listBestSeller", bestSell);
		
		//newest
		List<Product> newest = new ArrayList<>();
		for(int i=0; i< 5; i++) {
			if(i == productService.getNewest().size() -1) break;
			Product p = productService.getNewest().get(i);
			newest.add(p);
		}
		model.addAttribute("listNewest", newest);
		
		//Most Interest
		List<Product> inter = new ArrayList<>();
		for(int i =0; i< 5;i++) {
			if(i == productService.getMostInterest().size() -1) break;
			Product p = productService.getMostInterest().get(i);
			inter.add(p);
		}
		model.addAttribute("listInterest", inter);
		
		//Most Discount
		List<Product> dis = new ArrayList<>();
		for(int i =0; i< 5;i++) {
			if(i == (productService.getMostDiscount()).size() -1) break;
			System.out.println((productService.getMostDiscount()).size());
			Product p = productService.getMostDiscount().get(i);
			dis.add(p);
		}
		model.addAttribute("listDiscount", dis);
		
		//Special
		List<Product> spec = new ArrayList<>();
		for(int i =0; i< 5;i++) {
			if(i == productService.findBySpecial().size() -1) break;
			Product p = productService.findBySpecial().get(i);
			spec.add(p);
		}
		
		model.addAttribute("listSpec", spec);
		sesion.setAttribute("category", cateService.findAll());
		sesion.removeAttribute("Admin");
		sesion.removeAttribute("employeeLogin");
		return "home/index";
	}

	@GetMapping("/homeAdmin")
	public String homePageAdmin() {
		return "homeAdmin";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/listCategory")
	public String listCategory(Model model) {
		
		model.addAttribute("categoryList",(List<Category>) sesion.getAttribute("category"));
		model.addAttribute("UPLOAD_DIRECTORY", UPLOAD_DIRECTORY + "\\");
		
		return "home/listCategory";
	}

	@GetMapping("/listCategory/{id}")
	public String getListProductByCategory(@PathVariable("id") int id,Model model) {
		List<Product> list = productService.findByCategory(id);
		model.addAttribute("listProduct", list);
		
		return "home/listProductByCategory";
	}
	
	@GetMapping("/listProduct/{id}")
	public String getProduct(@PathVariable("id") Integer id,Model model) {
		Product p = productService.findByID(id);
		p.setViews(p.getViews() + 1);
		System.out.println(p.getId());
		productService.save(p);
		List<Product> list = productService.findByCategory(p.getCategory().getId());
		model.addAttribute("product", p);
		model.addAttribute("listProduct", list);
		model.addAttribute("priceAfterDiscount",Math.round( p.getUnitprice() * (1-p.getDiscount())));
		
		//read cookie favorite
		Cookie favorite = cookieService.read("favorite");
		Boolean isFavo = false;
		if(favorite != null) {
			List<Product> pFavo = new ArrayList<Product>();
			String val = favorite.getValue();
			if(val.contains(String.valueOf(id))) isFavo = true;
			String[] ids = val.split(",");
			for(String idPFavo: ids) {
				pFavo.add(productService.findByID(Integer.valueOf(idPFavo)));
			}
			model.addAttribute("favorite", pFavo);
		}
		model.addAttribute("isFavo", isFavo);
		
		//create viewed list
		Cookie view = cookieService.read("view");
		String value = id.toString();
		if(view!= null) {
			value = view.getValue();
			if(!value.contains(id.toString())) value += "," +id.toString();
		}
		cookieService.create("view", value, 7);
		List<Product> viewList = new ArrayList<>();
		for(String ids: value.split(",")) {
			viewList.add(productService.findByID(Integer.valueOf(ids)));
		}
		model.addAttribute("viewList", viewList);
		
		
		return "home/detailProduct";
	}
	



	
	


	@GetMapping("/addToCookie/{id}")
	public String addToCookie(@PathVariable("id") Integer id,Model model) {
		Product p = productService.findByID(id);		
		Cookie favorite = cookieService.read("favorite");
		String val = id.toString();
		if(favorite != null) {
			
			val = favorite.getValue();
			if(!val.contains(id.toString())) {
				val += "," + id.toString();
			}
		}		
		cookieService.create("favorite", val, 30);
		String re = "redirect:/listCategory/" + p.getCategory().getId();
		
		return re;
	}
	
	@GetMapping("/addToCookie/product/{id}")
	public String addProductToCookie(@PathVariable("id") Integer id,Model model) {			
		Cookie favorite = cookieService.read("favorite");
		String val = id.toString();
		if(favorite != null) {
			
			val = favorite.getValue();
			if(!val.contains(id.toString())) {
				val += "," + id.toString();
			}
		}		

		System.out.println(val);
		cookieService.create("favorite", val, 30);
		model.addAttribute("isFavo", true);
		String re = "redirect:/listProduct/" + id;
		
		return re;
	}
	
	@GetMapping("/removeToCookie/product/{id}")
	public String removeProductToCookie(@PathVariable("id") Integer id,Model model) {
		Cookie favorite = cookieService.read("favorite");
		String val = id.toString();
		if(favorite != null) {
			
			val = favorite.getValue();
			val = val.replaceAll("," + id.toString(), "");			
		}		

		System.out.println(val);
		cookieService.create("favorite", val, 30);
		model.addAttribute("isFavo", true);
		
		String re = "redirect:/listProduct/" + id;
		return re;
	}
	
	
	
	

	@PostMapping("/product/search")
	public String searchByKeyword(@RequestParam("keyword") String keyword,Model model) {
		
		model.addAttribute("listProduct", productService.findByKeyword("%" +keyword + "%"));
		
		return "home/listProductByCategory";
	}
	

	@GetMapping("/getFile")
	public String getfile() {
		return "home/getFile";
	}
	@PostMapping("/getFile")
	public String getFile(ModelMap model, @RequestParam("image") MultipartFile image)  {
		
			
		try {
			String path = UPLOAD_DIRECTORY + image.getOriginalFilename();
			image.transferTo(new File(path));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return "Customer/forgotPass";
	}

	@GetMapping("/product/special")
	public String getSpecial(Model model) {
		
		model.addAttribute("listProduct", productService.findBySpecial());
		
		return "home/productSpecial";
	}
	@GetMapping("/product/bestSellers")
	public String getBestSell(Model model) {
		
		model.addAttribute("listProduct", productService.getBestSell());
		return "home/productSpecial";
	}
	
	@GetMapping("/product/mostInterest")
	public String getMostInterest(Model model) {
		
		model.addAttribute("listProduct", productService.getMostInterest());		
		return "home/productSpecial";
	}
	
	@GetMapping("/product/newest")
	public String getNewest(Model model) {
		
		model.addAttribute("listProduct", productService.getNewest());
		return "home/productSpecial";
	}
	
	@GetMapping("/product/bigDiscount")
	public String getMostDiscount(Model model) {
		
		model.addAttribute("listProduct", productService.getMostDiscount());
		return "home/productSpecial";
	}
	
	@GetMapping("/accountCustomer/Customer/{id}")
	public String detailAccountCustomer(@PathVariable("id") String id,Model model) {
		Customers customer = customerService.findById(id).get();
		model.addAttribute("customer", customer);
		model.addAttribute("customerLogin", sesion.getAttribute("customerLogin"));
		return "Customer/AccountCustomerDetail";
	}

	@PostMapping("/accountCustomer/saveAccountCustomer")
	public String saveAccountCustomer(Model model, Customers customer,@RequestParam("image") MultipartFile image) {
		customer.setActivated(true);
		
		try {
			if(!image.isEmpty()) {
				String path = UPLOAD_DIRECTORY + "customer\\" + image.getOriginalFilename();
				image.transferTo(new File(path));
				customer.setPhoto(image.getOriginalFilename());
			}else {
				if(sesion.getAttribute("customerLogin") != null) {
					customer.setPhoto(((Customers) sesion.getAttribute("customerLogin")).getPhoto());
					sesion.setAttribute("customerLogin", customer);
				}else {
					customer.setPhoto(null);
				}				
			}
			
			System.out.println(sesion.getAttribute("customerLogin"));
			System.out.println(customer.getPhoto());
			customerService.save(customer);
			model.addAttribute("customerLogin", sesion.getAttribute("customerLogin")); 
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("message", "loi luu file");
			model.addAttribute("customer", customer);
			return "Customer/AccountCustomerDetail";
		}
		return "redirect:/";
	}
}
