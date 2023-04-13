package com.example.loginandregistration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.example.loginandregistration.databinding.ActivityLoanListBinding

class LoanListActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
        const val USERID = "userId"
        const val CREATING_NEW_LOAN = "creatingNewLoan"
    }

    lateinit var binding: ActivityLoanListBinding
    lateinit var loanList: ArrayList<Loan>
    lateinit var loanAdapter: LoanAdapter
    lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.fabLoanListCreateNewLoan.setOnClickListener {
            val loanDetailIntent = Intent(this, LoanDetailActivity::class.java). apply {
                putExtra(USERID, userId)
                putExtra(CREATING_NEW_LOAN, true)
            }
            startActivity(loanDetailIntent)
        }

    }
    override fun onStart() {
        super.onStart()
        userId = intent.getStringExtra(USERID) ?: ""

        retrieveLoanData(userId)
    }

    private fun retrieveLoanData(userId: String) {

        val whereClause = "ownerId = '$userId'"
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.setWhereClause(whereClause)

        Backendless.Data.of(Loan::class.java).find(queryBuilder, object :
            AsyncCallback<List<Loan?>?> {
            override fun handleResponse(foundLoans: List<Loan?>?) {
                // every loaded object from the Loan table is now an individual java.util.Map\
                Log.d(LoginActivity.TAG, "handleResponse: ${foundLoans}")
                loanList = foundLoans as ArrayList<Loan>

                loanAdapter = LoanAdapter(loanList)
                // tell the cecylerview to use the adapter
                binding.recyclerViewLoanListLoans.adapter = loanAdapter
                // tell the adapter what kind of layout we want (linear or grind)
                binding.recyclerViewLoanListLoans.layoutManager = LinearLayoutManager(this@LoanListActivity)
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()\
                Log.d(LoginActivity.TAG, "handleFault: ${fault.message}")
            }
        })
    }
}