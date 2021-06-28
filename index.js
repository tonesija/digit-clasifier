const express = require("express")
const path = require("path")
const bodyparser = require('body-parser')
const fs = require('fs')

const app = express()

app.use(express.static(path.join(__dirname, 'static')))
app.use(bodyparser.urlencoded({
  extended: true
}))

app.post('/clasify', (req, res) => {
  const img = req.body.file
  console.log(img)
  //save img
  var base64Data = img.replace(/^data:image\/png;base64,/, "")
  var imgName = new Date()+'.png'
  fs.writeFileSync("uploads/"+imgName, base64Data, 'base64',
    function(err) {
      console.log(err)
    })
  //send UDP packet to neural net
  //wait for UDP answer 
  //return the message
})

console.log('Server listening on port 7070.')
app.listen(7070)