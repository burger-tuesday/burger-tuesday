package com.grosslicht.burgertuesday.api

import com.grosslicht.burgertuesday.domain.Restaurant
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.security.Principal
import javax.validation.Valid
import javax.validation.constraints.NotNull

@Validated
@RequestMapping("/v1")
interface RestaurantApi {
    @PostMapping("/restaurants", consumes = ["application/json"])
    fun createRestaurant(@Valid @RequestBody restaurant: Restaurant): ResponseEntity<Restaurant> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @DeleteMapping("/restaurants/{id}")
    fun deleteRestaurant(@PathVariable("id") id: String): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @GetMapping("/restaurants")
    fun getAllRestaurants(pageable: Pageable): ResponseEntity<List<Restaurant>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @GetMapping("/restaurants/{id}")
    fun getRestaurant(@PathVariable("id") id: String): ResponseEntity<Restaurant> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @GetMapping("/_search/restaurants")
    fun searchRestaurants(
        @NotNull @RequestParam(value = "query", required = true) query: String,
        pageable: Pageable
    ): ResponseEntity<List<Restaurant>> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @PutMapping("/restaurants", consumes = ["application/json"])
    fun updateRestaurant(@Valid @RequestBody restaurant: Restaurant): ResponseEntity<Restaurant> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }

    @PostMapping("/restaurants/add/{placeId}")
    fun addByPlaceId(@PathVariable("placeId") placeId: String, principal: Principal): ResponseEntity<Restaurant> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
