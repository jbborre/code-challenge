package com.code.challenge.search

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service("domainQueryService")
class DomainQueryServiceImpl(
        @Autowired
        val restTemplate: RestTemplate
) : DomainQueryService {

    override fun query(name: String): Result {
        return restTemplate.getForObject<Result>("https://api.domainsdb.info/v1/domains/search?domain=facebook")
    }
}