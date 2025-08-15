/**
 * Author: Tatiana Fløisbonn
 * Date: 1 February 2025
 * Description: Nav component for the header navigation bar.
 * It includes theme mode toggle, notifications, and user profile components.
 */
import './nav.css';
import NavThemeMode from './NavThemeMode';
import NavNotice from './NavNotice';
import NavUserProfile from './NavUserProfile';

function Nav({authorisedUser, getNotificationsForEmployee, updateNotification, logout}) {

  return (
    <nav className='header-nav ms-auto'>
        <ul className='d-flex align-items-center'>
            <NavThemeMode />
            <NavNotice getNotificationsForEmployee={getNotificationsForEmployee} authorisedUser={authorisedUser} updateNotification={updateNotification} />
            {/* The Nav component receives the isAuthenticated prop cheks if user is authorised.
            If yes, the it shows <NavUserProfile /> component*/}
            {Object.keys(authorisedUser).length > 0 && <NavUserProfile authorisedUser={authorisedUser} logout={logout}/>}
        </ul>

    </nav>
  );
}

export default Nav;