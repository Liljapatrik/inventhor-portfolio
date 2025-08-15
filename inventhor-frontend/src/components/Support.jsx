/**
 * Author: Furo Muktar Eshetu
 * Date: 10 March 2025
 * Description: This component displays support information and contact details.
 * It includes a phone number and an email address for users to reach out for support.
*/

import './support.css'; // Importing the CSS file for styling


function Support() {
  return (
    <div className="support">
        <h1>Support</h1>
        <h5>We can always help you by phone call</h5>
        <h1>123456789</h1>
        <h5>Or you can send us an email</h5>
        <h1>inventhor@support.no</h1>
    </div>
  )
}

export default Support;