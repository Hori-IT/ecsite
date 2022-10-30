package com.example.ecsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ecsite.model.dao.GoodsRepository;
import com.example.ecsite.model.dao.UserRepository;
import com.example.ecsite.model.entity.Goods;
import com.example.ecsite.model.entity.User;
import com.example.ecsite.model.form.GoodsForm;
import com.example.ecsite.model.form.LoginForm;

@Controller
@RequestMapping("/ecsite/admin")
public class AdminController {
	
	/*
	 * 	スライド：19
	 * 		UserRepository
	 * 		GoodsRepository の読込。
	 */
	@Autowired
	private UserRepository userRepos;
	
	@Autowired
	private GoodsRepository goodsRepos;
	
	@RequestMapping("/")
	public String index() {
		return "adminindex";
	}
	
	//	スライド：１１
	@PostMapping("/welcome")
	public String welcome(LoginForm form,Model m) {
		//	スライド：１１にて、仮記述　↓
		//　　System.out.println(form.getUserName() + " " + form.getPassword());
		
		//	スライド：19　↓↓↓
		List<User> users = userRepos.findByUserNameAndPassword(form.getUserName(), form.getPassword());
		
		if(users != null && users.size() > 0) {
			boolean isAdmin = users.get(0).getIsAdmin() != 0;
			if(isAdmin) {
				List<Goods> goods = goodsRepos.findAll();
				m.addAttribute("userName",users.get(0).getUserName());
				m.addAttribute("password",users.get(0).getPassword());
				m.addAttribute("goods",goods);
			}
		}
		return "welcome";
	}
	
	// Lesson5 スライド：2  ↓↓↓	
	@RequestMapping("/goodsMst")
	public String goodsMst(LoginForm form,Model m) {
		m.addAttribute("userName",form.getUserName());
		m.addAttribute("password",form.getPassword());
		
		return "goodsmst";
	}
	
	// Lesson5 スライド：9　　↓↓↓
	@RequestMapping("/addGoods")
	public String addGoods(GoodsForm goodsForm,LoginForm loginForm, Model m) {
		m.addAttribute("userName",loginForm.getUserName());
		m.addAttribute("password",loginForm.getPassword());
		
		Goods goods = new Goods();
		goods.setGoodsName(goodsForm.getGoodsName());
		goods.setPrice(goodsForm.getPrice());
		goodsRepos.saveAndFlush(goods);
		
		return "forward:/ecsite/admin/welcome";
	}
	
	// Lesson5 スライド：13　↓↓↓
	@ResponseBody
	@PostMapping("/api/deleteGoods")
	public String deleteApi(@RequestBody GoodsForm f, Model m) {
		try {
			goodsRepos.deleteById(f.getId());
		}catch(IllegalArgumentException e) {
			return "-1";
		}
		return "1";
	}
}
