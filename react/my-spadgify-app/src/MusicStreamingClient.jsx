import { useState } from "react";

const MusicStreamingClient = () => {
    const [audioContext, setAudioContext] = useState(null);
    const [, setSource] = useState(null);
    const [isPlaying, setIsPlaying] = useState(false);

    const startAudio = () => {
        // Initialize AudioContext on user gesture
        const context = new (window.AudioContext ||
            window.webkitAudioContext)();
        setAudioContext(context);

        // Create a WebSocket connection
        const socket = new WebSocket("ws://192.168.38.126:8080"); // Update URL as needed
        socket.binaryType = "arraybuffer";

        socket.onopen = () => {
            console.log("WebSocket connection established");
        };

        socket.onmessage = (event) => {
            console.log("onmessage");
            const audioData = event.data;
            context.decodeAudioData(audioData, (decodedData) => {
                playDecodedData(decodedData);
            });
        };

        socket.onerror = (error) => {
            console.error("WebSocket error:", error);
        };

        socket.onclose = () => {
            console.log("WebSocket connection closed");
        };
    };

    const playDecodedData = (decodedData) => {
        console.log("playDecodedData");
        const audioSource = audioContext.createBufferSource();
        audioSource.buffer = decodedData;
        audioSource.connect(audioContext.destination);
        audioSource.start(0);
        setSource(audioSource);
        setIsPlaying(true);
    };

    return (
        <div>
            <h1>Music Streaming Client</h1>
            <button onClick={startAudio} disabled={isPlaying}>
                Play Music
            </button>
        </div>
    );
};

export default MusicStreamingClient;
