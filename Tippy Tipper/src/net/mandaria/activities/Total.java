package net.mandaria.activities;

import net.mandaria.R;
import net.mandaria.TippyTipperApplication;
import net.mandaria.R.id;
import net.mandaria.R.layout;
import net.mandaria.R.menu;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Total extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total);
        
        TippyTipperApplication appState = ((TippyTipperApplication)this.getApplication());
        
        CalculateTip(appState.service.GetTipPercentageAsDouble());
        
        View btn_SplitBill = findViewById(R.id.btn_SplitBill);
        btn_SplitBill.setOnClickListener(new OnClickListener() 
        	{
            	public void onClick(View v) 
            	{
            		SplitBillWithDefaultNumberOfPeople();
            		
            		Intent i = new Intent(getBaseContext(), SplitBill.class);
            		startActivity(i);
            	}
            });
//        Drawable d_SplitBill = findViewById(R.id.btn_SplitBill).getBackground();
//        int green = Color.parseColor("#216C2A");
//        PorterDuffColorFilter filter_green = new PorterDuffColorFilter(green, PorterDuff.Mode.SRC_ATOP);
//        d_SplitBill.setColorFilter(filter_green);
		
        View btn_round_down = findViewById(R.id.btn_round_down);
        btn_round_down.setOnClickListener(new OnClickListener() 
        	{
            	public void onClick(View v) 
            	{
            		RoundDown();
            	}
            });
        
        View btn_round_up = findViewById(R.id.btn_round_up);
        btn_round_up.setOnClickListener(new OnClickListener() 
        	{
            	public void onClick(View v) 
            	{
            		RoundUp();
            	}
            });
		
		SeekBar seek_tip_percentage = (SeekBar)findViewById(R.id.seek_tip_percentage);
		seek_tip_percentage.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
			{	
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser)
				{
					if(fromUser)
					{
						CalculateTip((double)progress);
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar)
				{
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar)
				{
					// TODO Auto-generated method stub
					
				}
			});
		
    }
    
    /* Called after onCreate or onRestart */
	@Override
	public void onStart()
	{
		super.onStart();
		
		RefreshBillAmount();
		
		Button btn_TipAmount1 = (Button)findViewById(R.id.btn_TipAmount1);
        double tipPercentagePresetOne = (double)Settings.getTipPercentagePresetOne(getBaseContext());
        btn_TipAmount1.setText((int)tipPercentagePresetOne + "%");
        btn_TipAmount1.setOnClickListener(new OnClickListener() 
    	{
        	public void onClick(View v) 
        	{
        		double tipPercentagePresetOne = (double)Settings.getTipPercentagePresetOne(getBaseContext());
        		CalculateTip(tipPercentagePresetOne);
        	}
        });
        
        Button btn_TipAmount2 = (Button)findViewById(R.id.btn_TipAmount2);
        double tipPercentagePresetTwo = (double)Settings.getTipPercentagePresetTwo(getBaseContext());
        btn_TipAmount2.setText((int)tipPercentagePresetTwo + "%");
        btn_TipAmount2.setOnClickListener(new OnClickListener() 
    	{
        	public void onClick(View v) 
        	{
        		double tipPercentagePresetTwo = (double)Settings.getTipPercentagePresetTwo(getBaseContext());
        		CalculateTip(tipPercentagePresetTwo);
        	}
        });
        
        Button btn_TipAmount3 = (Button)findViewById(R.id.btn_TipAmount3);
        double tipPercentagePresetThree = (double)Settings.getTipPercentagePresetThree(getBaseContext());
        btn_TipAmount3.setText((int)tipPercentagePresetThree + "%");
        btn_TipAmount3.setOnClickListener(new OnClickListener() 
    	{
        	public void onClick(View v) 
        	{
        		double tipPercentagePresetThree = (double)Settings.getTipPercentagePresetThree(getBaseContext());
        		CalculateTip(tipPercentagePresetThree);
        	}
        });
	}
    
    @Override
  	public boolean onCreateOptionsMenu(Menu menu)
  	{
  		super.onCreateOptionsMenu(menu);
  		MenuInflater inflater = getMenuInflater();
  		inflater.inflate(R.menu.menu, menu);
  		return true;
  	}

  	@Override
  	public boolean onOptionsItemSelected(MenuItem item)
  	{
  		switch(item.getItemId())
  		{
  			case R.id.settings:
  				startActivity(new Intent(this, Settings.class));
  				return true;
  		}
  		return false;
  	}
  	
  	private void SplitBillWithDefaultNumberOfPeople()
  	{
  		TippyTipperApplication appState = ((TippyTipperApplication)this.getApplication());
  		int numberOfPeople = (int)Settings.getDefaultNumberOfPeopleToSplitBill(getBaseContext());
		
		appState.service.SplitBill(numberOfPeople);
  	}
    
    private void CalculateTip(Double percent)
    {
		TippyTipperApplication appState = ((TippyTipperApplication)this.getApplication());
		float excludeTaxRate = (float)Settings.getExcludeTaxRate(getBaseContext());
		
		//appState.service.CalculateTip(percent/100.0);
		appState.service.CalculateTip(percent/100.0, excludeTaxRate/100.0);

		BindData();
    }
    
    private void RoundDown()
    {
    	TippyTipperApplication appState = ((TippyTipperApplication)this.getApplication());
    	boolean roundTip = (boolean)Settings.isSetToRoundByTip(getBaseContext());
    	appState.service.RoundDown(roundTip);
    	
    	BindData();
    }
    
    private void RoundUp()
    {
    	TippyTipperApplication appState = ((TippyTipperApplication)this.getApplication());
    	boolean roundTip = (boolean)Settings.isSetToRoundByTip(getBaseContext());
    	appState.service.RoundUp(roundTip);
    	
    	BindData();
    }
    
    public void RefreshBillAmount()
    {
    	TippyTipperApplication appState = ((TippyTipperApplication)this.getApplication());
		double tipPercent = appState.service.GetTipPercentageAsDouble();
		float excludeTaxRate = (float)Settings.getExcludeTaxRate(getBaseContext());
		
		if(excludeTaxRate == 0)
		{
			appState.service.RefreshBillAmount();
			BindData();
		}
		else
			CalculateTip(tipPercent);
    }
    
    private void BindData()
    {
    	TippyTipperApplication appState = ((TippyTipperApplication)this.getApplication());
    	
    	TextView lbl_bill_amount = (TextView)findViewById(R.id.lbl_bill_amount);
		TextView lbl_tip_amount = (TextView)findViewById(R.id.lbl_tip_amount);
		TextView lbl_total_amount = (TextView)findViewById(R.id.lbl_total_amount);
		//TextView lbl_tip_percentage = (TextView)findViewById(R.id.lbl_tip_percentage);
		SeekBar seek_tip_percentage = (SeekBar)findViewById(R.id.seek_tip_percentage);
		View inflated_excludetax = findViewById(R.id.inflated_excludeTax);
		TextView lbl_tip_text = (TextView)findViewById(R.id.lbl_tip_text);
		
		float excludeTaxRate = (float)Settings.getExcludeTaxRate(getBaseContext());
		if(excludeTaxRate != 0)
		{
			ViewStub stub_excludeTax = (ViewStub)findViewById(R.id.stub_excludeTax);
			if(stub_excludeTax != null)
				stub_excludeTax.setVisibility((int)View.VISIBLE);
			else if(inflated_excludetax != null)
				inflated_excludetax.setVisibility((int)View.VISIBLE);
			TextView lbl_tax_amount = (TextView)findViewById(R.id.lbl_tax_amount);
			lbl_tax_amount.setText(appState.service.GetTaxAmount());
			
			// Add (tax%): to label
			TextView lbl_tax_text = (TextView)findViewById(R.id.lbl_tax_text);
			String tax_text = lbl_tax_text.getText().toString();
			if(!tax_text.contains(appState.service.GetTaxPercentage()))
				lbl_tax_text.setText(this.getString(R.string.tax).replace(":", " (" + appState.service.GetTaxPercentage() + "):"));
		}
		else
		{
			if(inflated_excludetax != null)
				inflated_excludetax.setVisibility((int)View.GONE);
		}

		// Add (tip%): to label
		String tip_text = lbl_tip_text.getText().toString();
		if(!tip_text.contains(appState.service.GetTipPercentage()))
			lbl_tip_text.setText(this.getString(R.string.tip).replace(":", " (" + appState.service.GetTipPercentage() + "):"));
		
		//lbl_tip_percentage.setText(appState.service.GetTipPercentage());
		lbl_bill_amount.setText(appState.service.GetBillAmount());
		lbl_tip_amount.setText(appState.service.GetTipAmount());
		lbl_total_amount.setText(appState.service.GetTotalAmount());
		seek_tip_percentage.setProgress(appState.service.GetTipPercentageRounded());
    }
}
