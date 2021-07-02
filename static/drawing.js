// SETTING ALL VARIABLES
var isMouseDown=false;
var canvas = document.createElement('canvas');
var body = document.getElementsByTagName("body")[0];
var ctx = canvas.getContext('2d');
var linesArray = [];
var currentSize = 28;
var currentColor = "rgb(255,255,255)";
var currentBg = "black";
var sizeX = 400
var sizeY = 400

// INITIAL LAUNCH
createCanvas();

// BUTTON EVENT HANDLERS
document.getElementById('eraser').addEventListener('click', eraser);
document.getElementById('clear').addEventListener('click', createCanvas);
document.getElementById('send').addEventListener('click', send)

// REDRAW 
function redraw() {
    for (var i = 1; i < linesArray.length; i++) {
      ctx.beginPath();
      ctx.moveTo(linesArray[i-1].x, linesArray[i-1].y);
      ctx.lineWidth  = linesArray[i].size;
      ctx.lineCap = "round";
      ctx.strokeStyle = linesArray[i].color;
      ctx.lineTo(linesArray[i].x, linesArray[i].y);
      ctx.stroke();
    }
}

// DRAWING EVENT HANDLERS
canvas.addEventListener('mousedown', function() {mousedown(canvas, event);});
canvas.addEventListener('mousemove',function() {mousemove(canvas, event);});
canvas.addEventListener('mouseup',mouseup);

// CREATE CANVAS
function createCanvas() {
  canvas.id = "canvas";
  canvas.width = parseInt(sizeX);
  canvas.height = parseInt(sizeY);
  canvas.style.zIndex = 8;
  //canvas.style.position = "relative";
  canvas.style.border = "1px solid";
  ctx.fillStyle = currentBg;
  ctx.fillRect(0, 0, canvas.width, canvas.height);
  document.getElementById('canvas').appendChild(canvas)
}

// ERASER HANDLING
function eraser() {
  currentSize = 50;
  currentColor = ctx.fillStyle
}

// UPLOAD IMAGE
function send() {
  var dataURL = canvas.toDataURL('image/jpeg')

  let data = {data: dataURL}
  fetch('clasify', {
    method: 'POST', 
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  })
  .then(response => response.json())
  .then(data => {
    console.log('Success:', data)
    document.getElementById('message').innerHTML = data.msg
  })
  .catch((error) => {
    console.error('Error:', error);
  });
}

// GET MOUSE POSITION
function getMousePos(canvas, evt) {
  var rect = canvas.getBoundingClientRect();
  return {
    x: evt.clientX - rect.left,
    y: evt.clientY - rect.top
  };
}

// ON MOUSE DOWN
function mousedown(canvas, evt) {
  isMouseDown=true
  var currentPosition = getMousePos(canvas, evt);
  ctx.moveTo(currentPosition.x, currentPosition.y)
  ctx.beginPath();
  ctx.lineWidth  = currentSize;
  ctx.lineCap = "round";
  ctx.strokeStyle = currentColor;
}

// ON MOUSE MOVE
function mousemove(canvas, evt) {
  if(isMouseDown){
    var currentPosition = getMousePos(canvas, evt);
    ctx.lineTo(currentPosition.x, currentPosition.y)
    ctx.stroke();
  }
}

// ON MOUSE UP
function mouseup() {
  isMouseDown=false
}