import { useState } from "react";

const MusicStreamingClient = () => {
    const [audioContext, setAudioContext] = useState(null);
    const [source, setSource] = useState(null);
    const [isPlaying, setIsPlaying] = useState(false);
    const [isNotStarted, setIsNotStarted] = useState(true);

    const startAudio = () => {
        if (!audioContext) {
            // Initialize AudioContext on user gesture
            const context = new (window.AudioContext ||
                window.webkitAudioContext)();
            setAudioContext(context);
        }

        // WebSocket setup and handling remains the same
        const socket = new WebSocket("ws://192.168.38.126:8080");
        socket.binaryType = "arraybuffer";

        socket.onopen = () => {
            console.log("WebSocket connection established");
        };

        socket.onmessage = (event) => {
            console.log("onmessage");
            const audioData = event.data;
            if (audioContext) {
                audioContext.decodeAudioData(
                    audioData,
                    (decodedData) => {
                        playDecodedData(decodedData);
                    },
                    (error) => {
                        console.error("Error decoding audio data:", error);
                    }
                );
            } else {
                console.error("Audio context is not initialized.");
            }
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

        if (!audioContext) {
            console.error("Audio context is null");
            return; // Prevents trying to use null audioContext
        }

        // Create a new audio source every time
        const audioSource = audioContext.createBufferSource();
        audioSource.buffer = decodedData;
        audioSource.connect(audioContext.destination);
        audioSource.start(0);
        setSource(audioSource);
        setIsPlaying(true);
    };

    const initPlayer = () => {
        console.log("initPlayer");
        setIsNotStarted(false);
        stopAudio();
        if (!audioContext) {
            // Initialize AudioContext on user gesture
            const context = new (window.AudioContext ||
                window.webkitAudioContext)();
            setAudioContext(context);
        }
    };

    const stopAudio = () => {
        console.log("Stop audio");
        if (source) {
            source.stop();
            setSource(null); // Reset source state
        }
        setIsPlaying(false);
    };

    return (
        <div>
            <button
                onClick={startAudio}
                disabled={isPlaying}
                hidden={isNotStarted}
            >
                Play Music
            </button>
            <button
                onClick={stopAudio}
                disabled={!isPlaying}
                hidden={isNotStarted}
            >
                Stop Music
            </button>
            <button onClick={initPlayer} hidden={!isNotStarted}>
                Init player
            </button>
        </div>
    );
};

export default MusicStreamingClient;
