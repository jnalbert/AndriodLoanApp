package com.example.loginandregistration

class RegistrationUtil {
    // use this in the test class for the is username taken test
    // make another similar list for some taken emails
    var existingUsers = listOf("cosmicF", "cosmicY", "bob", "alice")
    //    you can use listOf<type>() instead of making the list & adding individually
    //    List<String> blah = new ArrayList<String>();
    //    blah.add("hi")
    //    blah.add("hello")
    // make a list of existing fake emails
    var existingEmails = listOf("beanstock@hotmail.com", "bigbear79@gmail.com")

    // isn't empty
    // already taken
    // minimum number of characters is 3
    fun validateUsername(username: String) : Boolean {
        return username.isNotBlank()
                && username.length >= 3
                && !existingUsers.contains(username)
    }

    // make sure meets security requirements (deprecated ones that are still used everywhere)
    // min length 8 chars
    // at least one digit
    // at least one capital letter
    // both passwords match
    // not empty
    fun validatePassword(password : String, confirmPassword: String) : Boolean {
        return password.isNotEmpty()
                && password == confirmPassword
                && password.matches(Regex(".*\\d+.*")) // regx to check for digits
                && password.matches(Regex(".*[A-Z]+.*")) // regx to check for Capitals
                && password.length >= 8
    }

    // isn't empty
    fun validateName(name: String) : Boolean {
        return name.isNotBlank()
    }

    // isn't empty
    // make sure the email isn't used
    // make sure it's in the proper email format user@domain.tld
    fun validateEmail(email: String) : Boolean {
        return email.isNotBlank()
                && email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")) // regx to check for email format
                && !existingEmails.contains(email)
    }
}