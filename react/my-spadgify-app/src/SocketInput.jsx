import React, { useState } from "react";

function SocketInput({ getIpPort, hidden }) {
    const handleChange = (e) => {
        const { value } = e.target;
        getIpPort(value);
    };

    return <input type="text" onChange={handleChange} hidden={hidden} />;
}

export default SocketInput;
