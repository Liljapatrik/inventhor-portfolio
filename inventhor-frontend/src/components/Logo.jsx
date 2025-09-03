/**
 * Author: Furo Muktar Eshetu
 * Date: 1 February 2025
 * Description: Logo component for the header navigation bar.
 * It includes a toggle button for the sidebar and the application logo.
 */
import './logo.css';

function Logo() {

    const handleToggleSideBar = () => {
        document.body.classList.toggle('toggle-sidebar');
    }

    return (
        <div className='d-flex align-items-center justify-content-between'>

            {/*Search bar icon*/}
            <i className='bi bi-list toggle-sidebar-btn' onClick={handleToggleSideBar}>

            </i>

            <a href="#" className='logo d-flex align-items-center'>

                <i className="bi bi-box-seam-fill logo-icon"></i>
                <span className='logo-text'>INVENTHOR</span>

            </a>

        </div>
    )
}

export default Logo