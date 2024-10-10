import { useEffect, useState } from "react";

const MusicStreamingClient = () => {
    const [audioContext, setAudioContext] = useState(null);
    const [source, setSource] = useState(null);
    const [isPlaying, setIsPlaying] = useState(false);

    const startAudio = () => {
        // Initialize AudioContext on user gesture
        // var context;
        // if (!audioContext) {
        //     context = new (window.AudioContext || window.webkitAudioContext)();
        // }
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

    const stopAudio = () => {
        console.log("Stop audio");
        source.stop();
        setIsPlaying(false);
    };

    const playDecodedData = (decodedData) => {
        console.log("playDecodedData");

        if (!audioContext) {
            console.log("Audio context is null? " + audioContext);
        }
        const audioSource = audioContext.createBufferSource();
        console.log("audioBuffer: " + audioSource);
        audioSource.buffer = decodedData;
        audioSource.connect(audioContext.destination);
        audioSource.start(0);
        setSource(audioSource);
        setIsPlaying(true);
    };

    useEffect(() => {
        console.log("audioContext useeffect: " + audioContext);
    }, [audioContext]);

    return (
        <div>
            <button onClick={startAudio} disabled={isPlaying}>
                Play Music
            </button>
            <button onClick={stopAudio} disabled={!isPlaying}>
                Stop Music
            </button>
        </div>
    );
};

export default MusicStreamingClient;
