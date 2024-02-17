// pages/register.tsx
import { useRouter } from 'next/router';
import React, { useState } from 'react';
import "../app/globals.css"
import { Noto_Sans } from "next/font/google";
import Image from 'next/image';

const noto = Noto_Sans({ 
  subsets: ["latin"],
  weight: "700", 
});

const RegisterPage = () => {
  const router = useRouter();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  
  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
  
    const payload = {
      name: username,
      password: password
    };
  
    try {
      const response = await fetch('https://taskfireapi.jamesellerbee.com/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
      });

      if (response.ok) {
        const data = await response.json();
        console.log(data)
        setSuccessMessage('Registered successfully! Redirecting to login...');

        // Redirect after a short delay
        setTimeout(() => {
          router.push('/');
        }, 3000);
      } else {
        // Handle errors
        console.error('Registration failed');
      }
    } catch (error) {
      console.error('There was an error registering the user', error);
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
    
    <div className="w-full max-w-xs">
      <h1 className="text-center text-3xl font-bold mb-8
      "><span className={noto.className}>TaskFire</span>
      </h1>
      {successMessage && (
        <div className="text-green-500 text-center my-2">
          {successMessage}
        </div>
      )}
      <form className="bg-white shadow-md rounded px-6 pt-6 pb-8 mb-4" onSubmit={handleRegister}>
        <h2 className="text-center text-2xl font-bold mb-8 text-amber-500" ><span className={noto.className}>Register</span></h2>
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
        <div className="flex items-center justify-between">
          <button className="bg-amber-600 hover:bg-amber-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline" type="submit">
            Register
          </button>
          <a className="inline-block align-baseline font-bold text-sm text-amber-600 hover:text-amber-700 " href="#" onClick={() => router.push('/')}>
            Already have an account?
          </a>
        </div>
      </form>
    </div>
  </div>
  );
};

export default RegisterPage;
