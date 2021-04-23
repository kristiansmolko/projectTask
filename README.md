# projectTask

This is project Task manager.

## Instalation

First of all [nodejs](https://nodejs.org/en/download/) is needed. 

Install express to create server:

```
npm install express
```

Then, to work with mongodb database:

```
npm install mongodb
```

Optional: install nodemon to run server "live":

```
npm install nodemon
```

In order use nodemon, a small change has to be made: add to settings in VSCode (or any other IDE):

```
"terminal.integrated.shellArgs.windows": ["-ExecutionPolicy", "Bypass"]
```

Warning: this command will allow third-party scripts to be run on your computer.

## Usage

To start new project:

```
npm init
```

After series of information, package.json is created with data about project.
Next you need to do, is create new .js file (what you specified, or default index.js)

### Start

Without nodemon:

```
node index.js
```

With nodemon:

```
nodemon index.js
```

## External links

Working with [date](https://usefulangle.com/post/187/nodejs-get-date-time) in nodejs. <br> Getting [body](https://nodejs.dev/learn/get-http-request-body-data-using-nodejs). <br>
[Query](https://www.javatpoint.com/nodejs-mongodb-query) mongodb. 

