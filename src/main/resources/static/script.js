// script.js
// Common JavaScript functions for handling session and logout

// Set Axios base URL if needed (e.g., if backend is on a different domain/port for development)
// In this setup, since both frontend and backend are served from the same Spring Boot app,
// relative paths like '/api/auth/login' will work. If running HTML files directly via Live Server
// or a different port, you might need to uncomment and adjust this:
// axios.defaults.baseURL = 'http://localhost:8081';

// Important: Ensure cookies are sent with cross-origin requests (if applicable)
axios.defaults.withCredentials = true;

/**
 * Checks if a session is active. If not, redirects to a specified URL.
 * @param {string} checkSessionUrl - The backend API endpoint to check session status.
 * @param {string} redirectUrl - The URL to redirect to if no session is active.
 * @param {function} [callback] - Optional callback function to execute on successful session check, receives response data.
 */
async function checkSessionAndRedirect(checkSessionUrl, redirectUrl, callback = null) {
    try {
        const response = await axios.get(checkSessionUrl, { withCredentials: true });
        // If response is OK, session is active.
        const data = response.data; // Expecting JSON response with userId and username
        console.log('Session active:', data);
        sessionStorage.setItem('userId', data.userId);
        sessionStorage.setItem('username', data.username);
        if (callback) {
            callback(data); // Call the callback with the data
        }
    } catch (error) {
        console.error('Session check failed:', error.response ? error.response.data : error.message);
        // If session is not active (e.g., 401 Unauthorized), redirect
        if (error.response && error.response.status === 401) {
            sessionStorage.removeItem('userId'); // Clear any lingering user data
            sessionStorage.removeItem('username');
            window.location.href = redirectUrl;
        }
    }
}

/**
 * Handles user logout. Clears session storage and redirects.
 * @param {string} logoutUrl - The backend API endpoint for logout.
 * @param {string} redirectUrl - The URL to redirect to after logout.
 */
async function handleLogout(logoutUrl, redirectUrl) {
    try {
        await axios.post(logoutUrl, {}, { withCredentials: true });
        sessionStorage.clear(); // Clear all session storage
        window.location.href = redirectUrl;
    } catch (error) {
        console.error('Logout failed:', error.response ? error.response.data : error.message);
        // Even if logout fails on server, client-side session should be cleared for safety
        sessionStorage.clear();
        window.location.href = redirectUrl;
    }
}
