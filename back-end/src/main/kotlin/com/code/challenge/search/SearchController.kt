package com.code.challenge.search

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController()
class SearchController(
        @Qualifier("domainQueryService")
        val domainQueryService: DomainQueryService
){

    @GetMapping("search")
    fun search(): List<Domain>{
        val result = domainQueryService.query("facebook")
        return result.domains
    }
}