/**
 * 
 */
package podsalinan;

import java.io.File;


/**
 * @author bugman
 *
 */
public class CLPreferencesMenu extends CLMenu{
	private ProgSettings settings;
	private CLInput input;

	public CLPreferencesMenu(ProgSettings newMenuList, ProgSettings preferences) {
		super(newMenuList,"preferences");
		String[] mainMenuList = {"1. Change Podcast Update Rate",
								 "2. Number of Downloaders",
								 "3. Default Download Directory",
								 "4. Automatically Download New Podcast Episodes",
								 "5. Set Download Speed Limit",
								 "",
								 "9. Return to Preferences Menu"};
		setMainMenuList(mainMenuList);
		setSettings(preferences);
		input = new CLInput();
	}

	@Override
	public void printMainMenu() {
		super.printMainMenu();
		
	}

	public ProgSettings getSettings() {
		return settings;
	}

	public void setSettings(ProgSettings settings) {
		this.settings = settings;
	}

	private String printUserFriendlyUpdateRate(){
    	switch (Integer.parseInt(settings.findSetting("updateInterval").value)){
		    case 60:
			    return "Hourly";
		    case 120:
		    	return "2 Hours";
		    case 180:
		    	return "3 Hours";
		    case 360:
		    	return "6 Hours";
		    case 720:
		    	return "12 Hours";
		    case 1440:
		    	return "Daily";
		    default:
		    	return null;
    	}

	}
	
	private void changePodcastRate() {
		System.out.println ();
		System.out.println ("How often to update the podcast feeds?");
		System.out.println ("1. Hourly");
		System.out.println ("2. Every 2 Hours");
		System.out.println ("3. Every 3 Hours");
		System.out.println ("4. Every 6 Hours");
		System.out.println ("5. Every 12 Hours");
		System.out.println ("6. Daily");
		Setting podcastRateSetting = settings.findSetting("updateInterval"); 
        if (podcastRateSetting==null)
        	settings.addSetting("updateInterval", "1440");
        
        System.out.print ("Choice ["+printUserFriendlyUpdateRate()+"]: ");
		/* Take user input.
		 * Make sure it is between 1 & 6
		 * If not leave PodcastRate as it's current value.
		 */
		
		String updateValue = input.getValidNumber(1,6);
		if (updateValue!=null){
			if (updateValue.length()>0){
				switch (Integer.parseInt(updateValue)){
					case 1:
						// 1 Hour
						updateValue="60";
						break;
					case 2:
						//2 Hours
						updateValue="120";
						break;
					case 3:
						// 3 Hours
						updateValue="180";
						break;
					case 4:
						// 6 Hours
						updateValue="360";
						break;
					case 5:
						// 12 Hours
						updateValue="720";
						break;
					case 6:
						// 24 Hours
						updateValue="1440";
						break;
				}
				settings.updateSetting("updateInterval",updateValue);
				System.out.println("Update Interval now set to:"+settings.findSetting("updateInterval").value);
				// Wake up the main thread in Podsalinan to update the wait value
				
				synchronized (settings.getWaitObject()){
					settings.getWaitObject().notify();
				}
			}
		}
	}

	private void changeDefaultDirectory() {
		File newPath;
		System.out.println ();
		System.out.print ("Enter Default Directory["+settings.findSetting("defaultDirectory").value+"]: ");
		/* Take user input.
		 * Create File object to test if directory exists.
		 * If directory exists set defaultDirectory to file Input
		 * If not show and error and leave defaultDirectory as is
		 */
		String userInput=input.getStringInput();
		if ((userInput.length()>0)&&(userInput!=null)){
			newPath=new File(userInput);
			if ((newPath.exists())&&(newPath.isDirectory())){
				settings.updateSetting("defaultDirectory",userInput);
			} else {
				System.out.println ("Error: User Input invalid");
			}
		}
		System.out.println("Default Directory: "+settings.findSetting("defaultDirectory").value);
	}

	private void changeNumDownloaders() {
		System.out.println ();
		System.out.print ("Enter Number of Simultaneous Downloads["+settings.findSetting("maxDownloaders").value+"]: ");
		/* Take user input.
		 * Make sure it is between 1 and 30
		 * If not, get the user to enter it again.
		 */
		String numDownloaders = input.getValidNumber(1,30);
		if (numDownloaders!=null)
			settings.updateSetting("maxDownloaders",numDownloaders);
		System.out.println("Simultaneous Downloads: "+settings.findSetting("maxDownloaders").value);
	}
	
	private void changeAutoQueueNewEpisodes() {
		System.out.println ();
		Setting autoQueueSetting = settings.findSetting("autoQueue");
		if (autoQueueSetting==null)
			settings.addSetting("autoQueue", "false");
		System.out.print ("Do you want new episodes Automatically Queued to Download? (Y/N) ["+
				          settings.findSetting("autoQueue").value+"]: ");
		String autoDownloadResponse = input.getStringInput();
		if (autoDownloadResponse.length()==1){
			switch (autoDownloadResponse.charAt(0)){
				case 'Y':
				case 'y':
					settings.updateSetting("autoQueue","true");
					settings.getWaitObject().notify();
					break;
				case 'N':
				case 'n':
					settings.updateSetting("autoQueue","false");
					break;
				default:
					System.err.println ("Error: User entered Value is invalid. No change made");
					break;
			}
		} else if (autoDownloadResponse.length()>1) {
			if (autoDownloadResponse.equalsIgnoreCase("yes"))
				settings.updateSetting("autoQueue","true");
			else if (autoDownloadResponse.equalsIgnoreCase("no"))
				settings.updateSetting("autoQueue","false");
			else if (autoDownloadResponse.equalsIgnoreCase("no"))
				System.err.println ("Error: User entered Value is invalid. No change made");
		}
		System.out.println("Auto Queue Downloads: "+settings.findSetting("autoQueue").value);

	}

	private void setDownloadSpeed() {
		System.out.println();
		System.out.println("Valid values: 0 (Means no limit); 25 (Means 25Kbps); 1M (Means 1 Mbps)");
		System.out.println("The value you set here is the total limit, which is shared evenly across downloaders");
		if (settings.findSetting("downloadLimit")==null)
			settings.addSetting("downloadLimit", "0");
		System.out.print ("Please enter the Download Speed Limit["+settings.findSetting("downloadLimit").value+"]:");
		String userInput = input.getStringInput();
		int speed=-1;
		if (userInput.length()>0){
			try {
				speed = Integer.parseInt(userInput);
			} catch (NumberFormatException e){
				speed= -1;
			}
			if (speed<0){
				if ((userInput.toUpperCase().endsWith("M"))||
					(userInput.toUpperCase().endsWith("MBPS"))||
					(userInput.toUpperCase().endsWith("MB"))){
					String speedString=userInput.split("M")[0];
					try{
						speed = (Integer.parseInt(speedString)*1024);
					} catch (NumberFormatException e){
						speed = -1;
					}
				}
			}
			if (speed<0){
				System.err.println("Invalid Speed. Download Speed Limit unchanged");
			} else {
				settings.updateSetting("downloadLimit",Integer.toString(speed));
			}
		}
		System.out.println("Max Download Speed: "+settings.findSetting("downloadLimit").value);
	}

	public void process(String userInput){
		super.process(userInput);
	}
	
	public void process(int userInputInt) {
		switch (userInputInt){
			case 1:
				changePodcastRate();
				break;
			case 2:
				changeNumDownloaders();
				break;
			case 3:
				changeDefaultDirectory();
				break;
			case 4:
				changeAutoQueueNewEpisodes();
				break;
			case 5:
				setDownloadSpeed();
				break;
			case 9:
				menuList.clear();
				break;
			case 99:
				printMainMenu();
		}
		userInputInt=-1000;
		super.process(userInputInt);
	}
	
	public boolean printList(){
		for (Setting setting : settings.getArray()){
			
			if (setting.name.equalsIgnoreCase("updateInterval")){
				System.out.println(setting.name+" = "+printUserFriendlyUpdateRate());
			} else if (setting.name.equalsIgnoreCase("downloadLimit")){
				System.out.println(setting.name+" = "+setting.value+"Kbps");
			} else
				System.out.println(setting.name+" = "+setting.value);
		}
		
		return true;
	}
}
