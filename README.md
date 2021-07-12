# About
Project that uses neural network with backpropagation for digit clasification. Project includes a express backend server
and a neural network written in Java that builds into a jar file which uses UDP to communicate with the server.

## Neural network
This clasifier uses a standard sigmoid neural network that uses backpropagation as a learning mechanism. I have gotten
best results with 784,128,128,128,10 layout which achieved 96% accuracy (verification set) at 36 epoch. ETA was 0.05.

## Training the network.
1. Create *slike* folder.
2. Inside the folder create 10 folders called [0-9], eg. "0", "1", ...,"9".
3. Place the MINST digit images inside them. (this is the training set)
4. Inside *slike* folder create *check* folder.
5. Do step 2 for *check* folder.
6. Place different MINST digit images inside them. (this is the verification set)
7. Run Net.jar.

I used about 3k for each digit in the training set and about 700 for each digit in the verification set.


## Starting the server locally
1. Open a terminal and run *npm install*
2. Run *npm start* to start the node server.
3. Access the website with url http://localhost:7070.
