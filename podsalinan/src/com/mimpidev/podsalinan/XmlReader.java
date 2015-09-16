/*******************************************************************************
 * Copyright (c) 2011 Sam Bell.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or  any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Sam Bell - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package com.mimpidev.podsalinan;

import java.io.InputStream;
import java.util.Vector;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.mimpidev.podsalinan.data.Episode;

/**
 * @author bugman
 *
 */
public class XmlReader {
	private String title;
	private String atomLink=null;
	
	public XmlReader(){
	}
	
	public Vector<Episode> parseEpisodes(InputStream file){
		Vector<Episode> episodes = new Vector<Episode>();
		XMLInputFactory factory = XMLInputFactory.newInstance();
		String itunes="",
			   media="";
		Episode newEpisode=null;
		
		factory.setProperty(XMLInputFactory.IS_COALESCING, true);
		try {
			XMLStreamReader r = factory.createXMLStreamReader(file);
			int event = r.getEventType();
			while (true){
				switch (event){
					case XMLStreamConstants.START_DOCUMENT:
						
						break;
					case XMLStreamConstants.START_ELEMENT:
						// If we hit the rss element we need to identify the namespace for itunes and media
						if (r.getName().toString().equalsIgnoreCase("rss"))
							for (int count=0; count < r.getNamespaceCount(); count++){
								if (r.getNamespacePrefix(count).equalsIgnoreCase("itunes"))
									itunes=r.getNamespaceURI(count);
								else if (r.getNamespacePrefix(count).equalsIgnoreCase("media"))
									media=r.getNamespaceURI(count);
							}
							
						// On finding a new item create a new episode object
						if (r.getName().toString().equalsIgnoreCase("item")){
							newEpisode = new Episode();
						}
						// If we find title, but it is not the title inside of an item it will be the podcast
						// title, and we will store this temporarily.
						if (r.getName().toString().equalsIgnoreCase("title")){
							if (newEpisode==null)
								title=r.getElementText();
							else
								newEpisode.setTitle(r.getElementText());
						}
						// If we find the published date, inside of an item set the date for the episode object 
						if (r.getName().toString().equalsIgnoreCase("pubDate")){
							if (newEpisode!=null)
								newEpisode.setDate(r.getElementText());
						}

						// If we find the published date, inside of an item set the date for the episode object 
						if (r.getName().toString().equalsIgnoreCase("description")){
							if (newEpisode!=null)
								// The replaceAll regExp is used to remove HTML from the description, as some
								// descriptions have html in it. trim() is used to clean whitespace from the description
								newEpisode.setDescription(r.getElementText().replaceAll("\\<.*?>", "").trim());
						}
						// If we find a enclosure tag grab the url and length then add the episode to the
						// array of episodes to be sent to the calling method.
						if (r.getName().toString().equalsIgnoreCase("enclosure")){
							if (newEpisode!=null){
								for (int count=0; count < r.getAttributeCount(); count++)
									if (r.getAttributeName(count).toString().equalsIgnoreCase("url"))
										newEpisode.setURL(r.getAttributeValue(count));
									else if (r.getAttributeName(count).toString().equalsIgnoreCase("length"))
										newEpisode.setSize(r.getAttributeValue(count));
								episodes.add(newEpisode);
							}
						}
						if (r.getName().toString().equalsIgnoreCase("atom:link")){
							String href="";
							boolean self=false;
							for (int count=0; count < r.getAttributeCount(); count++){
								if (r.getAttributeName(count).toString().equalsIgnoreCase("href"))
									href=r.getAttributeValue(count);
								if (r.getAttributeName(count).toString().equalsIgnoreCase("rel"))
									self=r.getAttributeValue(count).equalsIgnoreCase("self");
							}
							if (self)
								atomLink=href;
						}
						/*
						 * We are not currently using the Author for individual episodes. We may decide to change
						 * this later.
						if (r.getName().toString().equalsIgnoreCase("{"+itunes+"}author")){
							if (newEpisode!=null)
								newEpisode.setAuthor(r.getElementText());
						}*/
						break;
				}
				
				if (!r.hasNext())
					break;
				event = r.next();
			}
			r.close();
		} catch (XMLStreamException e) {
		}
		
		return episodes;
	}
	
	public Vector<Menu> parseMenus(InputStream file){
		
		return null;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAtomLink() {
	    return atomLink;	
	}
	
	public void setAtomLink(String href){
		atomLink=href;
	}
}
