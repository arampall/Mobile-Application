var user=require('./models/userapi');
var jwt=require('jsonwebtoken');
var cryptojs=require('crypto-js');
var path=require('path');

module.exports={
configure:function(app){
app.post('/getUserDetails/',function(req,res){
user.getUser(req.body,res);
});

app.get('/getAllUserDetails/',function(req,res){
user.getAllUsers(res);
});

app.post('/createUser/',function(req,res){
user.createUser(req.body,res);
});

app.get('/getQuestions/',requireAuthentication,function(req,res){
user.getQuestions(res);
});

app.post('/getAdmin/',function(req,res){
//console.log(req.body);
user.getAdmin(req.body,res);
});

app.post('/sendAnswer/',requireAuthentication,function(req,res){
console.log(req.body);
user.sendAnswer(req.body,res);
});

app.post('/getReport/',function(req,res){
user.getAnswer(req.body,res);
});

app.post('/registerDevice/',function(req,res){
console.log('registered device');
user.registerDevice(req.body,res);
});

app.post('/getDevice/',function(req,res){
console.log('get device request');
user.getDevice(req.body,res);
});

app.post('/getAllDevices/',function(req,res){
console.log('get all devices');
user.getAllDevices(req.body,res);
});
app.post('/pushNotification/',function(req,res){
user.push(req.body,res);
});
}
};

function requireAuthentication (req, res, next) {
    var token = req.get('Auth');

    Authenticate(token).then(function (tokenData){
        res.locals.user = tokenData.UserId;
        
        next();
    }, function () {
        console.log(err);
        res.status(401).send();
    });

}

function Authenticate(token){
    return new Promise(function(resolve, reject){
        try{
            var decodedJWT = jwt.verify(token, 'jwtHS256');
            var bytes = cryptojs.AES.decrypt(decodedJWT.token, 'clinic28262');
            var tokenData = JSON.parse(bytes.toString(cryptojs.enc.Utf8));
            
            resolve(tokenData);
        }catch(err){
            reject();
        }
    });
}
