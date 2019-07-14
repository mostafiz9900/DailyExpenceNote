package com.hk.dailyexpensenote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;


public class AdapterExpense extends RecyclerView.Adapter<AdapterExpense.ViewHolder> {
    private Context context;
    private List<Expense>expenseList;
    private DatabaseHelper helper;

    public AdapterExpense() {
    }

    public AdapterExpense(Context context, List<Expense> expenseList, DatabaseHelper helper) {
        this.context = context;
        this.expenseList = expenseList;
        this.helper = helper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_expense_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final Expense currentExpense=expenseList.get(i);
        viewHolder.expenseType.setText(currentExpense.getType());
        viewHolder.date.setText(currentExpense.getDate());
        viewHolder.amount.setText(""+currentExpense.getAmount());


        //each item click listner  and sho bottomsheet

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here bottom sheet are included

               // View detailssheet=LayoutInflater.from(context).inflate(R.layout.details_expense_sheet,null);


                //BottomSheetDialog sheetDialog=new BottomSheetDialog(context);   //this process also can be by dialogue
               // sheetDialog.setContentView(detailssheet);
                //sheetDialog.show();


              DetailExpenseSheet detailExpenseSheet=new DetailExpenseSheet(currentExpense.getId(),currentExpense.getDate(),currentExpense.getType(),currentExpense.getTime(),currentExpense.getAmount());
              detailExpenseSheet.show(((FragmentActivity)context).getSupportFragmentManager(),"Expense Details");

            }
        } );




        //popup Button for edit and update data
        viewHolder.popupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu menu=new PopupMenu(context,v);  //set popup menu custom process
                menu.getMenuInflater().inflate(R.menu.menu_setting,menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.deleteItem:
                                //delete confirmation
                                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                                builder.setMessage("Delete");
                                builder.setTitle("Are you sure to delete ?");
                                builder.setCancelable(false);
                                builder.setIcon(R.drawable.ic_delete_forever_black_24dp);

                                //when click yes
                                builder.setPositiveButton("Yes",new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        helper=new DatabaseHelper(context);
                                        helper.deleteData(currentExpense.getId());
                                        expenseList.remove(i);
                                        notifyDataSetChanged();
                                        dialog.cancel();
                                    }
                                });

                                //when click no
                                builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.create();
                                builder.show();




                                break;

                            case R.id.updateItem:

                                requestUpdate(currentExpense);

                                break;



                            default:
                        }
                        return false;
                    }
                });
                menu.show();       //finally need to call show function for display popup menu





            }
        });

    }

    private void requestUpdate(Expense currentExpense) {
        Intent intent=new Intent(context, UpdatExpenseActivity.class);
        intent.putExtra("type",currentExpense.getType());
        intent.putExtra("id",String.valueOf(currentExpense.getId()));    //since id is integer value
        intent.putExtra("date",currentExpense.getDate());
        intent.putExtra("time",currentExpense.getTime());
        intent.putExtra("amount", String.valueOf(currentExpense.getAmount()));
        intent.putExtra("id",String.valueOf(currentExpense.getId()));   //parse id with String field
        context.startActivity(intent);






    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView expenseType,amount,date;
        ImageView popupBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseType=itemView.findViewById(R.id.expensTypeTV);
            date=itemView.findViewById(R.id.expensDateTV);
            amount=itemView.findViewById(R.id.expenseAmountTV);
            popupBtn=itemView.findViewById(R.id.popupMenuBtn);
        }
    }
}
