package com.grosslicht.burgertuesday.controller

import com.grosslicht.burgertuesday.controller.util.PaginationUtil
import com.grosslicht.burgertuesday.domain.Restaurant
import com.grosslicht.burgertuesday.service.RestaurantService
import mu.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/v1")
class RestaurantController(private val restaurantService: RestaurantService) {

    @GetMapping("/restaurants")
    @CrossOrigin
    fun getAllRestaurants(pageable: Pageable): ResponseEntity<List<Restaurant>> {
        logger.debug("REST request to get a page of Restaurants")
        val page = restaurantService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(page, "/v1/restaurants")
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    @PostMapping("/restaurant/add/{placeId}")
    @CrossOrigin
    fun add(@PathVariable("placeId") placeId: String, principal: Principal): ResponseEntity<Restaurant> {
        return ResponseEntity.ok(restaurantService.addRestaurant(placeId, principal.name))
    }
}
