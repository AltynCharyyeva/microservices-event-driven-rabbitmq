import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

export default function UserDeviceMeasurements() {
  const { deviceId } = useParams(); // Get the deviceId from the URL parameters
  const [measurements, setMeasurements] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [notification, setNotification] = useState(""); // State for notification message

  const socket = new SockJS("http://localhost/monservice/ws"); // Connect to the WebSocket endpoint
  const stompClient = Stomp.over(socket);

  const username = localStorage.getItem("name");

  useEffect(() => {
    // Connect to WebSocket
    stompClient.connect({}, function (frame) {
      console.log("Connected: " + frame);

      // Subscribe to the /topic/notifications topic
      stompClient.subscribe(
        `/topic/${username}/queue/notification`,
        function (message) {
          // Show the notification for 5 seconds
          setNotification(message.body);
          setTimeout(() => {
            setNotification(""); // Hide the notification after 5 seconds
          }, 9000);
        }
      );
    });

    // Cleanup: Disconnect from WebSocket when the component is unmounted
    return () => {
      if (stompClient && stompClient.connected) {
        stompClient.disconnect();
      }
    };
  }, []); // Empty dependency array ensures this runs only once when the component mounts

  useEffect(() => {
    const fetchMeasurements = async () => {
      try {
        const response = await axios.get(
          `http://localhost/monservice/monitoring/${deviceId}/measurements`
        );
        console.log(response.data);
        setMeasurements(response.data); // Assuming response.data contains the list of measurements
      } catch (err) {
        setError("No measurements found");
      } finally {
        setLoading(false);
      }
    };

    fetchMeasurements();
  }, [deviceId]);

  if (loading) {
    return <div>Loading measurements...</div>;
  }

  if (error) {
    return <div className="alert alert-danger">{error}</div>;
  }

  return (
    <div className="container mt-5">
      <h2>Device Measurements</h2>

      {/* Display the notification if it exists */}
      {notification && (
        <div className="alert alert-warning">{notification}</div>
      )}

      {measurements.length === 0 ? (
        <p>No measurements found for this device.</p>
      ) : (
        <ul className="list-group">
          {measurements.map((measurement) => (
            <li key={measurement.id} className="list-group-item">
              For 1 hour {measurement.timestamp} -{" "}
              {measurement.measurementValue} kWh
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
