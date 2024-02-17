// pages/login.tsx
import { useRouter } from 'next/router';
import React, { useState } from 'react';
import "../app/globals.css"
import { Noto_Sans } from "next/font/google";
import Image from 'next/image';

const noto = Noto_Sans({ 
  subsets: ["latin"],
  weight: "700", 
});

const LoginPage = () => {
  const router = useRouter();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();

    const name = username;
    try {
      const response = await fetch('https://taskfireapi.jamesellerbee.com/auth', {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
          "Access-Control-Allow-Origin": "https://taskfireapi.jamesellerbee.com/",
        },
        body: JSON.stringify({ name, password }),
      });

      if (response.ok) {
        const data = await response.json();
        //console.log(data)
        localStorage.setItem('accountUid', (data.id));
        router.push('/dashboard')
        // console.log(response.json())
      } else {
        setErrorMessage('Invalid username or password.');
      }
    } catch (error) {
      setErrorMessage('There was a problem connecting to the server.');
      console.error('Login error:', error);
    }
  };

  return (
    <div className="flex flex-col justify-center items-center h-screen bg-slate-500">
<Image
      src="/NotesApp.svg"
      width={65}
      height={65}
      alt="TaskFire Logo"
      className='mb-3'
    />
      <div className="w-full max-w-xs ">
      
       <h1 className="text-center text-3xl font-bold mb-8" ><span className={noto.className}>TaskFire</span></h1>

      <form className="bg-white shadow-xl rounded px-6 pt-6 pb-8 mb-4" onSubmit={handleLogin}>
        <h2 className="text-center text-2xl font-bold mb-8 text-amber-500" ><span className={noto.className}>Login</span></h2>
        <div className="m-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="username">
            Username
          </label>
          <input 
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" 
            id="username" 
            type="text" 
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div className="m-4">
          <label className="block text-gray-700 text-sm font-bold mb-2" htmlFor="password">
            Password
          </label>
          <input 
            className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline" 
            id="password" 
            type="password" 
            placeholder="*********"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        
       {errorMessage && <p className="text-red-500 text-center mb-2">{errorMessage}</p>}

        <div className="flex items-center justify-between">
          <button className="bg-amber-600 hover:bg-amber-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline" type="submit">
            Sign In
          </button>
          <a className="inline-block align-baseline font-bold text-sm text-amber-600 hover:text-amber-700" href="#" onClick={() => router.push('/register')}>
          Create An Account
          </a>
        </div>
      </form>
      </div>
    </div>
  );
};

export default LoginPage;
