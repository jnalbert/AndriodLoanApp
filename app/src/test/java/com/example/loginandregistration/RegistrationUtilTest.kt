package com.example.loginandregistration

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RegistrationUtilTest {
    // methodName_someCondition_expectedResult
    @Test
    fun validatePassword_emptyPassword_isFalse() {
        val actual = RegistrationUtil().validatePassword("", "")
        assertThat(actual).isFalse()
    }

    @Test
    fun validatePassword_passwordsDontMatch_isFalse() {
        val actual = RegistrationUtil().validatePassword("ThisIsAtest5", "ThisIsATest6")
        assertThat(actual).isFalse()
    }

    @Test
    fun validatePassword_passwordNot8Chars_isFalse() {
        val actual = RegistrationUtil().validatePassword("Not8", "Not8")
        assertThat(actual).isFalse()
    }

    @Test
    fun validatePassword_noCapital_isFalse() {
        val actual = RegistrationUtil().validatePassword("thereisnocap", "thereisnocap")
    }

    @Test
    fun validatePassword_noNumber_isFalse() {
        val actual = RegistrationUtil().validatePassword("ThereIsNo", "ThereIsNo")
        assertThat(actual).isFalse()
    }

    @Test
    fun validatePassword_validPassword_isTrue() {
        val actual = RegistrationUtil().validatePassword("ThisIsAtest5", "ThisIsAtest5")
        assertThat(actual).isTrue()
    }

    // username validation
    @Test
    fun validateUsername_emptyUsername_isFalse() {
        val actual = RegistrationUtil().validateUsername("")
        assertThat(actual).isFalse()
    }

    @Test
    fun validateUsername_usernameTooShort_isFalse() {
        val actual = RegistrationUtil().validateUsername("a")
        assertThat(actual).isFalse()
    }

    @Test
    fun validateUsername_usernameTaken_isFalse() {
        val actual = RegistrationUtil().validateUsername("cosmicF")
        assertThat(actual).isFalse()
    }

    @Test
    fun validateUsername_validUsername_isTrue() {
        val actual = RegistrationUtil().validateUsername("cosmic")
        assertThat(actual).isTrue()
    }

    // validate Name
    @Test
    fun validateName_emptyName_isFalse() {
        val actual = RegistrationUtil().validateName("")
        assertThat(actual).isFalse()
    }

    @Test
    fun validateName_validName_isTrue() {
        val actual = RegistrationUtil().validateName("Cosmic")
        assertThat(actual).isTrue()
    }

    // validate email
    @Test
    fun validateEmail_emptyEmail_isFalse() {
        val actual = RegistrationUtil().validateEmail("")
        assertThat(actual).isFalse()
    }

    @Test
    fun validateEmail_emailTaken_isFalse() {
        val actual = RegistrationUtil().validateEmail("beanstock@hotmail.com")
        assertThat(actual).isFalse()
    }

    @Test
    fun validateEmail_invalidEmail_isFalse() {
        val actualNoAt = RegistrationUtil().validateEmail("beanstockhotmail.com")
        val actualNoDot = RegistrationUtil().validateEmail("beanstock@hotmailcom")

        assertThat(actualNoAt).isFalse()
        assertThat(actualNoDot).isFalse()
    }

    @Test
    fun validateEmail_validEmail_isTrue() {
        val actual = RegistrationUtil().validateEmail("jnalbert879@gmail.com")
        assertThat(actual).isTrue()
    }
}