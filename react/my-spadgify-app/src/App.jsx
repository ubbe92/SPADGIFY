import 'bootstrap/dist/css/bootstrap.min.css';

import React, { useState } from "react";

import Header from "./Header.jsx";
import Footer from "./Footer.jsx";
// import Food from "./Food.jsx";

import MusicStreamer from "./MusicStreamer.jsx";

function App() {
    // Example media data with 3 songs
    const [mediaData, setMediaData] = useState([
        "Shape of You-Ed Sheeran-Divide",
        "Blinding Lights-The Weeknd-After Hours",
        "Bohemian Rhapsody-Queen-A Night at the Opera",
    ]);

    // Placeholder function for getMedia (optional for now)
    const getMedia = (newMedia) => {
        console.log("New media received: ", newMedia);
        // Logic for adding new media can be implemented here
    };
    return (
        <>
            <Header></Header>
            <div class="main-container">
                {/* <MusicStreamingClient></MusicStreamingClient>
                <Table getMedia={getMedia} data={mediaData}></Table> */}
                <MusicStreamer></MusicStreamer>
            </div>

            <Footer></Footer>
        </>
    );
}

export default App;
