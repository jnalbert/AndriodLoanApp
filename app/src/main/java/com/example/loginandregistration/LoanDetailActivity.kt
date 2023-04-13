package com.example.loginandregistration

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.loginandregistration.databinding.ActivityLoanDetailBinding
import java.lang.Double
import java.util.*

class LoanDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoanDetailBinding
    private var isCreatingNewLoan: Boolean = false
    var loanIsEditable = false
    var cal = Calendar.getInstance()
    lateinit var loan : Loan

    companion object {
        val EXTRA_LOAN = "loan"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isCreatingNewLoan = intent.getBooleanExtra(LoanListActivity.CREATING_NEW_LOAN, false)
        if (isCreatingNewLoan) {
            loan = Loan()
            toggleEditable()
        } else {
            loan = intent.getParcelableExtra<Loan>(EXTRA_LOAN) ?: Loan()
        }

        binding.checkBoxLoanDetailIsFullyRepaid.isChecked = loan.isRepaid
        binding.editTextLoanDetailInitialLoan.setText(loan.amount.toString())
        binding.editTextLoanDetailBorrower.setText(loan.borrower)
        binding.editTextLoanDetailAmountRepaid.setText(loan.amountRepaid.toString())
        binding.textViewLoanDetailAmountStillOwed.text = String.format("Still Owed %.2f", loan.amount - loan.amountRepaid)

        binding.buttonLoanDetailSave.setOnClickListener {
            if (loan.ownerId.isBlank()) {
                loan.ownerId = intent.getStringExtra(LoanListActivity.USERID) ?: ""
            }
            updateLoanInBackendless()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_loan_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menu_item_loan_detail_edit -> {
                toggleEditable()
                true
            }
            R.id.menu_item_loan_detail_delete -> {
                deleteFromBackendless()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteFromBackendless() {
        Backendless.Data.of(Loan::class.java).remove( loan,
            object : AsyncCallback<Long?> {
                override fun handleResponse(response: Long?) {
                    // Person has been deleted. The response is the
                    // time in milliseconds when the object was deleted
                    Toast.makeText(this@LoanDetailActivity, "${loan.borrower} Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun handleFault(fault: BackendlessFault) {
                    Log.d("BirthdayDetail", "handleFault: ${fault.message}")
                }
            })
    }

    private fun updateLoanInBackendless() {

        Log.d("LoanDetailActivity", "updateLoanInBackendless: ${binding.checkBoxLoanDetailIsFullyRepaid.isChecked}")

        val loanToUpdate = Loan(
            borrower = binding.editTextLoanDetailBorrower.text.toString() ?: loan.borrower,
            purpose = loan.purpose,
            amount = binding.editTextLoanDetailInitialLoan.text.toString().toDoubleOrNull() ?: loan.amount,
            dateLent = loan.dateLent,
            amountRepaid = binding.editTextLoanDetailAmountRepaid.text.toString().toDoubleOrNull() ?: loan.amountRepaid,
            dateFullyRepaid = loan.dateFullyRepaid,
            isRepaid = binding.checkBoxLoanDetailIsFullyRepaid.isChecked ?: loan.isRepaid,
            ownerId = loan.ownerId,
            objectId = loan.objectId
        )
        Log.d("LoanDetailActivity", "${loanToUpdate.isRepaid}")
        Backendless.Data.of(Loan::class.java).save(loanToUpdate, object : AsyncCallback<Loan?> {
            override fun handleResponse(response: Loan?) {
                Log.d("LoanDetailActivity", "handleResponse: ${response?.borrower} updated")
                loan = loanToUpdate
                toggleEditable()
                binding.textViewLoanDetailAmountStillOwed.text = String.format("Still Owed %.2f", loan.amount - loan.amountRepaid)
                Toast.makeText(this@LoanDetailActivity, "${loan.borrower} Sucessfully Saved", Toast.LENGTH_SHORT).show()
                if (isCreatingNewLoan) {
                    finish()
                }
            }

            override fun handleFault(fault: BackendlessFault) {
                Log.d("LoanDetailActivity", "handleFault: ${fault.message}")
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        })
    }


    private fun toggleEditable() {
        if (loanIsEditable) {
            loanIsEditable = false
            binding.buttonLoanDetailSave.isEnabled = false
            binding.buttonLoanDetailSave.visibility = View.GONE
            binding.checkBoxLoanDetailIsFullyRepaid.isEnabled = false
            binding.editTextLoanDetailBorrower.inputType = InputType.TYPE_NULL
            binding.editTextLoanDetailBorrower.isEnabled = false
            binding.editTextLoanDetailAmountRepaid.inputType = InputType.TYPE_NULL
            binding.editTextLoanDetailAmountRepaid.isEnabled = false
            binding.editTextLoanDetailInitialLoan.inputType = InputType.TYPE_NULL
            binding.editTextLoanDetailInitialLoan.isEnabled = false
            binding.checkBoxLoanDetailIsFullyRepaid.isClickable = false

            // set everything back to what it was
            binding.checkBoxLoanDetailIsFullyRepaid.isChecked = loan.isRepaid
            binding.editTextLoanDetailBorrower.setText(loan.borrower)
            binding.editTextLoanDetailInitialLoan.setText(loan.amount.toString())
            binding.editTextLoanDetailAmountRepaid.setText(loan.amountRepaid.toString())
        } else {
            loanIsEditable = true
            binding.buttonLoanDetailSave.isEnabled = true
            binding.buttonLoanDetailSave.visibility = View.VISIBLE
            binding.checkBoxLoanDetailIsFullyRepaid.isEnabled = true
            binding.editTextLoanDetailBorrower.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            binding.editTextLoanDetailBorrower.isEnabled = true
            binding.editTextLoanDetailAmountRepaid.inputType = InputType.TYPE_NUMBER_VARIATION_NORMAL
            binding.editTextLoanDetailAmountRepaid.isEnabled = true
            binding.editTextLoanDetailInitialLoan.inputType = InputType.TYPE_NUMBER_VARIATION_NORMAL
            binding.editTextLoanDetailInitialLoan.isEnabled = true
            binding.checkBoxLoanDetailIsFullyRepaid.isClickable = true
        }
    }
}