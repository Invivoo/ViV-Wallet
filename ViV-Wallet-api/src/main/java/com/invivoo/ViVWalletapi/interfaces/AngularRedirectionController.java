package com.invivoo.ViVWalletapi.interfaces;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AngularRedirectionController {

    @RequestMapping(value = {"/**/{[path:[^\\\\.]*}"})
    public String redirect() {
        return "forward:/";
    }
}
