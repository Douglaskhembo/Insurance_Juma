package com.brokersystems.brokerapp.nav.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
/**
 * System Main Navigation Controller
 * @author mugenyq
 *
 */
@Controller
@RequestMapping(value = "/protected/home")
public class NavigationController {
	
	 @RequestMapping(value="/orgsetups",method = RequestMethod.GET)
	    public ModelAndView orgSetups() {
	        return new ModelAndView("orgSetupScreen");
	    }
	 
	 /**
	  * Navigation to System Setups Screen
	  * @return
	  */
	 @RequestMapping(value="/setups",method = RequestMethod.GET)
	    public ModelAndView tensetups() {
	        return new ModelAndView("uwsetupScreen");
	    }


	/**
	 * Navigation to Accounts Setups Screen
	 * @return
	 */
	@RequestMapping(value="/accountsetups",method = RequestMethod.GET)
	public ModelAndView accountsetups() {
		return new ModelAndView("accountsetups");
	}

	/**
	 * Navigation to Medical Setups Screen
	 * @return
	 */
	@RequestMapping(value="/medicalsetups",method = RequestMethod.GET)
	public ModelAndView medsetups() {
		return new ModelAndView("medsetups");
	}

	/**
	 * Navigation to Finance Setups Screen
	 * @return
	 */
	@RequestMapping(value="/financesetups",method = RequestMethod.GET)
	public ModelAndView financesetups() {
		return new ModelAndView("financesetups");
	}



	/**
	  * Navigation to System Transaction Screen
	  * @return
	  */
	 @RequestMapping(value="/trans",method = RequestMethod.GET)
	    public ModelAndView trans() {
	        return new ModelAndView("tentrans");
	    }
	 
	
	 
	    
	    

}
