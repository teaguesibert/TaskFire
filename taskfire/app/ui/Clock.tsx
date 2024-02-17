import React, { useState, useEffect } from 'react';

function Clock() {
  const [currentTime, setCurrentTime] = useState(new Date());

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentTime(new Date());
    }, 1000);

    // Clear interval on component unmount
    return () => clearInterval(timer);
  }, []);

  // Format the time string to exclude seconds
  const timeString = currentTime.toLocaleTimeString([], {
    hour: '2-digit',
    minute: '2-digit'
  });

  // Format the date string
  const dateString = currentTime.toLocaleDateString();

  return (
    <div>
      <p className='text-sm'>{timeString}</p>
      <p className='text-xs'>{dateString}</p>
    </div>
  );
}

export default Clock;
