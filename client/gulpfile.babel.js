'use strict';

import gulp from "gulp";
import rename from "gulp-rename";
import gulpIf from "gulp-if";
import sass from "gulp-sass";
import sourcemaps from "gulp-sourcemaps";
import uglify from "gulp-uglifyjs";
import zip from "gulp-zip";
import inject from "gulp-inject";
import clean from "gulp-clean";
import util from "gulp-util";
import dateFormat from "date-format";
import path from "path";
import express from "express";
import morgan from "morgan";
import minimatch from "minimatch";
import http from "http";
import httpProxy from "http-proxy";
import tinyLiveReload from "tiny-lr";
import connectLiveReload from "connect-livereload";
import eventStream from "event-stream";
import yargs from "yargs";

const argv = yargs.argv;
const timestamp = dateFormat(argv['timestamp-format'] || process.env['GULP_TIMESTAMP_FORMAT'] || 'yyyyMMddhhmmss');
const liveReload = tinyLiveReload();

const project = require('./package.json');

function proxy(logPrefix, options, proxyPort) {
    let httpServer = http.createServer((req, res) => {
        let option = options.find((option) => {
            return (minimatch(req.url, option.pattern))
        });

        option.proxy.web(req, res);
    });

    httpServer.on('error', (err, req, res) => {
        res.status(503).end();
    });

    util.log(logPrefix, 'Proxy listening on port', util.colors.green(proxyPort));

    httpServer.listen(proxyPort);
}

function proxyOptions(expressPort) {
    return [
        {
            proxy: httpProxy.createProxyServer({
                target: argv['api-url'] || process.env['GULP_API_URL'] || 'http://localhost:8080/'
            }),
            pattern: "/api/**"
        },
        {
            proxy: httpProxy.createProxyServer({
                target: argv['express-url'] || process.env['GULP_EXPRESS_URL'] || 'http://localhost:' + expressPort + '/'
            }),
            pattern: "/**"
        }
    ];
}

function sendStream(res, stream) {
    return stream.pipe(eventStream.map((file, callback) => {
        let contentType = express.static.mime.lookup(file.path);
        let charset = express.static.mime.charsets.lookup(contentType);

        res.set('Content-Type', contentType + (charset ? '; charset=' + charset : ''));

        callback(null, file.contents);
    })).pipe(res);
}

function expressIndex() {
    return (req, res) => {
        sendStream(res, gulp.src('src/main/webapp/index.html')
            .pipe(inject(gulp.src(['target/gulp/main/js/app.js', 'target/gulp/main/css/style.css']), {
                ignorePath: 'target/gulp/main',
                addRootSlash: false
            })));
    }
}

function expressJasmine() {
    return (req, res, next) => {
        let staticServer = express.static('src/test/webapp');

        if (req.path == '/') {
            sendStream(res, gulp.src('src/test/webapp/html/jasmine-index.html')
                .pipe(inject(gulp.src('target/gulp/test/js/app.js'), {
                    ignorePath: 'target/gulp',
                    addRootSlash: false
                })));
        }
        else {
            staticServer(req, res, next);
        }
    };
}

function expressStatic(path) {
    return (req, res, next) => {
        let staticServer = express.static(path);

        return staticServer(req, res, () => {
            res.status(404).send('Not Found');
        });
    };
}

function appJs() {
    return gulp.src([
        'src/main/webapp/js/jquery/jquery.js',
        require.resolve('angular/angular.js'),
        require.resolve('angular-aria/angular-aria.js'),
        require.resolve('angular-cookies/angular-cookies.js'),
        require.resolve('angular-messages/angular-messages.js'),
        require.resolve('angular-route/angular-route.js'),
        require.resolve('angular-sanitize/angular-sanitize.js'),
        require.resolve('bootstrap-sass/assets/javascripts/bootstrap.js'),
        'src/main/webapp/js/angular-xeditable/*.js',
        'src/main/webapp/js/custom/*.js',
        'src/main/webapp/js/custom/**/*.js'
    ]);
}

function compileJs() {
    return appJs()
        .pipe(uglify('app.js', {
            basePath: 'js',
            mangle: false,
            outSourceMap: argv['uglify-out-source-maps'] || process.env['GULP_UGLIFY_OUT_SOURCE_MAPS'] !== undefined
        }))
        .pipe(gulp.dest('target/gulp/main/js'));
}

function compileScss(errLogToConsole) {
    let enableSassSourceMaps = argv['enable-sass-source-maps'] || process.env['GULP_ENABLE_SASS_SOURCE_MAPS'] !== undefined;

    return gulp.src('src/main/webapp/scss/main.scss')
        .pipe(gulpIf(enableSassSourceMaps, sourcemaps.init()))
        .pipe(sass({
            errLogToConsole: errLogToConsole,
            outputStyle: argv['sass-output-style'] || process.env['GULP_SASS_OUTPUT_STYLE'] || 'compressed',
            includePaths: [
                'node_modules/bootstrap-sass/assets/stylesheets',
                'node_modules/font-awesome/scss'
            ],
            sourceMap: '' // Required to prevent gulp-sass from crashing.
        }))
        .pipe(rename('style.css'))
        .pipe(gulpIf(enableSassSourceMaps, sourcemaps.write('.')))
        .pipe(gulp.dest('target/gulp/main/css'));
}

function testJs() {
    return gulp.src([
        require.resolve('angular/angular.js'),
        require.resolve('angular-route/angular-route.js'),
        require.resolve('angular-messages/angular-messages.js'),
        require.resolve('angular-aria/angular-aria.js'),
        require.resolve('angular-mocks/angular-mocks.js'),
        'src/test/webapp/specs/angular/angular-jasmine.js',
        'src/main/webapp/js/custom/*.js',
        'src/main/webapp/js/custom/**/*.js',
        'src/test/webapp/specs/custom/*.spec.js'
    ]);
}

function compileTestJs() {
    return testJs()
        .pipe(uglify('app.js', {
            basePath: 'js',
            mangle: false,
            outSourceMap: argv['uglify-out-source-maps'] || process.env['GULP_UGLIFY_OUT_SOURCE_MAPS'] !== undefined
        }))
        .pipe(gulp.dest('target/gulp/test/js'));
}

// ************************************ //
// ************** TASKS *************** //
// ************************************ //

gulp.task('compile-font', () => {
    return gulp.src(['node_modules/bootstrap-sass/assets/fonts/**/**.*', 'node_modules/font-awesome/fonts/**.*'])
        .pipe(gulp.dest('target/gulp/main/fonts'));
});

gulp.task('compile-js', () => {
    return compileJs();
});

gulp.task('watch-js', ['compile-js'], () => {
    let logPrefix = '[' + util.colors.blue('watch-js') + ']';

    gulp.watch('src/main/webapp/js/**/*.js', () => {
        util.log(logPrefix, 'Recompiling JS');

        compileJs();
    });

    gulp.watch('target/gulp/main/js/*.js', (event) => {
        util.log(logPrefix, 'Reloading', path.relative('target/gulp/main/js', event.path));

        liveReload.changed({
            body: {
                files: [
                    path.relative('target/gulp/main/js', event.path)
                ]
            }
        });
    });
});

gulp.task('compile-scss', () => {
    return compileScss(false);
});

gulp.task('watch-scss', ['compile-scss'], () => {
    let logPrefix = '[' + util.colors.blue('watch-scss') + ']';

    gulp.watch('src/main/webapp/scss/**/*.scss', () => {
        util.log(logPrefix, 'Recompiling SCSS');

        compileScss(true);
    });

    gulp.watch('target/gulp/main/css/*.css', (event) => {
        util.log(logPrefix, 'Reloading', path.relative('target/gulp/main/css', event.path));

        liveReload.changed({
            body: {
                files: [
                    path.relative('target/gulp/main/css', event.path)
                ]
            }
        });
    });
});

gulp.task('watch-html', () => {
    let logPrefix = '[' + util.colors.blue('watch-html') + ']';

    gulp.watch(['src/main/webapp/index.html', 'src/main/webapp/html/**'], (event) => {
        util.log(logPrefix, 'Reloading', path.relative('src/main/webapp', event.path));

        liveReload.changed({
            body: {
                files: [
                    path.relative('src/main/webapp', event.path)
                ]
            }
        });
    });
});

gulp.task('compile-test-js', () => {
    return compileTestJs();
});

gulp.task('watch-test-js', ['compile-test-js'], () => {
    let logPrefix = '[' + util.colors.blue('watch-test-js') + ']';

    gulp.watch('src/test/webapp/specs/**/*.js', () => {
        util.log(logPrefix, 'Recompiling Test JS');

        compileTestJs();
    });

    gulp.watch('target/gulp/test/js/*.js', (event) => {
        util.log(logPrefix, 'Reloading', path.relative('target/gulp/test/js', event.path));

        liveReload.changed({
            body: {
                files: [
                    path.relative('target/gulp/test/js', event.path)
                ]
            }
        });
    });
});

gulp.task('zip', ['compile-font', 'compile-js', 'compile-scss'], () => {
    let buildNumber = argv['build-number'] || process.env['GULP_BUILD_NUMBER'];
    let filename = argv['zip-filename'] || process.env['GULP_ZIP_FILENAME'] || project.name + '-' + project.version + (buildNumber !== undefined ? '+build.' + buildNumber : '') + '.zip';

    util.log('Creating', util.colors.magenta(filename));

    return eventStream.merge(
        gulp.src([
            argv['uglifyjs-out-source-maps'] || process.env['GULP_UGLIFYJS_OUT_SOURCE_MAPS'] ? 'src/main/webapp/js/**' : '',
            'src/main/webapp/html/**',
            'src/main/webapp/img/**'
        ], {base: 'src/main/webapp'}),

        gulp.src('target/gulp/main/**'),

        gulp.src('src/main/webapp/index.html')
            .pipe(inject(gulp.src(['target/gulp/main/js/app.js', 'target/gulp/main/css/style.css']), {
                ignorePath: 'target/gulp/main',
                addRootSlash: false
            })))
        .pipe(zip(filename))
        .pipe(gulp.dest('target'));
});

gulp.task('run', ['compile-font', 'watch-html', 'watch-js', 'watch-scss', 'watch-test-js'], () => {
    let logPrefix = '[' + util.colors.blue('run') + ']';

    let proxyPort = argv['proxy-port'] || process.env['GULP_PROXY_PORT'] || 4444;
    let expressPort = argv['express-port'] || process.env['GULP_EXPRESS_PORT'] || 7777;
    let liveReloadPort = argv['live-reload-port'] || process.env['GULP_LIVE_RELOAD_PORT'] || 35729;

    liveReload.listen(liveReloadPort);

    proxy(logPrefix, proxyOptions(expressPort), proxyPort);

    return express()
        .use(morgan('combined'))
        .use('/css', expressStatic('target/gulp/main/css'))
        .use('/fonts', expressStatic('target/gulp/main/fonts'))
        .use('/html', expressStatic('src/main/webapp/html'))
        .use('/img', expressStatic('src/main/webapp/img'))
        .use('/js', expressStatic('target/gulp/main/js'))
        .use(connectLiveReload({
            port: liveReloadPort
        }))
        .use('/test/js', expressStatic('target/gulp/test/js'))
        .use('/test', expressJasmine())
        .use('/specs', expressStatic('src/test/webapp/specs'))
        .use(expressIndex())
        .listen(expressPort, function () {
            util.log(logPrefix, 'Express listening on port', util.colors.green(expressPort));
        });
});

gulp.task('clean', () => {
    return gulp.src('target/gulp', {read: false})
        .pipe(clean());
});

gulp.task('default', ['zip']);