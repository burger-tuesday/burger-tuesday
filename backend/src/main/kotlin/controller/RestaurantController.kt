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

//[PlaceDetails: "DOOR No. 8 - The Steakhouse" ChIJwfNYEowHbUcRJ5omltDrMp0 (GOOGLE) address="Neubaugasse 8, 1070 Wien, Austria" geometry=[Geometry: 48.19896400,16.34993800 (null) bounds=null, viewport=[48.20028763,16.35118403, 48.19758967,16.34848607]], vicinity=Neubaugasse 8, Wien, types=[restaurant, point_of_interest, food, establishment], phone=01 5220392, internationalPhoneNumber=+43 1 5220392, url=https://maps.google.com/?cid=11327375293895055911, website=http://www.door8.at/, icon, openingHours, utcOffset=120, priceLevel=3, rating=4.6, 10 photos, 5 reviews]
