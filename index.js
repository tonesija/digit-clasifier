const express = require("express")
const path = require("path")
const bodyparser = require('body-parser')
const fs = require('fs')
const ba64 = require('ba64')

const dgram = require('dgram')

const app = express()

app.use(express.static(path.join(__dirname, 'static')))
app.use(bodyparser.json())

app.post('/clasify', (req, res) => {
  const img = req.body.data
  //save image to disk
  var imgName = new Date()
  var path = 'uploads/'+imgName
  var imgBuffer = decodeBase64Image(img)
  fs.writeFileSync(path, imgBuffer.data, {encoding: 'utf8'})

  //send UDP packet to neural net
  const client = dgram.createSocket('udp4')
  client.send(path, 
    0, Buffer.byteLength(path, 'utf8'),
    5555, "127.0.0.1")

  //wait for UDP answer 
  client.on('message', function(msg){
    let message = msg.toString()
    console.log(message)
    res.send({msg: message})
  })
})

console.log('Server listening on port 7070.')
app.listen(7070)



function decodeBase64Image(dataString) {
  var matches = dataString.match(/^data:([A-Za-z-+\/]+);base64,(.+)$/),
    response = {};

  if (matches.length !== 3) {
    return new Error('Invalid input string');
  }

  response.type = matches[1];
  response.data = new Buffer(matches[2], 'base64');

  return response;
}