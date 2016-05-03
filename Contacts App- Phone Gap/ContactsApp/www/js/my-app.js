// Initialize your app
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.receivedEvent('deviceready');
    },
    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

app.initialize();
var myApp = new Framework7({
    animateNavBackIcon:true
});

// Export selectors engine
var $$ = Dom7;

// Add main View
var mainView = myApp.addView('.view-main', {
    // Enable dynamic Navbar
    dynamicNavbar: true,
    // Enable Dom Cache so we can use all inline pages
    domCache: true
});
$$('#getpic').on("click",function(){
	navigator.camera.getPicture(onSuccess, onFail, {
		quality:50,
		destinationType: Camera.DestinationType.FILE_URI,
		sourceType: navigator.camera.PictureSourceType.PHOTOLIBRARY
		})
});	
function onSuccess(imageURI){
    var Image = document.getElementById('getpicid');
    Image.value = imageURI;
}
function onFail(message){
    alert('Failed Because'+message);
}
$$('#add').on("click",function(e){
	var myname = document.getElementById('name').value;
	var element;
	var contact = navigator.contacts.create();
	if(myname!=""){
		contact.displayName = document.getElementById('name').value;
		contact.nickname = document.getElementById('name').value;
		var phoneNumbers = [];
		phoneNumbers[0] = new ContactField('work',document.getElementById('work').value , false);
		phoneNumbers[1] = new ContactField('mobile', document.getElementById('mobile').value, true);
		phoneNumbers[2] = new ContactField('home', document.getElementById('home').value, false);
		contact.phoneNumbers = phoneNumbers;
		var email=[];
		email[0] = new ContactField('email',document.getElementById('email').value , true); 
		contact.emails = email;
		var url=[];
		url[0] = new ContactField('work',document.getElementById('url').value); 
		contact.urls = url;
		var photos = [];
		element = document.getElementById('getpicid').value;
		if(element!=""){
			photos[0] = new ContactField('url', document.getElementById('getpicid').value ,true);
			contact.photos = photos; 
		}
		var mydate = new Date(document.getElementById('birthday').value); 
		contact.birthday = mydate;
		// save to device
		contact.save(onSuccessdo,onError);
	}
	else
	{
		alert("Please enter a name");
	}
	
});	

function onSuccessdo(contact){
	alert("success");
}
function onError(contacterror){
	alert("fail");
}
