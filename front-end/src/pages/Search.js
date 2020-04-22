import React, {useState} from 'react';
import './Search.css';
import {SearchResults} from "../components/SearchResults";

export function SearchPage() {
    const url = 'http://localhost:8080'
    const [searchText, setSearchText] = useState("");
    const [searchResults, setSearchResults] = useState([]);

    function onSearchTextUpdate(event) {
        setSearchText(event.target.value);
    }

    function onSearch(event) {
        if(event.key === 'Enter' || event.type === 'click') {
            fetch(`${url}/search?name=${searchText}`)
                .then(res =>
                    res.json()
                )
                .then((data) => {
                    setSearchResults(data);
                })
                .catch(e => console.log("Error occurred while trying to retrieve search", e))
        }
    }

    return <div className="search_page">
        <div className="search_bar">
            <div className="search_input_wrapper">
                <input className="search_input" value={searchText}
                       onKeyDown={onSearch}
                       onChange={onSearchTextUpdate}
                />
            </div>
            <button className="search_button" onClick={onSearch}>Search</button>
        </div>
        <SearchResults data={searchResults}/>
    </div>
}