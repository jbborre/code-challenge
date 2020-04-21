package com.code.challenge.search

interface DomainQueryService {
    abstract fun query(name: String): Result
}

data class Result(
        val domains: List<Domain>
)
data class Domain(
        val domain: String,
        val create_date: String?
)