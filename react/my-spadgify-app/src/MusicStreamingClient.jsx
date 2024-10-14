import {  useState } from "react";
import SocketInput from "./SocketInput";

const MusicStreamingClient = () => {
    const [audioContext, setAudioContext] = useState(null);
    const [source, setSource] = useState(null);
    const [isPlaying, setIsPlaying] = useState(false);
    const [isNotStarted, setIsNotStarted] = useState(true);
    const [value, setValue] = useState("");
    const [ipPortSocket, setIpPortSocket] = useState(null);

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

    const startAudio = () => {
        if (!audioContext) {
            // Initialize AudioContext on user gesture
            const context = new (window.AudioContext ||
                window.webkitAudioContext)();
            setAudioContext(context);
        }

        // WebSocket setup and handling remains the same
        // const socket = new WebSocket("ws://192.168.38.126:8080");
        const socket = new WebSocket("ws://" + ipPortSocket);
        socket.binaryType = "arraybuffer";

        socket.onopen = () => {
            console.log("WebSocket connection established");

            // Request a song to listen to!
            // const data = "perfect beauty-Mikael Jäcksson-In the bodega";
            // const data = "amalgam-DJ Sinthu-Performance is life";

            // const data = value;
            // socket.send(data);

            const data = value;
            socket.send(data);
        };

        socket.onmessage = (event) => {
            console.log("onmessage");

            if (typeof event.data === "string") {
                console.log("Is string!: " + event.data);
            } else if (event.data instanceof ArrayBuffer) {
                console.log("is music data");
            } else {
                console.log("unknown data");
            }

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

    const stopAudio = () => {
        console.log("Stop audio");
        if (source) {
            source.stop();
            setSource(null); // Reset source state
        }
        setIsPlaying(false);
    };

    function handleChange(e) {
        setValue(e.target.value);
    }

    const getIpPortSocket = (serverIpPortSocket) => {
        setIpPortSocket(serverIpPortSocket);
    };

    return (
        <div>
            <div className="MusicStreamingClient">
                <div hidden={isNotStarted}>
                    <input value={value} onChange={handleChange} />
                    <div>Requested song: {value}</div>
                </div>

                <div>
                    <SocketInput
                        getIpPort={getIpPortSocket}
                        hidden={!isNotStarted}
                    ></SocketInput>
                </div>

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
        </div>
    );
};

export default MusicStreamingClient;
