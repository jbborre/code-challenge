import React, {useState} from 'react';
import './Results.css';

export function SearchResults(results) {

    function displayResults(results) {
        return results ? results.data.map(
            result => (
                <li className="result_item" key={result.domain}>{result.domain} ({result.create_date})</li>
            )
        ) : <div/>
    }

    return (
        <div className="results">
            {displayResults(results)}
        </div>
    )
}
