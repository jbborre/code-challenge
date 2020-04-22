package com.code.challenge.search

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private val LOGGER = KotlinLogging.logger {}

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController()
class SearchController(
        @Qualifier("domainQueryService")
        val domainQueryService: DomainQueryService
) {

    @GetMapping("search")
    fun search(
            @RequestParam("name")
            domainName: String
    ): List<Domain> {
        LOGGER.info("search called with {}", domainName)

        val result = domainQueryService.query(domainName)
        LOGGER.info("returning the results of request")
        return result.domains
    }
}