package com.grosslicht.burgertuesday.service

import at.favre.lib.idmask.IdMask
import com.google.maps.GeoApiContext
import com.google.maps.PlaceDetailsRequest.FieldMask.FORMATTED_ADDRESS
import com.google.maps.PlaceDetailsRequest.FieldMask.NAME
import com.google.maps.PlaceDetailsRequest.FieldMask.PERMANENTLY_CLOSED
import com.google.maps.PlaceDetailsRequest.FieldMask.PLACE_ID
import com.google.maps.PlaceDetailsRequest.FieldMask.PRICE_LEVEL
import com.google.maps.PlaceDetailsRequest.FieldMask.RATING
import com.google.maps.PlaceDetailsRequest.FieldMask.URL
import com.google.maps.PlaceDetailsRequest.FieldMask.VICINITY
import com.google.maps.PlaceDetailsRequest.FieldMask.WEBSITE
import com.google.maps.PlacesApi
import com.grosslicht.burgertuesday.domain.Restaurant
import com.grosslicht.burgertuesday.repository.RestaurantRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.AbstractAuditable_.createdBy
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.Optional

@Service
class RestaurantService(
    private val restaurantRepository: RestaurantRepository,
    private val geoApiContext: GeoApiContext,
    private val idMask: IdMask<Long>
) {
    @PreAuthorize("hasAuthority('manage:restaurants')")
    fun addByPlaceId(placeId: String, username: String): Restaurant {
        val placeDetails = PlacesApi.placeDetails(geoApiContext, placeId).fields(
            PLACE_ID,
            NAME,
            FORMATTED_ADDRESS,
            VICINITY,
            RATING,
            PRICE_LEVEL,
            URL,
            WEBSITE,
            PERMANENTLY_CLOSED
        ).await()
        val restaurant = Restaurant(
            placeId = placeDetails.placeId,
            name = placeDetails.name,
            address = placeDetails.formattedAddress,
            vicinity = placeDetails.vicinity,
            googleRating = BigDecimal(placeDetails.rating.toString()),
            priceLevel = placeDetails.priceLevel?.toString()?.toInt() ?: -1,
            url = placeDetails.url.toString(),
            website = placeDetails.website.toString(),
            permanentlyClosed = placeDetails.permanentlyClosed,
            createdBy = username
        )
        return restaurantRepository.save(restaurant)
    }

    fun findAll(pageable: Pageable): Page<Restaurant> {
        return restaurantRepository.findAll(pageable)
    }

    fun find(id: String): Optional<Restaurant> {
        val unmaskedId = idMask.unmask(id)
        return restaurantRepository.findById(unmaskedId)
    }
}
