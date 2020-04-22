package com.code.challenge.search

import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

private val LOGGER = KotlinLogging.logger {}

@Service("domainQueryService")
class DomainQueryServiceImpl(
        @Autowired
        val restTemplate: RestTemplate
) : DomainQueryService {
    override fun query(name: String): Result {
        LOGGER.info("calling api domains db with search of {}", name)
        val result = restTemplate.getForObject<Result>("https://api.domainsdb.info/v1/domains/search?domain={domain}&zone={zone}&limit={limit}",
                mapOf("domain" to name,
                        "zone" to "com",
                        "limit" to "10"
                ))
        return Result(domains = result.domains.sortedBy { it.domain })
    }
}