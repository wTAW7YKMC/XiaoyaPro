/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#10B981',
          dark: '#059669',
          light: '#D1FAE5',
        },
        secondary: '#3B82F6',
        accent: {
          orange: '#F59E0B',
          cyan: '#06B6D4',
        },
        card: '#FFFFFF',
        text: {
          primary: '#1F2937',
          secondary: '#6B7280',
          link: '#10B981',
        },
        border: '#E5E7EB',
        'red-dot': '#EF4444',
        'tag-bg': '#FEF3C7',
      },
      borderRadius: {
        sm: '8px',
        md: '12px',
        lg: '16px',
        xl: '20px',
      },
      boxShadow: {
        card: '0 2px 8px rgba(0, 0, 0, 0.06)',
        float: '0 4px 16px rgba(0, 0, 0, 0.08)',
      },
      fontSize: {
        xs: '12px',
        sm: '14px',
        base: '16px',
        lg: '18px',
        xl: '24px',
      },
    },
  },
  plugins: [],
}
