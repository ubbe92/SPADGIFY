import { useState } from "react";
import SocketInput from "./SocketInput";
import Table from "./Table";

function MusicStreamer() {
    const [audioContext, setAudioContext] = useState(null);
    const [source, setSource] = useState(null);
    const [isPlaying, setIsPlaying] = useState(false);
    const [isNotStarted, setIsNotStarted] = useState(true);
    const [value, setValue] = useState("");
    const [ipPortSocket, setIpPortSocket] = useState("192.168.38.126:8080");
    const [ipPortRest, setIpPortRest] = useState("192.168.38.126:8000");
    const [error, setError] = useState(null);
    const [data, setData] = useState([]); // Holds the data from the server

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

        console.log("socket: " + ipPortSocket);
        console.log("rest: " + ipPortRest);
        getAllMusic(ipPortRest);
    };

    const startAudio = () => {
        if (!audioContext) {
            // Initialize AudioContext on user gesture
            const context = new (window.AudioContext ||
                window.webkitAudioContext)();
            setAudioContext(context);
        }

        const socket = new WebSocket("ws://" + ipPortSocket);
        socket.binaryType = "arraybuffer";

        socket.onopen = () => {
            console.log("WebSocket connection established");
            let data = value;
            socket.send(data);
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

    const getIpPortRest = (serverIpPortRest) => {
        setIpPortRest(serverIpPortRest);
    };

    const getAllMusic = (ipPort) => {
        fetch("http://" + ipPort + "/API/mediaInfo", {
            // Adjust the endpoint as per your Restlet server
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json(); // Parse the JSON response
            })
            .then((data) => {
                let jsonStr = JSON.stringify(data);
                let jsonArr = JSON.parse(jsonStr);

                let dataArr = [];
                for (let i = 0; i < jsonArr.length; i++) {
                    let song = jsonArr[i].song;
                    let artist = jsonArr[i].artist;
                    let album = jsonArr[i].album;
                    let metadata = song + "-" + artist + "-" + album;
                    dataArr.push(metadata);
                }

                setData(dataArr);
                console.log("Get all music done!");
            })
            .catch((error) => {
                setError(error); // Handle any errors
                console.log("error get all music: " + error.value);
            });
    };

    // Placeholder function for getMedia (optional for now)
    const getMedia = (newMedia) => {
        console.log("Selected media: ", newMedia);
        setValue(newMedia);
        // Logic for adding new media can be implemented here
    };

    return (
        <div>
            <div className="MusicStreamingClient">
                <div hidden={isNotStarted}>
                    {/* <input value={value} onChange={handleChange} /> */}
                    <div>Requested song: {value}</div>
                </div>

                <div>
                    <SocketInput
                        getIpPort={getIpPortSocket}
                        hidden={!isNotStarted}
                        defaultValue={ipPortSocket}
                    ></SocketInput>
                    <div hidden={!isNotStarted}>
                        Socket server: {ipPortSocket}
                    </div>
                    <SocketInput
                        getIpPort={getIpPortRest}
                        hidden={!isNotStarted}
                        defaultValue={ipPortRest}
                    ></SocketInput>
                    <div hidden={!isNotStarted}>Rest server: {ipPortRest}</div>
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

                <Table getMedia={getMedia} data={data}></Table>
            </div>
        </div>
    );
}

export default MusicStreamer;
