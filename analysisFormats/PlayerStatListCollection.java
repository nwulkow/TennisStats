package analysisFormats;

import java.util.ArrayList;

public class PlayerStatListCollection extends ArrayList<PlayerStatList>{
	
	public PlayerStatListCollection(){
		super();
	}

	
	public void printCollection(){
		for(PlayerStatList pls : this){
			pls.print();
		}
	}
}
