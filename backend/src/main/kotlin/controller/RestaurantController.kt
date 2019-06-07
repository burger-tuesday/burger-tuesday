package com.grosslicht.burgertuesday.controller

import com.grosslicht.burgertuesday.api.RestaurantApi
import com.grosslicht.burgertuesday.controller.util.PaginationUtil
import com.grosslicht.burgertuesday.domain.Restaurant
import com.grosslicht.burgertuesday.service.RestaurantService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class RestaurantController(private val restaurantService: RestaurantService) : RestaurantApi {
    override fun getAllRestaurants(pageable: Pageable): ResponseEntity<List<Restaurant>> {
        val page = restaurantService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(page, "/v1/restaurants")
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    override fun getRestaurant(id: String): ResponseEntity<Restaurant> {
        return ResponseEntity.of(restaurantService.find(id))
    }

    override fun addByPlaceId(@PathVariable("placeId") placeId: String, principal: Principal): ResponseEntity<Restaurant> {
        return ResponseEntity.ok(restaurantService.addByPlaceId(placeId, principal.name))
    }
}
