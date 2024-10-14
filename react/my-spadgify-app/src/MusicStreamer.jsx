import { useState } from "react";
import SocketInput from "./SocketInput";

function MusicStreamer() {
    const [audioContext, setAudioContext] = useState(null);
    const [value, setValue] = useState("");

    const ipPort = "192.168.38.126:8080";

    const openWebSocket = (ipPort) => {
        return new Promise((resolve, reject) => {
            const socket = new WebSocket("ws://" + ipPort);
            socket.binaryType = "arraybuffer";

            socket.onopen = () => {
                console.log("WebSocket connection established");
                resolve(socket); // Resolve with the socket once it's open
            };

            socket.onerror = (error) => {
                reject(error); // Reject the promise if there's an error
            };
        });
    };

    const startAudio = async () => {
        if (!audioContext) {
            const context = new (window.AudioContext ||
                window.webkitAudioContext)();
            setAudioContext(context);
        }

        try {
            const socket = await openWebSocket(ipPort);
            const data = value; // Your data to send
            socket.send(data);
            console.log("Data sent:", data);

            socket.onmessage = (event) => {
                console.log("Received message from server:", event.data);
            };
        } catch (error) {
            console.error("Error opening WebSocket:", error);
        }
    };

    function handleChange(e) {
        setValue(e.target.value);
    }

    return (
        <div>
            <input value={value} onChange={handleChange} />
            <div>{value}</div>
            <button onClick={startAudio}>Click me!</button>
        </div>
    );
}

export default MusicStreamer;
