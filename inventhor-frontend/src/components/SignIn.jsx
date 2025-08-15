/**
 * Author: Tatiana Fløisbonn
 * Date: 14 February 2025
 * Description: SignIn component for handling user authentication with Keycloak.
 * It provides a button to log in with Keycloak and displays the logged-in user's information.
 * It uses the `useKeycloak` hook from the `@react-keycloak/web` library to manage authentication state.
 */

import React, { useRef } from 'react';
import './signIn.css';

// Import the useKeycloak hook from the @react-keycloak/web library
// This hook provides access to Keycloak's authentication state and methods.
import { useKeycloak } from "@react-keycloak/web";


function SignIn({login}) {

    // Destructure keycloak and initialized from the useKeycloak hook
    const { keycloak, initialized } = useKeycloak();
      
    let emailRef = useRef();
    let passwordRef = useRef();

    // Check if Keycloak is initialized before rendering the component
    function onSignInButtonClick() {
        login(emailRef.current.value, passwordRef.current.value);
    }

    // Function to update the authorized user in localStorage
    function updateAuthorizedUser() {

        if (keycloak.authenticated) {

            localStorage.setItem("access_token", keycloak.token);

            login(keycloak.tokenParsed.email);
        }
    }

    return <div className='signInContainer'>

        <div className="formSide">

            
            {!keycloak.authenticated && (
                <button className="signInButton" onClick={() => keycloak.login()}>Login with Keycloak</button>
            )}
            {keycloak.authenticated && updateAuthorizedUser() && (
                <div>You're logged in as {keycloak.tokenParsed.preferred_username}</div>

            )}
            
        </div>

        <div className="imageSide">

            <img  src="/images/Picture for Sign in page.svg" />

        </div>


    </div>
}

export default SignIn;