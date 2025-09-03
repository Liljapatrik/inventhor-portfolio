/**
 * Author: Tatiana Fløisbonn
 * Date: 1 February 2025
 * Description: NavUserProfile component for displaying the user's profile in the navigation bar.
 * It includes the user's name, position, and links to profile settings, support, and logout.
 */

function NavUserProfile({authorisedUser, logout}) {

    return (
        <li className='nav-item dropdown'>
            
            <a className='nav-link nav-profile d-flex align-items-center' href='#' data-bs-toggle="dropdown">
            
            <i className="bi bi-person-circle"></i>

            { authorisedUser &&
                <span className='nav-profile-name'>
                    {authorisedUser.firstname} {authorisedUser.lastname.charAt(0)}.
                </span>
            }

            </a>

            <ul className='dropdown-menu dropdown-menu-end dropdown-menu-arrow profile'>

                {authorisedUser &&
                
                    <li className='dropdown-header'>
                        
                        <h4>{authorisedUser.firstname} {authorisedUser.lastname}</h4>
                        <span>{authorisedUser.position}</span>

                    </li>
                
                }

                <li>
                    <hr className='dropdown-divider'/>
                </li>

                <li>
                    <a className='dropdown-item d-flex align-items-center' href='/settings'>
                        <i className='bi bi-person'></i>
                        <span href>My Profile</span>
                    </a>
                </li>

                <li>
                    <hr className='dropdown-divider'></hr>
                </li>

                <li>
                    <a className='dropdown-item d-flex align-items-center' href='/support'>
                        <i className='bi bi-question-circle'></i>
                        <span>Need Help?</span>
                    </a>
                </li>

                <li>
                    <hr className='dropdown-divider'></hr>
                </li>

                <li>
                    <a className='dropdown-item d-flex align-items-center' onClick={logout}>
                        <i className='bi bi-box-arrow-right'></i>
                        <span>Sign Out</span>
                    </a>
                </li>

            </ul>

        </li>
    )
}

export default NavUserProfile;