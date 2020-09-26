module.exports = {
  purge: false,
  theme: {
    inset: {
      '0': 0,
      '1/4': '0.25rem',
      '1/3': '0.35rem',
      '1': '1rem',
      '2': '2rem',
      auto: 'auto',
      'middle': '50%',
      '30': '1.875rem'
    },
    animations: {
      spin: {
        from: {
          transform: 'rotate(0deg)'
        },
        to: {
          transform: 'rotate(360deg)'
        }
      },
      fadeIn: {
        '0%': {
          opacity: 0
        },
        '100%': {
          opacity: 1
        }
      }
    },
    extend: {
      animationDuration: {
        '0.5s': '0.5s',
      },
      borderRadius: {
        'xl': '0.75rem'
      },
      colors: {
        'slate': '#2D343A',
        'emerald': '#2B884A',
        'barley': '#846C5B',
        'pale-ale': '#E3D257',
        'steel': '#8C9C94',
        'background-gray': '#CCCCCC'
      },
      lineHeight: {
        '12': '3rem',
        '18': '4.5rem'
      },
      width: {
        '11': '2.75rem',
        '51': '12.75rem',
        '71': '17.75rem'
      },
      height: {
        '11': '2.75rem',
        '13': '3.25rem',
        'input': '53px',
        '9/10': '90%'
      },
      margin: {
        '9': '2.25rem'
      },
      minHeight: {
        '12': '3rem'
      },
      zIndex: {
        '75': 75
      }
    }
  },
  variants: {},
  plugins: [
    function ({ addUtilities }) {
      addUtilities(
        {
          '.content-empty': {
            content: "''"
          },
          '.content-none': {
            content: "none"
          }
        }
      );
    }
  ],
  future: {
    removeDeprecatedGapUtilities: true
  }
};
