const functions = require('firebase-functions');

const admin = require('firebase-admin');

const twilio = require('twilio');

var client = new twilio('ACa09242192633777e218acddb9ac1512e', 'f670275673b53ccb6a2764a1afc8262b');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

exports.helloWorld = functions.database.ref('/users/{userId}/panic').onWrite(event => {
    var something = event.data.ref.parent.child('location').limitToLast(1);
    client.messages.create({
        to:'+16609566645',
        from:'+14153607309',
        body: something
    }, function(error, message) {
        // The HTTP request to Twilio will run asynchronously. This callback
        // function will be called when a response is received from Twilio
        // The "error" variable will contain error information, if any.
        // If the request was successful, this value will be "falsy"
        
        if (!error) {
            // The second argument to the callback will contain the information
            // sent back by Twilio for the request. In this case, it is the
            // information about the text messsage you just sent:
            console.log('Success! The SID for this SMS message is:');
            console.log(message.sid);
     
            console.log('Message sent on:');
            console.log(message.dateCreated);
        } else {
            something = "There was an error";
        }
    });
    return event.data.ref.parent.child('modified').set(something);
});
