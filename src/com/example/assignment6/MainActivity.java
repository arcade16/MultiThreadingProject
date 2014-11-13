package com.example.assignment6;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.app.Activity;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	
	private TextView introText;
	private TextView findText; 
	private TextView resultText;
	
	private boolean running = false;
	private boolean isMagic = false;
	
	private ProgressBar progressBar;
	
	
	Handler messagePassing = new Handler()
	{
		
		public void handleMessage(Message message)
		{
			
			int magicNumber = message.what;
			int lastDigit = lastDigit(magicNumber);
			
			//Determine if number is magic
			if (((magicNumber % 7 == 0) || ((magicNumber % 4 ==0) && (lastDigit == 2))) && (isMagic == false))
			{
				
				//Tell user magic number
				findText.setText("Magic number found");
				resultText.setText("The value of the magic number is: " + magicNumber);
				
				running = false;
				isMagic = true;
				
				progressBar.setVisibility(View.INVISIBLE);
				
			} else if (isMagic == false){
				running = true;
			}

	}};
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	//Create menu
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        introText = (TextView)findViewById(R.id.intro);
        findText = (TextView)findViewById(R.id.finding);
        resultText = (TextView)findViewById(R.id.result);
        
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        
    }
    
    @Override
    public void onStart()
    {
    	super.onStart();
    	
    	//Create threads
    	Thread firstThread = new Thread(magic);		
    	Thread secondThread = new Thread(magic);
    	
    	running = true;	
    	
    	//Start Threads
    	firstThread.start();						
    	secondThread.start();
    }
    
    @Override
    public void onStop()
    {
    	super.onStop();
    	
    	running = false;					
    }
    
   Runnable magic = new Runnable()
   {
    	//Generate numbers
    	public void run()
    	{
    		try
    		{
    			while(running)
    			{
    				Thread.sleep(1000);	
    				
    				int random = generateRandomNumber();	
    				
    				Log.i(Thread.currentThread().getName(), random+"");	
    				Message msg = messagePassing.obtainMessage(random);	
    				
    				messagePassing.sendMessage(msg);									
    			}
    		}
    		catch (InterruptedException e)
    		{
    			
    			Log.i("Exception ", e.getMessage().toString());
    			
    		} finally {
    			
    			running = false;
    			
    		}
	}};
    
    public synchronized int generateRandomNumber()
    { 
    	boolean fourDigit = false;					
    	int randomInteger = 0;				
    	
    	while (fourDigit == false)
    	{		
    		
	    	double randomDouble = Math.random()*10000;	
	    	randomInteger = (int)randomDouble;
    	
			if(randomInteger > 1000)
			{
				fourDigit = true;
			}
    	
    	}
    
    	return randomInteger;
}
    
    public int lastDigit(int number)
    {	
    	int last = number % 10; 
    	return last;
    }
    
}
