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

import com.shopzilla.api.client.model.CatalogResponse;
import com.shopzilla.api.client.model.Category;
import com.shopzilla.api.client.model.Offer;
import com.shopzilla.api.client.model.Product;
import com.shopzilla.publisher.service.CategoryProviderService;

import java.util.ArrayList;
import java.util.List;

import com.shopzilla.publisher.service.CategorySearchService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    private CategorySearchService categorySearchService;

//    @RequestMapping(value = "*", method = RequestMethod.GET)
//    public String displayHomepage(Model uiModel) {
//        List<Category> categoryList = categoryProviderService.fetchCategories();
//        uiModel.addAttribute("categories", categoryList);
//        return "index";
//    }

    @RequestMapping(value = "*", method = RequestMethod.GET)
    public String displayHomepage(Model uiModel) {
        List<Category> categoryList = categoryProviderService.fetchCategories();
        for (Category category : categoryList) {
            CatalogResponse response = categorySearchService.categorySearch(category.getId(), 10);

            this.offers.addAll(response.getOffers());
            this.products.addAll(response.getProducts());
        }
        uiModel.addAttribute("offers", this.offers);
        uiModel.addAttribute("products", this.products);

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
