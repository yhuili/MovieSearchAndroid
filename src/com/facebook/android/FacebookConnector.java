package com.facebook.android;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;

public class FacebookConnector
{
	// borrow the code from Facebook example: LoginButton.java
	private Facebook facebook = null;
	private AsyncFacebookRunner asyncRunner;
	private Context context;
	private String[] permissions;
	private Handler mHandler;
	private Activity activity;
	private SessionListener mSessionListener = new SessionListener();;

	public FacebookConnector(String appId,Activity activity,Context context,String[] permissions) 
	{
		this.facebook = new Facebook(appId);
		this.asyncRunner = new AsyncFacebookRunner(facebook);
		
		SessionStore.restore(facebook, context);
        SessionEvents.addAuthListener(mSessionListener);
        SessionEvents.addLogoutListener(mSessionListener);
        
		this.context = context;
		this.permissions = permissions;
		this.mHandler = new Handler();
		this.activity = activity;
	}

	public void login() 
	{
        if (!facebook.isSessionValid()) 
        {
            facebook.authorize(this.activity, this.permissions, new LoginDialogListener());
        }
    }

	public void logout() 
	{
        SessionEvents.onLogoutBegin();
        //AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(this.facebook);
        asyncRunner.logout(this.context, new LogoutRequestListener());
	}

    private final class LoginDialogListener implements DialogListener 
    {
        public void onComplete(Bundle values) 
        {
            SessionEvents.onLoginSuccess();
        }

        public void onFacebookError(FacebookError error) 
        {
            SessionEvents.onLoginError(error.getMessage());
        }
        
        public void onError(DialogError error) 
        {
            SessionEvents.onLoginError(error.getMessage());
        }

        public void onCancel() 
        {
            SessionEvents.onLoginError("Action Canceled");
        }
    }
    
    public class LogoutRequestListener extends BaseRequestListener 
    {
        public void onComplete(String response, final Object state) 
        {
            // callback should be run in the original thread, 
            // not the background thread
            mHandler.post(new Runnable() 
            {
                public void run() 
                {
                    SessionEvents.onLogoutFinish();
                }
            });
        }
    }
    
    private class SessionListener implements AuthListener, LogoutListener 
    {
        
        public void onAuthSucceed() 
        {
            SessionStore.save(facebook, context);
        }

        public void onAuthFail(String error) {
        }
        
        public void onLogoutBegin() {           
        }
        
        public void onLogoutFinish() 
        {
            SessionStore.clear(context);
        }
    }

	public Facebook getFacebook() 
	{
		return this.facebook;
	}
	
	public AsyncFacebookRunner getAsyncRunner() 
	{
		return this.asyncRunner;
	}
}
