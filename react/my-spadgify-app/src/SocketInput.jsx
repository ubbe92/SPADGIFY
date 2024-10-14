import React, { useState } from "react";

function SocketInput({ getIpPort, hidden, defaultValue }) {
    const handleChange = (e) => {
        const { value } = e.target;
        getIpPort(value);
    };

    return (
        <input
            type="text"
            onChange={handleChange}
            hidden={hidden}
            defaultValue={defaultValue}
        />
    );
}

export default SocketInput;
