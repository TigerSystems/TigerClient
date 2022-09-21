package de.MarkusTieger;

import java.io.IOException;

public interface IVersionFetchable {
	
	public ClientVersion fetch() throws IOException, InterruptedException;

}
