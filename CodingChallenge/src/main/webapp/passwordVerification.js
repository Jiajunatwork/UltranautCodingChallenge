var specialCharArray = ['!', '@', '#', '$', '%', '^', '&', '*'];
function verifyPassword(){
    console.log("button clicked");
    var password = document.getElementById("psw").value;    
    console.log(`you entered ${password}`);
    //var message = iterationOnePasswordVerification(password);
    //var message = iterationTwoPasswordVerification(password);
    //var message = iterationThreePasswordVerification(password);
    var message = iterationFourPasswordVerification(password);
    displayVerificationMessage(message);
}

function displayVerificationMessage(message){
    var msgElement = document.getElementById("msg");
    msgElement.innerHTML = message;
    msgElement.style.visibility  = "visible";
}

/* Iteration 1
The tool should verify if a password meets the requirements for a strong password
• The password must be at least 8 characters in length
• The password must contain at least 1 letter
• The password must contain at least 1 number
*/
//Assumption: empty space is not a letter, but still chount as a charaster, so 1234_678 is 8 character long, but not a valid password
//returns "valid password" if password is valid, "invalid password" otherwise 
function iterationOnePasswordVerification(password){
    var msg = "invalid password";
    //making sure variable password is a string, because javascript does not check types
    if(typeof password == "string" || password instanceof String){
        var charArray = password.split("");
        var letterCount = 0;
        var numberCount = 0;
        for(var i=0;i<charArray.length;i++){
            if(charArray[i] != " " && isNaN(charArray[i]) == false){
                numberCount++;
            }
            var charCode = charArray[i].charCodeAt(0);
            if((charCode >= 65 && charCode < 91) || (charCode >= 97 && charCode < 123)){
                letterCount++;
            }
        }
        if(password.length >=8 && numberCount>=1 && letterCount>=1){
            msg = "valid password";
        }
    }
    return msg;
}
/*
Iteration 2:
Instead of just rejecting a password, the tool needs to indicate why the password is not acceptable.
*/
//Assumption: empty space is not a letter, but still chount as a charaster, so 1234_678 is 8 character long, but not a valid password
// returns "valid password" if pasword is valid, else returns why password is invalid
function iterationTwoPasswordVerification(password){
    var msg = "invalid password";
    //making sure variable password is a string, because javascript does not check types
    if(typeof password == "string" || password instanceof String){ 
        var charArray = password.split("");
        var letterCount = 0;
        var numberCount = 0;
        for(var i=0;i<charArray.length;i++){
            if(charArray[i] != " " && isNaN(charArray[i]) == false){
                numberCount++;
            }
            var charCode = charArray[i].charCodeAt(0);
            if((charCode >= 65 && charCode < 91) || (charCode >= 97 && charCode < 123)){
                letterCount++;
            }
        }
        if(password.length >=8 && numberCount>=1 && letterCount>=1){
            msg = "valid password";
        }else{ // password is invalid, check for reasons
            if(password.length < 8 ){
                msg = msg + ", password must be at least 8 characters long";
            }
            if(numberCount < 1){
                msg = msg + ", password must contains at least 1 number";
            }
            if(letterCount < 1){
                msg = msg + ", password must contains at least 1 letter";
            }
        }
    }else{
        msg = "invalid password, password is not a string";
    }
    return msg;
}

/*
Iteration 3
The tool must now take into account the type of user that a password is for. 
Admin users require a stronger password than regular users.

Passwords for Admin users must be at least 13 characters in length
Passwords for Admin users must contain a special character ('!', '@', '#', '$', '%', '^', '&', or '*')
The password requirements for regular users are unchanged
*/

//Assumption: empty space is not a letter, but still chount as a charaster, so 1234_678 is 8 character long, but not a valid password
//Assumption: admin password must contains one and only one special character
/* returns: "valid password" if one role is selected and password is valid for the selected role
   returns: "invalid password ...." with error message if one role is selected, but password is not valid for selected role

*/
function iterationThreePasswordVerification(password){
    var role = document.getElementById("roleSelection").value.toLowerCase().trim();
    var msg = "For "+role+` user, password "${password}" is a `;
    var result = "invalid password";
    if(role == "normal"){ // user selected normal role
        //because password requirement for iteration 3 is unchanged, using iterationTwoPasswordVerification() will do the job
        result = iterationTwoPasswordVerification(password); 
    }else{  // user selected admin role
        //making sure variable password is a string, because javascript does not check types
        if(typeof password == "string" || password instanceof String){ 
            var charArray = password.split("");
            var letterCount = 0;
            var numberCount = 0;
            var specialCharCount = 0;
            console.log("admin  "+password);
            for(var i=0;i<password.length;i++){
                var charIsSpecial = specialCharArray.includes(charArray[i]);
                if(charIsSpecial){
                    specialCharCount++;
                }
                if(charArray[i] != " " && isNaN(charArray[i]) == false){
                    numberCount++;
                }
                var charCode = charArray[i].charCodeAt(0);
                if((charCode >= 65 && charCode < 91) || (charCode >= 97 && charCode < 123)){
                    letterCount++;
                }
            }
            console.log(numberCount+"   "+letterCount+"   "+specialCharCount);
            //determine if password is valid:
            if(password.length >=13 && letterCount >=1 && numberCount >=1 && specialCharCount ==1){
                result = "valid password";
            }else{ //password is not valid, determine error
                if(password.length < 13){
                    result = result + ", password must be at least 13 characters";
                }
                if(letterCount < 1){
                    result = result + ", password must contains at least 1 letter";
                }
                if(numberCount < 1){
                    result = result + ", password must contains at least 1 number";
                }
                if(specialCharCount != 1){
                    result = result + `, password must contains exactly 1 special character`;
                }
            }
        }

    }
    msg = msg + result;
    return msg;
}



/*
Iteration 4
The password requirements for all users have become more strict:

Passwords for regular users must now be at least 10 characters in length
Passwords for Admin users must now include at least 3 special characters
*/

//Assumption: empty space is not a letter, but still chount as a charaster, so 1234_678 is 8 character long, but not a valid password
//Assumption: admin password must contains one and only one special character

/*
returns "valid password" if password is valid for the selected role
returns "invalid password ...." if password is invlaid for the selected role
*/
function iterationFourPasswordVerification(password){
    var role = document.getElementById("roleSelection").value.toLowerCase().trim();
    var msg = "For "+role+` user, password "${password}" is a `;
    var result = "invalid password"
     //making sure variable password is a string, because javascript does not check types
    if(typeof password == "string" || password instanceof String){ 
        var letterCount = 0;
        var numberCount = 0;
        var specialCharCount = 0;
        var charArray = password.split("");
        for(var i=0;i<password.length;i++){
            if(charArray[i]!=" "){
                if(isNaN(charArray[i]) == false){ //char at index i is number
                    numberCount++;
                }else{
                    if(specialCharArray.includes(charArray[i])){ //char at index i is special char
                        specialCharCount++;
                    }else{ // char at index i is letter
                        letterCount++;
                    }
                }
                
            }
        }
        if(role == "admin"){ //user role is admin
            if(password.length >= 13 && letterCount >= 1 && numberCount >= 1 && specialCharCount >=3){
                //valid password for admin
                result = "valid password";
            }else{
                if(password.length < 13){ // admin password must be at least 13 characters
                    result = result + ", password must be at least 13 characters long";
                }
                if(specialCharCount < 3){ // admin password must contains at least 3 special char
                    result = result + ", password must contains at least 3 special characters";
                }
            }
        }else{  // user role is normal
            if(password.length >= 10 && letterCount >= 1 && numberCount >= 1){//valid password for normal
                result = "valid password";
            }else{
                if(password.length < 10){ //normal password must be at least 10 characters
                    result = result + ", password must be at least 10 characters long"
                }
            }
        }
        if(letterCount < 1){ // password must contains at least one letter
            result = result + ", password must contains at least 1 letter";
        }
        if(numberCount < 1){ // password must contains at least one number
            result = result + ", password must contains at least 1 number";
        }
    }
    msg = msg + result;
    return msg;
}