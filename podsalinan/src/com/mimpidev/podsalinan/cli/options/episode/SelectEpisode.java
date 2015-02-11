/**
 * 
 */
package com.mimpidev.podsalinan.cli.options.episode;

import com.mimpidev.podsalinan.DataStorage;
import com.mimpidev.podsalinan.Podsalinan;
import com.mimpidev.podsalinan.cli.CLIOption;
import com.mimpidev.podsalinan.cli.ReturnCall;

/**
 * @author sbell
 *
 */
public class SelectEpisode extends CLIOption {

	public SelectEpisode(DataStorage newData) {
		super(newData);
		ShowSelectedMenu showMenu = new ShowSelectedMenu(newData);
		options.put("", showMenu);
		options.put("showmenu", showMenu);
		options.put("1", new DownloadEpisode(data));
		options.put("2", new DeleteEpisodeFromDrive(data));
		options.put("3", new CancelDownload(data));
		options.put("4", new ChangeStatus(data));
	}

	@Override
	public ReturnCall execute(String command) {
		if (debug) Podsalinan.debugLog.logInfo(this.getClass().getName()+":"+command);
		String[] commandOptions = command.split(" ");
		if (commandOptions.length>1){
			returnObject = options.get("showmenu").execute(command);
		}
		
		return returnObject;
	}

}
