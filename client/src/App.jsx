  import { react ,useState} from 'react'

  import './App.css'
import Signup from './pages/Signup';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';

  function App() {
    
  const [value ,setValue] = useState(500);

  return (
    <>
        <div>
          {/* <Signup/> */}
          {/* <Login/>   */}
          <Dashboard/>

        </div>
    </>
  ) 

}
          
export default App
          

