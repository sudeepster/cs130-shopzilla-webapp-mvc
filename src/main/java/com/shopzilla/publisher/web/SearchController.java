package com.shopzilla.publisher.web;

import com.shopzilla.api.client.model.CatalogResponse;
import com.shopzilla.api.client.model.Category;
import com.shopzilla.api.client.model.Offer;
import com.shopzilla.api.client.model.Product;
import com.shopzilla.publisher.service.CategoryProviderService;
import com.shopzilla.publisher.service.CategorySearchService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

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
    private CategoryProviderService categoryProviderService;
    private CategorySearchService categorySearchService;
    private List<Offer> offers = new ArrayList<Offer>();
    private List<Product> products = new ArrayList<Product>();


    @RequestMapping(method = RequestMethod.GET)
    public String show(@RequestParam("searchkey") String key, Model uiModel) {
        List<Category> categoryList = categoryProviderService.fetchCategories();
        for (Category category : categoryList) {
            CatalogResponse response = categorySearchService.keywordSearch(category.getId(), 10, key);

            this.offers.addAll(response.getOffers());
            this.products.addAll(response.getProducts());
        }

        uiModel.addAttribute("offers", this.offers);
        uiModel.addAttribute("products", this.products);

        uiModel.addAttribute("searchkey", key);
        return "search";
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
