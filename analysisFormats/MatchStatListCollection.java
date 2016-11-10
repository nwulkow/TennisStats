package analysisFormats;

import java.util.ArrayList;

public class MatchStatListCollection extends ArrayList<MatchStatList>{

	public MatchStatListCollection(){
		super();
	}

	
	public void printCollection(){
		for(MatchStatList msl : this){
			msl.print();
		}
	}
	
}
