import java.util.Date;
public class basicblock {

	public 	String outputHash;
	public  String inputHash;
	private String blockData; 
	private long time;
	
	public basicblock(String blockData,String inputHash ) {
		this.blockData = blockData;
		this.inputHash = inputHash;
		this.time = new Date().getTime();
	}
}
