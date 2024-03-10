import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        'primary': {
          '100': '#f6b17a',
          '200': '#f8ba88',
          '300': '#fac297',
          '400': '#fccba5',
          '500': '#fdd3b4',
          '600': '#fedcc3',
        },
        'surface': {
          '100': '#121212',
          '200': '#282828',
          '300': '#3f3f3f',
          '400': '#575757',
          '500': '#717171',
          '600': '#8b8b8b',
        },
        'surface-mixed': {
          '100': '#25201c',
          '200': '#3a3531',
          '300': '#4f4b47',
          '400': '#66625f',
          '500': '#7e7a77',
          '600': '#969391',
        },
      },
    },
  },
  plugins: [],
};
export default config;
