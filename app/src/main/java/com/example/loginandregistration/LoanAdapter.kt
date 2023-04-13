package com.example.loginandregistration

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault


class LoanAdapter(var dataSet: MutableList<Loan>) :
    RecyclerView.Adapter<LoanAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewBorrwerName: TextView
        val textViewTotalLoanAmount: TextView
        val textViewLoanAmountLeft: TextView
        val textViewDateLent: TextView
        val textViewPurpose: TextView
        val textViewIsRepaid: TextView
        val layout: ConstraintLayout

        init {
            textViewBorrwerName = view.findViewById(R.id.textView_laonItem_borrowerName)
            textViewTotalLoanAmount = view.findViewById(R.id.textView_loanList_totalLoan)
            textViewLoanAmountLeft = view.findViewById(R.id.textView_loanList_totalAmountLeft)
            textViewDateLent = view.findViewById(R.id.textView_loanItem_dateLentValue)
            textViewPurpose = view.findViewById(R.id.textView_loanItem_purposeValue)
            textViewIsRepaid = view.findViewById(R.id.textView_loanItem_isRepaid)
            layout = view.findViewById(R.id.layout_loanItem_layout)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_loan_data, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val loan = dataSet[position]
        val context = viewHolder.textViewBorrwerName.context
        viewHolder.textViewBorrwerName.text = loan.borrower
        viewHolder.textViewTotalLoanAmount.text = loan.amount.toString()
        viewHolder.textViewLoanAmountLeft.text = loan.getRemainingAmount().toString()
        // convert the date to mm/dd/yyyy format
        val formattedDate = "${loan.dateLent.month + 1}/${loan.dateLent.date}/${loan.dateLent.year + 1900}"
        viewHolder.textViewDateLent.text = formattedDate
        viewHolder.textViewPurpose.text = loan.purpose
        viewHolder.textViewIsRepaid.text = if (loan.isRepaid) "Yes" else "No"
        viewHolder.textViewIsRepaid.setTextColor(if (loan.isRepaid) Color.GREEN else Color.RED)

        viewHolder.layout.setOnClickListener {
            startLoanDetailActivity(it, loan)
        }

        viewHolder.layout.isLongClickable = true
        viewHolder.layout.setOnLongClickListener {
            val popMenu = PopupMenu(context, viewHolder.textViewBorrwerName)
            popMenu.inflate(R.menu.menu_loan_list_context)
            popMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.menu_loanlist_delete -> {
                        deleteFromBackendless(position)
                        true
                    }
                    else -> true
                }
            }
            popMenu.show()
            true
        }
    }

    private fun startLoanDetailActivity(view: View, loan: Loan) {
        val intent = Intent(view.context, LoanDetailActivity::class.java)
        intent.putExtra(LoanDetailActivity.EXTRA_LOAN, loan)
        view.context.startActivity(intent)
    }

    private fun deleteFromBackendless(position: Int) {
        val contactToDelete = dataSet.get(position)
        Log.d("LoanAdapter", "deleteFromBackendless: Trying to delete ${dataSet[position]}")
        // put in the code to delete the item using the callback from Backendless
        // in the handleResponse, we'll need to also delete the item from the loanList
        // and make sure that the recyclerview is updated
        Backendless.Data.of(Loan::class.java).remove(contactToDelete, object : AsyncCallback<Long?> {
            override fun handleResponse(response: Long?) {
                // Contact objectdhas been deleted
                dataSet.removeAt(position)
                notifyItemRemoved(position)
            }

            override fun handleFault(fault: BackendlessFault) {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Log.d("LoanAdapter", "handleFault: ${fault.message}")
            }
        })
    }


    override fun getItemCount() = dataSet.size
}