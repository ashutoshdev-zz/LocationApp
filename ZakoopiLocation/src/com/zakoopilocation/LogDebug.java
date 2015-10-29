package com.zakoopilocation;

import android.util.Log;

public class LogDebug {

	/*
	 * check = ture if want to print message 
	 * or check = false if do not want to print message
	 */
	static boolean check = true;
	
	public LogDebug(){
		
		
	}
	
	public static void showMessage(String tag,String message){
		
		if(check == true){
			
			Log.e(tag,message);
		}
		
	}
}
