package com.grosslicht.burgertuesday.controller

import com.grosslicht.burgertuesday.domain.Visit
import com.grosslicht.burgertuesday.service.VisitService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.time.LocalDate

@RestController
@RequestMapping("/v1")
class VisitController(private val visitService: VisitService) {

    @PostMapping("/visit/{placeId}/add/{date}")
    @CrossOrigin
    fun add(
        @PathVariable("placeId") placeId: Long,
        @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date: LocalDate,
        @RequestParam("sponsored") sponsored: Boolean,
        principal: Principal
    ): ResponseEntity<Visit> {
        return ResponseEntity.ok(visitService.addVisit(placeId, date, sponsored, principal.name))
    }
}
