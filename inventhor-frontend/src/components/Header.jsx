/**
 * Author: Tatiana Fløisbonn
 * Date: 1 February 2025
 * Description: Header component for the application.
 * It includes the logo, navigation bar, and user profile.
 */

import './header.css';
import Logo from './Logo';
import Nav from './Nav';

function Header({authorisedUser, logout, getNotificationsForEmployee, updateNotification}) {
  return (
    <header id='header' className='header fixed-top d-flex align-items-center'>
        {/*Logo*/}
        <Logo />
        {/*Navigation*/}
        <Nav getNotificationsForEmployee={getNotificationsForEmployee} authorisedUser={authorisedUser} updateNotification={updateNotification} logout={logout}/>
    </header>
  )
}

export default Header;