const express = require("express")
const path = require("path")

const app = express()

app.use(express.static(path.join(__dirname, 'static')))

console.log('Server listening on port 7070.')
app.listen(7070)