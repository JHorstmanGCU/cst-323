package com.gcu.bikeshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Handles requests for the main dashboard page.
 */
@Controller
public class HomeController {

    /**
     * Shows the main dashboard page.
     *
     * @param model page data sent to the view
     * @return the home page template
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageTitle", "Bike Shop Order Tracker");
        return "home";
    }
}
