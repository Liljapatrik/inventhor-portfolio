/**
 * Author Tatiana Fløisbonn
 * Date: 1 February 2025
 * Description: SearchBar component for displaying a search bar with an input field and a search icon button.
 * It is not used in the current application but can be used for future search functionality.
 */


import './searchBar.css';

function SearchBar() {
  return (
    <div className='search-bar'>
        
        <form className='search-form d-flex align-items-center' method='POST' action="#">
            
            <button type='submit'>
                <i className='bi bi-search search-icon'></i>
            </button>
            <input type='text' name='query' placeholder='Search'/>

        </form>

    </div>
  )
}

export default SearchBar;