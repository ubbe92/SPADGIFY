import { useEffect, useState } from "react";

function FetchDataComponent() {
    const [data, setData] = useState([]); // Holds the data from the server
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        // Fetch data from the Restlet server
        // fetch("http://localhost:8080/API/mediaInfo", {
        fetch("http://localhost:8000/API/mediaInfo", {
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
                setData(data); // Store the data in state
                setLoading(false); // Loading is done
            })
            .catch((error) => {
                setError(error); // Handle any errors
                setLoading(false); // Stop loading
            });
    }, []); // Run only once after component mounts

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error: {error.message}</div>;

    return (
        <div>
            <h1>Data from Restlet API:</h1>
            <ul>
                {data.map((item, index) => (
                    <li key={index}>{JSON.stringify(item)}</li> // Display each item
                ))}
            </ul>
        </div>
    );
}

export default FetchDataComponent;
