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
	private Object waitObject;

	public CLPreferencesMenu(ProgSettings newMenuList, ProgSettings preferences, Object waitObject) {
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
		this.waitObject = waitObject;
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

	private void changePodcastRate() {
		System.out.println ();
		System.out.println ("How often to update the podcast feeds?");
		System.out.println ("1. Hourly");
		System.out.println ("2. Every 2 Hours");
		System.out.println ("3. Every 3 Hours");
		System.out.println ("4. Every 6 Hours");
		System.out.println ("5. Every 12 Hours");
		System.out.println ("6. Daily");
		System.out.print ("Choice: ");
		/* Take user input.
		 * Make sure it is between 1 & 6
		 * If not leave PodcastRate as it's current value.
		 */
		
		String updateValue = input.getValidNumber(1,6);
		if (updateValue!=null){
			switch (Integer.parseInt(updateValue)){
				case 1:
					// 1 Hour
					updateValue="60";
					break;
				case 2:
					// 2 Hours
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
			// Wake up the main thread in Podsalinan to update the wait value
			settings.updateSetting("updateInterval",updateValue);
			synchronized (waitObject){
				waitObject.notify();
			}
		}
	}

	private void changeDefaultDirectory() {
		File newPath;
		System.out.println ();
		System.out.print ("Enter Default Directory: ");
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
	}

	private void changeNumDownloaders() {
		System.out.println ();
		System.out.print ("Enter Number of Simultaneous Downloads: ");
		/* Take user input.
		 * Make sure it is between 1 and 30
		 * If not, get the user to enter it again.
		 */
		String numDownloaders = input.getValidNumber(1,30);
		if (numDownloaders!=null)
			settings.updateSetting("maxDownloaders",numDownloaders);
	}
	
	private void changeAutoQueueNewEpisodes() {
		System.out.println ();
		System.out.print ("Do you want new episodes Automatically Queued to Download? (Y/N): ");
		String autoDownloadResponse = input.getStringInput();
		if (autoDownloadResponse.length()==1){
			switch (autoDownloadResponse.charAt(0)){
				case 'Y':
				case 'y':
					settings.updateSetting("autoQueue","true");
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
	}

	private void setDownloadSpeed() {
		System.out.println();
		System.out.println("Valid values: 0 (Means no limit); 25 (Means 25Kbps); 1M (Means 1 Mbps)");
		System.out.println("The value you set here is the total limit, which is shared evenly across downloaders");
		System.out.print ("Please enter the Download Speed Limit:");
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
				settings.addSetting("downloadLimit",Integer.toString(speed));
			}
		}
	}

	public void process(String userInput){
		
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
		}
		super.process(userInputInt);
	}
}
