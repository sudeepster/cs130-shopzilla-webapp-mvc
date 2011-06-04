/**
 * Copyright 2011 Shopzilla.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.shopzilla.publisher.web;

import com.shopzilla.api.client.model.*;
import com.shopzilla.perf.aspect.Loggable;
import com.shopzilla.perf.aspect.PerfTimed;
import com.shopzilla.perf.logger.LogLevel;
import com.shopzilla.publisher.service.CategoryProviderService;

import java.io.Reader;
import java.util.*;

import com.shopzilla.publisher.service.CategorySearchService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author alook
 * @since 4/19/11
 */
@RequestMapping("/")
@Controller
public class HomeController {
    private static final Log LOG = LogFactory.getLog(HomeController.class);

    private CategoryProviderService categoryProviderService;
    private List<Offer> offers = new ArrayList<Offer>();
    private List<Product> products = new ArrayList<Product>();
    private List<Attribute> attributes = new ArrayList<Attribute>();

    private CategorySearchService categorySearchService;

//    @RequestMapping(value = "*", method = RequestMethod.GET)
//    public String displayHomepage(Model uiModel) {
//        List<Category> categoryList = categoryProviderService.fetchCategories();
//        uiModel.addAttribute("categories", categoryList);
//        return "index";
//    }

    @PerfTimed
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String displayHomepage(Model uiModel) {
        this.offers.clear();
        this.products.clear();
        this.attributes.clear();
        List<Category> categoryList = categoryProviderService.fetchCategories();
        for (Category category : categoryList) {
            CatalogResponse response = categorySearchService.categorySearch(category.getId(), 10);

            this.offers.addAll(response.getOffers());
            this.products.addAll(response.getProducts());
            this.attributes.addAll(response.getRelatedAttributes());
        }
        uiModel.addAttribute("offers", this.offers);
        uiModel.addAttribute("products", this.products);
        uiModel.addAttribute("attributes", this.attributes);

        return "index";
    }

    @PerfTimed
    @RequestMapping(value = "search", method = RequestMethod.GET)
    public String show(@RequestParam("searchkey") String key, Model uiModel) {
        this.offers.clear();
        this.products.clear();
        List<Category> categoryList = categoryProviderService.fetchCategories();
        for (Category category : categoryList) {
            CatalogResponse response = categorySearchService.keywordSearch(category.getId(), 10, key);

            this.offers.addAll(response.getOffers());
            this.products.addAll(response.getProducts());
        }

        uiModel.addAttribute("offers", this.offers);
        uiModel.addAttribute("products", this.products);
        uiModel.addAttribute("attributes", this.attributes);
        uiModel.addAttribute("searchkey", key);
        return "index";
    }

    @PerfTimed
    @RequestMapping(value = "filter", method = RequestMethod.GET)
    public String filter(HttpServletRequest request, Model uiModel) {

        StringBuilder query = new StringBuilder();
        Enumeration requestEnum = request.getParameterNames();
        while (requestEnum.hasMoreElements()) {
            String key = (String) requestEnum.nextElement();
            for (String str : request.getParameterValues(key)) {
                query.append(key);
                query.append(":");
                query.append(str);
                query.append(";");
            }

        }
        query.deleteCharAt(query.length() - 1);
        String queryStr = query.toString();

        this.offers.clear();
        this.products.clear();
        List<Category> categoryList = categoryProviderService.fetchCategories();
        for (Category category : categoryList) {
            CatalogResponse response = categorySearchService.attributeSearch(category.getId(), 10, queryStr);

            this.offers.addAll(response.getOffers());
            this.products.addAll(response.getProducts());
        }

        uiModel.addAttribute("offers", this.offers);
        uiModel.addAttribute("products", this.products);
        uiModel.addAttribute("attributes", this.attributes);

        return "index";
    }


    @Required
    public void setCategoryProviderService(CategoryProviderService categoryProviderService) {
        this.categoryProviderService = categoryProviderService;
    }

    @Required
    public void setCategorySearchService(CategorySearchService categorySearchService) {
        this.categorySearchService = categorySearchService;
    }
}
