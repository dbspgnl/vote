package com.example.vote.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
    
    @RequestMapping(value = "/")
    public String home() {
        return "index";
    }

    @RequestMapping(value = "/chat")
    public String chat() {
        return "chat";
    }

    @RequestMapping(value = "/avalonExpedition")
    public String avalonExpedition() {
        return "avalonExpedition";
    }

    @RequestMapping(value = "/avalonCampaign")
    public String avalonCampaign() {
        return "avalonCampaign";
    }

    @RequestMapping(value = "/avalonRoleSet")
    public String avalonRoleSet() {
        return "avalonRoleSet";
    }

}
