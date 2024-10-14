import { useState } from "react";
import "./css/Table.css";

function Table({ getMedia, data }) {
    // Handles clicking on a table row and returning the clicked row's details
    const handleRowClick = (song, artist, album) => {
        // alert(`Selected Song: ${song}, Artist: ${artist}, Album: ${album}`);
        let metadata = song + "-" + artist + "-" + album;
        getMedia(metadata);
    };

    // Handles input change (add or remove songs)
    const handleChange = (e) => {
        const { value } = e.target;
        getMedia(value);
    };
    // Search - fuzzy search
    // Display songs in table
    // On click return row from table

    return (
        <div className="MediaTable">
            <div className="SearchField">
                <input
                    type="text"
                    placeholder="Add song-artist-album"
                    onChange={handleChange}
                />
            </div>
            <div className="">
                {/* Input to add media (not fully implemented) */}
                <table>
                    <thead>
                        <tr>
                            <th>Song</th>
                            <th>Artist</th>
                            <th>Album</th>
                        </tr>
                    </thead>
                    <tbody>
                        {data && data.length > 0 ? (
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
                        ) : (
                            <tr>
                                <td colSpan="3">No media items available</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default Table;
