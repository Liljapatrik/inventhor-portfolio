/**
 * Author: Tatiana Fløisbonn
 * Date: 1 February 2025
 * Description: NavThemeMode component for toggling dark mode in the navigation bar.
 * This component checks the local storage for dark mode status and toggles it accordingly.
 */

function NavThemeMode() {

    // Check local storage for dark mode status
    let darkmode = localStorage.getItem('darkmode');

    const themeSwitch = document.getElementById('theme-switch');

    // If dark mode is not set, default to light mode
    const enableDarkmode = () => {
        document.body.classList.add('darkmode')
        localStorage.setItem('darkmode', 'active')
    }
    // If dark mode is set, remove dark mode class and update local storage
    const disableDarkmode = () => {
        document.body.classList.remove('darkmode')
        localStorage.setItem('darkmode', null)
    }
    // If dark mode is active, enable it on initial load
    if(darkmode === 'active') enableDarkmode();

    // Toggle dark mode based on current status
    const toggleDarkMode = () =>
    {
        darkmode = localStorage.getItem('darkmode')
        darkmode !== "active" ? enableDarkmode(): disableDarkmode()
    }

  return (
    
    <div className='nav-theme-mode'>
        <button id='theme-switch' onClick={ 
            () => {
                toggleDarkMode();
            }
            }>
            <i className='bi bi-moon'></i>
            <i className="bi bi-brightness-high"></i>
        </button>
    </div> 
  )
}

export default NavThemeMode;