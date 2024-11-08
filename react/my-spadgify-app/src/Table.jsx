import { useState } from "react";
import "./css/Table.css";
import Fuse from "fuse.js";

function Table({ getMedia, data }) {
    // Handles clicking on a table row and returning the clicked row's details
    const handleRowClick = (song, artist, album) => {
        // alert(`Selected Song: ${song}, Artist: ${artist}, Album: ${album}`);
        let metadata = song + "-" + artist + "-" + album;
        getMedia(metadata);
    };

    // Search - fuzzy search
    const [query, setQuery] = useState('');  // User input for search query
    const [results, setResults] = useState([]);  // Search results

    // Fuse.js options
    const options = {
        includeScore: true,
        keys: ['song', 'artist'],  // Fields to search in
    };

    const fuse = new Fuse(data, options);  // Initialize Fuse.js with data and options

    // Handle search input change
    const handleSearch = (event) => {
        const value = event.target.value;
        setQuery(value);

        // Perform fuzzy search if query is not empty
        if (value.trim()) {
            const searchResults = fuse.search(value);
            setResults(searchResults.map(result => result.item));  // Extract the items from the result
            console.log(results)
        } else {
            setResults([]);  // Reset results if query is cleared
        }
    };

    // Display songs in table
    // On click return row from table

    return (
        <div className="table-container">
            <input
                className="search-field"
                type="text"
                placeholder="Search..."
                onChange={handleSearch}
            />
            <div className="media-table">
                <table>
                    <thead>
                        <tr>
                            <th>Song</th>
                            <th>Artist</th>
                            <th>Album</th>
                        </tr>
                    </thead>
                </table>
                {/* Scrollable container for the table body */}
                <div className="scrollable-tbody">
                    <table>
                    <tbody>
                        {
                        results && results.length > 0 ? (
                            results.map((item, index) => {
                                const [song, artist, album] = item.split("-"); // Split "song-artist-album"
                                return (
                                    <tr
                                        key={index}
                                        onClick={() =>
                                                handleRowClick(song, artist, album)
                                            }
                                        >
                                            <td>{song}</td>
                                            <td>{artist}</td>
                                            <td>{album}</td>
                                        </tr>
                                    );
                                })
                            )
                        : (

                            data.map((item, index) => {
                                const [song, artist, album] = item.split("-"); // Split "song-artist-album"
                                return (
                                    <tr
                                        key={index}
                                        onClick={() =>
                                            handleRowClick(song, artist, album)
                                        }
                                    >
                                        <td>{song}</td>
                                        <td>{artist}</td>
                                        <td>{album}</td>
                                    </tr>
                                );
                            })
                            )}
                    </tbody>
                </table>
                </div>
            </div>
        </div>
    );
}

export default Table;
