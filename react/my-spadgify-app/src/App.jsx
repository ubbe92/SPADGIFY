import React, { useState } from 'react';

import Header from "./Header.jsx";
import Footer from "./Footer.jsx";
// import Food from "./Food.jsx";
import Table from "./Table.jsx";

import MusicStreamingClient from "./MusicStreamingClient.jsx";

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
      <MusicStreamingClient></MusicStreamingClient>
      <Table getMedia={getMedia} data={mediaData}></Table>
      <Footer></Footer>
    </>
  );
}

export default App;
