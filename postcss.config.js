const postcssImport = require('postcss-import');
const tailwindCss = require('tailwindcss');
const autoprefixer = require('autoprefixer');
const csso = require('postcss-csso');
const purgecss = require('@fullhuman/postcss-purgecss')({
    content: ['./src/**/*.cljs'],
    defaultExtractor: content => {
        // Taken from tailwind documentation
        // https://tailwindcss.com/docs/controlling-file-size/#setting-up-purgecss
        const classes = content.match(/[\w-/:]+(?<!:)/g) || [];

        // For all function calls, strip leading paren, and namespace if one exists
        const invocations = content.match(/\([\w\.\-\>/]+/g) || [];
        const elements = invocations.map(invocation => {
            const symbol = invocation.substring(1);
            const slash = symbol.lastIndexOf('/');
            return slash === -1 ? symbol : symbol.substring(slash + 1);
        });

        return [...classes, ...elements];
    }
});

const plugins = [postcssImport, tailwindCss, autoprefixer];

const productionPlugins = [...plugins, purgecss, csso];

module.exports = ({ env, options }) => {
    const isProduction = env === 'production';

    return {
        ...options,
        plugins: isProduction ? productionPlugins : plugins,
        map: !isProduction
    };
};
