import React, {useState} from 'react';

export function SearchResults(results) {

    console.log("results", results);
    return (
        results ? results.data.map(
            result => (
                <li>{result.name}</li>
            )
        ) : <div/>

    )
}
