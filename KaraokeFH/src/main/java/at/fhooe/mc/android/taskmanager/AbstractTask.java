package at.fhooe.mc.android.taskmanager;

import android.os.AsyncTask;

public abstract class AbstractTask extends AsyncTask<String, String, String>
{
	private String mResult;
	private String mProgressMessage;
	private IProgressTracker mProgressTracker;
	private int TaskName = -1;

	/* UI Thread */
	public void setProgressTracker(IProgressTracker progressTracker) 
	{
		// Attach to progress tracker
		mProgressTracker = progressTracker;
		// Initialise progress tracker with current task state
		if (mProgressTracker != null) 
		{
			if(mProgressMessage != null)
				mProgressTracker.onProgress(mProgressMessage);
			
			if (mResult != null) 
			{
				mProgressTracker.onComplete();
			}
		}
	}
	
	/* UI Thread */
	@Override
	protected void onCancelled() {
		// Detach from progress tracker
		mProgressTracker = null;
	}

	/* UI Thread */
	@Override
	protected void onProgressUpdate(String... values) {
		// Update progress message 
		mProgressMessage = values[0];
		
		// And send it to progress tracker
		if (mProgressTracker != null && mProgressMessage != null) 
		{
			mProgressTracker.onProgress(mProgressMessage);
		}
	}
	
	/* UI Thread */
	@Override
	protected void onPostExecute(String result) {
		// Update result
		mResult = result;
		// And send it to progress tracker
		if (mProgressTracker != null) {
			mProgressTracker.onComplete();
		}
		// Detach from progress tracker
		mProgressTracker = null;
	}
	
	public String getResult(){
		return mResult;
	}
	
	@Override
	protected abstract String doInBackground(String... p_UrlPath);
	
	/**
	 * @return the mResult
	 */
	public String getmResult() {
		return mResult;
	}

	/**
	 * @param mResult the mResult to set
	 */
	public void setmResult(String mResult) {
		this.mResult = mResult;
	}

	/**
	 * @return the mProgressMessage
	 */
	public String getmProgressMessage() {
		return mProgressMessage;
	}

	/**
	 * @param mProgressMessage the mProgressMessage to set
	 */
	public void setmProgressMessage(String mProgressMessage) {
		this.mProgressMessage = mProgressMessage;
	}

	/**
	 * @return the mProgressTracker
	 */
	public IProgressTracker getmProgressTracker() {
		return mProgressTracker;
	}

	/**
	 * @param mProgressTracker the mProgressTracker to set
	 */
	public void setmProgressTracker(IProgressTracker mProgressTracker) {
		this.mProgressTracker = mProgressTracker;
	}
	
	/**
	 * @return the TaskName
	 */
	public int getTaskName() {
		return TaskName;
	}

	/**
	 * @param TaskName the TaskName to set
	 */
	public void setTaskName(int TaskName) {
		this.TaskName = TaskName;
	}

}
