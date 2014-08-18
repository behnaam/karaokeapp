package at.fhooe.mc.android.taskmanager;

public final class GetJsonTask extends AbstractTask 
{

	private String m_UrlPath;
	
	/**
	 * Async task that will download a JSON file depending on the type of query. It will return a string with the content of the JSON file.
	 * @param p_TaskName - name of task so that we know how to parse the received JSON
	 * @param p_UrlPath - URL to query.
	 * @param p_ProgressMessage - progress dialog message displayed during executing
	 */
	public GetJsonTask(int p_TaskName, String p_UrlPath, String p_ProgressMessage) 
	{
		// Keep reference to resources
		m_UrlPath = p_UrlPath;
	
		// Initialise initial pre-execute message
		setmProgressMessage(p_ProgressMessage);
		setTaskName(p_TaskName);
	}
	

	/* Separate Thread */
	@Override
	protected String doInBackground(String... p_UrlPath) 
	{	
		String _JsonFileContent = JsonParser.getJsonFile(m_UrlPath);
		return _JsonFileContent;
	}
	
}