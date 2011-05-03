package com.shopzilla.publisher.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by IntelliJ IDEA.
 * User: spradhan
 * Date: 5/2/11
 * Time: 12:37 AM
 * To change this template use File | Settings | File Templates.
 */

@RequestMapping("/search")
@Controller
public class SearchController {
    @RequestMapping(method = RequestMethod.GET)
    public String show(@RequestParam("searchkey") String key, Model uiModel) {
        uiModel.addAttribute("searchkey", key);
        return "search";
    }
}
