package com.smiledon.library.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.smiledon.library.R;


public class PublicDialog {


	private String left_tx;
	private String right_tx;
	private OnClickListener left_clickListener;
	private OnClickListener right_clickListener;


	private Context mContext;
	public Dialog dialog;
	public TextView title_tv;
	public TextView content;
	public TextView left_tv;
	public View left_right_line;
	public TextView right_tv;

	/**
	 *  only one right button 
	 * @param mContext
	 * @param right_tx
	 * @param right_clickListener
	 */
	public PublicDialog(Context mContext, String right_tx, OnClickListener right_clickListener) {
		this.mContext = mContext;
		this.right_tx = right_tx;
		this.right_clickListener = right_clickListener;
		initDialog();
	}

	/**
	 *  two button. left button is click dismiss
	 * @param mContext
	 * @param left_tx
	 * @param right_tx
	 * @param right_clickListener
	 */
	public PublicDialog(Context mContext, String left_tx, String right_tx, OnClickListener right_clickListener) {
		this.mContext = mContext;
		this.left_tx = left_tx;
		this.right_tx = right_tx;
		this.right_clickListener = right_clickListener;
		initDialog();
	}


	/**
	 *  two button. define by yourself
	 * @param mContext
	 * @param left_tx
	 * @param right_tx
	 * @param right_clickListener
	 */
	public PublicDialog(Context mContext, String left_tx, OnClickListener left_clickListener, String right_tx, OnClickListener right_clickListener) {
		this.mContext = mContext;
		this.left_tx = left_tx;
		this.right_tx = right_tx;
		this.left_clickListener = left_clickListener;
		this.right_clickListener = right_clickListener;
		initDialog();
	}

	private void initDialog() {
		dialog = new Dialog(mContext, R.style.loading_dialog);
		dialog.setContentView(R.layout.dialog_public_layout);
		title_tv = (TextView) dialog.findViewById(R.id.dialog_title);
		content = (TextView) dialog.findViewById(R.id.dialog_content);
		left_tv = (TextView) dialog.findViewById(R.id.dialog_left_tv);
		left_right_line = (View) dialog.findViewById(R.id.dialog_vertical_line);
		right_tv = (TextView) dialog.findViewById(R.id.dialog_right_tv);


		if(left_tx == null){
			left_tv.setVisibility(View.GONE);
			left_right_line.setVisibility(View.GONE);
		}

		setLeftTvTx(left_tx);
		setRightTvTx(right_tx);

		if(left_clickListener != null){
			left_tv.setOnClickListener(left_clickListener);
		}
		else{
			left_tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}
		right_tv.setOnClickListener(right_clickListener);
	}

	public void show(){
		if(dialog != null && !dialog.isShowing())
			dialog.show();
	}

	public void dismiss(){
		if(dialog != null && dialog.isShowing())
			dialog.dismiss();
	}

	public void setTitleTx(String title){
		title_tv.setText(title);
	}

	public void setTitleVisible(int visible){
		dialog.findViewById(R.id.dialog_title_ll).setVisibility(visible);
	}

	public void setContentTx(String contentTx){
		content.setText(contentTx);
	}

	public void setLeftTvTx(String left_tx){
		if(left_tx != null)
			left_tv.setText(left_tx);
	}

	public void setRightTvTx(String right_tx){
		if(right_tx != null)
			right_tv.setText(right_tx);
	}

	public TextView getContentView(){
		return content;
	}
	public TextView getLeftTv(){
		return left_tv;
	}
	public TextView getRightTv(){
		return right_tv;
	}

	/**
	 *  click back is can dismiss dialog?
	 * @param left_tvable
	 */
	public void setCancelable(boolean left_tvable){
		dialog.setCancelable(left_tvable);
	}

	public void setCanceledOnTouchOutside(boolean left_tvable){
		dialog.setCanceledOnTouchOutside(left_tvable);
	}

	public void setDialogCantDismiss(){
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
	}

	public void setType(int type){
		dialog.getWindow().setType(type);
	}

	public boolean isShowing(){
		return dialog.isShowing();
	}

}
