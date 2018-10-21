const webpack = require('webpack');
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const branchName = require('current-git-branch');
const branch = branchName();

console.info("Branch: " + branch);

module.exports = (env, options) => {
    var isDev = options.mode === 'development';

    return {
        mode: isDev ? 'development' : 'production',
        performance: {
            hints: process.env.NODE_ENV === 'production' ? "warning" : false
        },
        entry: {
            backoffice: './src/main/ts/Index.tsx'
        },
        output: {
            path: path.resolve(__dirname, 'src/main/resources/static'),
            filename: 'scripts/[chunkhash].js',
            publicPath: isDev ? '/' : '/' + branch
        },
        devtool: 'source-map',
        resolve: {
            extensions: ['.js', '.json', '.ts', '.tsx']
        },
        plugins: [
            new HtmlWebpackPlugin({
                title: 'User Management',
                filename: path.resolve(__dirname, 'src/main/resources/static/index.html'),
                template: path.resolve(__dirname, 'src/main/resources/template/template.html')
            }),
            new webpack.DefinePlugin({
                CONTEXT_PATH: isDev ? JSON.stringify('') : JSON.stringify('/' + branch)
            })
        ],
        module: {
            rules: [
                {
                    test: /\.(ts|tsx)$/,
                    loader: "awesome-typescript-loader"
                },
                {
                    test: /\.css$/,
                    use: [
                        {
                            loader: 'style-loader'
                        },
                        {
                            loader: 'css-loader'
                        }
                    ]
                },
                {
                    test: /\.(jpg|jpeg|png|gif|svg)$/,
                    use: {
                        loader: 'url-loader' || 'file-loader',
                        options: {
                            name: 'images/[hash].[ext]',
                            limit: 1000 // inline file data until size
                        }
                    }
                }
            ]
        },
        devServer: {
            contentBase: path.join(__dirname, 'src/main/resources/static'),
            publicPath: isDev ? '/' : '/' + branch,
            compress: true,
            port: 9000,
            proxy: {
                '/api/**': {
                    target: isDev ? 'http://localhost:8080' : 'http://localhost:8080/' + branch,
                    secure: false,
                    changeOrigin: true
                }
            }
        }
    }
}