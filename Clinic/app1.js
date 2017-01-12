var express=require('express');
var bodyParser=require('body-parser');
var connection=require('./connection');
var routes=require('./routes');


var expressJwt=require('express-jwt');

var app=express();
app.use(function(req,res,next){
res.setHeader('Access-Control-Allow-Origin','*');
next();
});
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));


connection.init();
routes.configure(app);
var server=app.listen(80,function(){
console.log('Port is: '+ server.address().port)});
