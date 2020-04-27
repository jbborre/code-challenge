package com.code.challenge.jms

data class CandidateModel(
        val event: Event,
        val recordId: Long,
        val name: String
)

enum class Event{
    CREATE,
    UPDATE,
    DELETE
}

enum class Status{
    SUCCESS,
    FAILURE
}

data class Audit(
        val recordId: Long,
        val event: Event,
        val status: Status
)