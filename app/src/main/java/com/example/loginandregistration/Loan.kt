package com.example.loginandregistration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Loan(
    var borrower: String = "borrower",
    var purpose: String = "purpose",
    var amount: Double = 99999.99,
    var dateLent: Date = Date(0),
    var amountRepaid: Double = 99.99,
    var dateFullyRepaid: Date? = null,
    var isRepaid: Boolean = false,
    var ownerId: String = "",
    var objectId: String? = null,
): Parcelable {
    fun getRemainingAmount(): Double {
        return amount - amountRepaid
    }
}
